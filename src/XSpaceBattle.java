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
    private static final Color BACKGROUND_COLOR = XColor.BLACK.value();
    private static final int DEATH_HANDLER_DELAY = XPlayer.DEATH_LIFE_SPAN + 3000;
    private final XInput input;
    private final Canvas context;
    private final XPlayer player;
    private final XAsteroidManager asteroidManager;
    private final XWeaponManager weaponManager;
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
        this.weaponManager = new XWeaponManager(
                size,
                this.asteroidManager,
                this.input,
                this.player
        );
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
        switch (event.getActionCommand()) {
            case "update":
                if (this.input.requestsExit())
                    SwingUtilities.invokeLater(this::dispose);
                this.updatePhysics();
                this.updateGraphics();
                break;
            case "revive":
                this.player.revive();
                break;
            default:
                throw new RuntimeException("action command was not recognized");
        }
    }

    private void updatePhysics() {
        long currentTime = System.currentTimeMillis();
        this.player.move();
        this.weaponManager.handleFireRequest(currentTime);
        this.asteroidManager.move();
        this.weaponManager.move(currentTime);
        this.asteroidManager.ensureAsteroidAvailability();
    }

    private void updateGraphics() {
        int width = this.context.getWidth();
        int height = this.context.getHeight();
        Image buffer = this.context.createImage(width, height);
        Graphics surface = buffer.getGraphics();
        // Fill in the background.
        surface.setColor(BACKGROUND_COLOR);
        surface.fillRect(0, 0, width, height);
        this.drawAllWorldObjects(surface);
        this.context.getGraphics().drawImage(buffer, 0, 0, null);
    }

    private void drawAllWorldObjects(Graphics surface) {
        this.asteroidManager.draw(surface);
        this.weaponManager.drawProjectiles(surface);
        // Always draw the player last.
        this.player.draw(surface);
    }
}
