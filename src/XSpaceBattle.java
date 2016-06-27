import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XSpaceBattle extends JFrame implements ActionListener {
    static final XRandom CHAOS = new XRandom();
    private static final String WINDOW_TITLE = "Space Battle";
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 40;
    private static final Font TEXT_STYLE = new Font("Verdana", Font.PLAIN, 25);
    private static final XColor WEAPON_COLOR = XColor.RED;
    private static final XColor POINTS_COLOR = XColor.GREEN;
    private static final Color BACKGROUND_COLOR = XColor.BLACK.value();
    private static final int DEATH_HANDLER_DELAY = XPlayer.DEATH_LIFE_SPAN + 3000;
    private static final String WEAPON_CANVAS_ANCHOR = "NORTH_WEST";
    private static final String WEAPON_STRING_ANCHOR = "NORTH_WEST";
    private static final XVector WEAPON_OFFSET = new XVector(+20, +40);
    private static final String POINTS_CANVAS_ANCHOR = "NORTH_EAST";
    private static final String POINTS_STRING_ANCHOR = "NORTH_EAST";
    private static final XVector POINTS_OFFSET = new XVector(-20, +40);
    private static final XVector WEAPON_STATUS_OFFSET = new XVector(+20, -10);
    private final XInput input;
    private final Canvas context;
    private final XPlayer player;
    private final XAsteroidManager asteroidManager;
    private final XHealthManager healthManager;
    private final XWeaponManager weaponManager;
    private final XTextWriter weaponWriter;
    private final XTextWriter pointsWriter;
    private final Timer updater;

    private XSpaceBattle() {
        super(WINDOW_TITLE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.input = new XInput();
        this.context = new Canvas();
        // Add key listener to all components.
        this.addKeyListener(this.input);
        this.context.addKeyListener(this.input);
        this.context.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.add(this.context);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        Dimension size = this.context.getSize();
        this.player = new XPlayer(
                size,
                this.input,
                new XVector(this.context.getWidth() >> 1, this.context.getHeight() >> 1),
                () -> {
                    Timer resuscitator = new Timer(DEATH_HANDLER_DELAY, this);
                    resuscitator.setActionCommand("revive");
                    resuscitator.setRepeats(false);
                    resuscitator.start();
                }
        );
        this.asteroidManager = new XAsteroidManager(size, this.player);
        this.healthManager = new XHealthManager(
                size,
                XPlayer.getShape(XVector.CIRCLE_2_8));
        this.weaponManager = new XWeaponManager(
                size,
                this.asteroidManager,
                this.healthManager,
                this.input,
                this.player);
        this.weaponWriter = new XTextWriter(size, TEXT_STYLE, WEAPON_COLOR);
        this.pointsWriter = new XTextWriter(size, TEXT_STYLE, POINTS_COLOR);
        this.updater = new Timer(1000 / FRAMES_PER_SECOND, this);
        this.updater.setActionCommand("update");
        this.updater.start();
    }

    public static void main(String[] argv) {
        new XSpaceBattle();
    }

    public void dispose() {
        this.updater.stop();
        super.dispose();
    }

    public void actionPerformed(ActionEvent event) {
        long currentTime = System.currentTimeMillis();
        switch (event.getActionCommand()) {
            case "update":
                if (this.input.requestsExit())
                    SwingUtilities.invokeLater(this::dispose);
                this.updatePhysics(currentTime);
                this.updateGraphics(currentTime);
                break;
            case "revive":
                if (this.healthManager.canRevive()) {
                    this.player.revive();
                    this.healthManager.commitRevive();
                } else
                    SwingUtilities.invokeLater(this::dispose);
                break;
            default:
                throw new RuntimeException("action command was not recognized");
        }
    }

    private void updatePhysics(long currentTime) {
        this.player.move();
        this.weaponManager.handleFireRequest(currentTime);
        this.asteroidManager.move();
        this.weaponManager.move(currentTime);
        this.asteroidManager.ensureAsteroidAvailability();
    }

    private void updateGraphics(long currentTime) {
        int width = this.context.getWidth();
        int height = this.context.getHeight();
        Image buffer = this.context.createImage(width, height);
        Graphics surface = buffer.getGraphics();
        // Fill in the background.
        surface.setColor(BACKGROUND_COLOR);
        surface.fillRect(0, 0, width, height);
        this.drawAllWorldObjects(surface, currentTime);
        this.drawHeadsUpDisplay(surface, currentTime);
        this.context.getGraphics().drawImage(buffer, 0, 0, null);
    }

    private void drawAllWorldObjects(Graphics surface, long currentTime) {
        this.asteroidManager.draw(surface);
        this.weaponManager.drawProjectiles(surface, currentTime);
        // Always draw the player last.
        this.player.draw(surface);
    }

    private void drawHeadsUpDisplay(Graphics surface, long currentTime) {
        this.weaponWriter.write(
                surface,
                this.weaponManager.getCurrentWeaponName(),
                WEAPON_CANVAS_ANCHOR,
                WEAPON_STRING_ANCHOR,
                WEAPON_OFFSET);
        this.pointsWriter.write(
                surface,
                Integer.toString(this.healthManager.getScore()),
                POINTS_CANVAS_ANCHOR,
                POINTS_STRING_ANCHOR,
                POINTS_OFFSET);
        this.weaponManager.drawStatus(surface, WEAPON_STATUS_OFFSET, currentTime);
        this.healthManager.draw(surface);
    }
}
