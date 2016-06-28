import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Created by Stephen "Zero" Chappell on 1 June 2016.
 */
class XEngine implements ActionListener {
    private static final Font TEXT_STYLE = new Font("Verdana", Font.PLAIN, 25);
    private static final XColor WEAPON_COLOR = XColor.RED;
    private static final XColor POINTS_COLOR = XColor.GREEN;
    private static final Color BACKGROUND_COLOR = XColor.BLACK.value();
    private static final int DEATH_HANDLER_DELAY = XPlayer.DEATH_LIFE_SPAN + 3000;
    private static final XAnchor WEAPON_CANVAS_ANCHOR = XAnchor.NORTH_WEST;
    private static final XAnchor WEAPON_STRING_ANCHOR = XAnchor.NORTH_WEST;
    private static final XVector WEAPON_OFFSET = new XVector(+20, +40);
    private static final XAnchor POINTS_CANVAS_ANCHOR = XAnchor.NORTH_EAST;
    private static final XAnchor POINTS_STRING_ANCHOR = XAnchor.NORTH_EAST;
    private static final XVector POINTS_OFFSET = new XVector(-20, +40);
    private static final XVector WEAPON_STATUS_OFFSET = new XVector(+20, -10);
    private final XInput input;
    private final Canvas context;
    private final Runnable shutdown;
    private final XSpecialEffects specialEffects;
    private final XPlayer player;
    private final XStarField starField;
    private final XAsteroidManager asteroidManager;
    private final XHealthManager healthManager;
    private final XWeaponManager weaponManager;
    private final XHyperspaceManager hyperspaceManager;
    private final XTextWriter weaponWriter;
    private final XTextWriter pointsWriter;
    private boolean enableCheating;

    XEngine(XInput input, Canvas context, Runnable shutdown) {
        this.input = input;
        this.context = context;
        this.shutdown = shutdown;
        Dimension size = context.getSize();
        this.specialEffects = new XSpecialEffects(size);
        long currentTime = System.currentTimeMillis();
        this.player = new XPlayer(
                size,
                this.input,
                new XVector(context.getWidth() >> 1, context.getHeight() >> 1),
                this.specialEffects,
                () -> {
                    Timer resuscitator = new Timer(DEATH_HANDLER_DELAY, this);
                    resuscitator.setActionCommand("revive");
                    resuscitator.start();
                },
                currentTime);
        this.enableCheating = false;
        this.starField = new XStarField(size);
        this.asteroidManager = new XAsteroidManager(
                size,
                this.player,
                this.specialEffects);
        this.healthManager = new XHealthManager(
                size,
                XPlayer.getShape(XVector.CIRCLE_2_8));
        this.weaponManager = new XWeaponManager(
                size,
                this.asteroidManager,
                this.healthManager,
                this.input,
                this.player,
                this.specialEffects);
        this.hyperspaceManager = new XHyperspaceManager(
                size,
                this.input,
                this.player,
                this.asteroidManager,
                this.specialEffects,
                this.weaponManager,
                currentTime);
        this.weaponWriter = new XTextWriter(size, TEXT_STYLE, WEAPON_COLOR);
        this.pointsWriter = new XTextWriter(size, TEXT_STYLE, POINTS_COLOR);
    }

    public void actionPerformed(ActionEvent event) {
        long currentTime = System.currentTimeMillis();
        switch (event.getActionCommand()) {
            case "update":
                this.checkInput(currentTime);
                this.updatePhysics(currentTime);
                this.updateGraphics(currentTime);
                break;
            case "revive":
                if (!this.healthManager.canRevive())
                    this.shutdown.run();
                else if (this.hyperspaceManager.initiateHyperspaceJump(this.enableCheating, currentTime)) {
                    this.player.revive(currentTime);
                    this.healthManager.commitRevive();
                    ((Timer) event.getSource()).stop();
                }
                break;
            default:
                throw new RuntimeException("action command was not recognized");
        }
    }

    private void checkInput(long currentTime) {
        if (this.input.requestsCheat())
            this.enableCheating = !this.enableCheating;
        this.hyperspaceManager.handlePanicRequest(this.enableCheating, currentTime);
        if (this.input.requestsToggleEffects())
            this.specialEffects.toggleEffects();
        if (this.input.requestsExit())
            this.shutdown.run();
    }

    private void updatePhysics(long currentTime) {
        this.player.move(currentTime);
        this.weaponManager.handleFireRequest(currentTime);
        this.asteroidManager.move(this.enableCheating, currentTime);
        this.weaponManager.move(currentTime);
        this.specialEffects.moveDebris();
        this.asteroidManager.ensureAsteroidAvailability();
        // Support new effect types.
        this.specialEffects.moveExplosions(currentTime);
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
        this.starField.draw(surface);
        this.asteroidManager.draw(surface);
        this.specialEffects.drawDebris(surface, currentTime);
        this.weaponManager.drawProjectiles(surface, currentTime);
        this.specialEffects.drawExplosions(surface, currentTime);
        this.weaponManager.drawLightning(surface);
        // Always draw the player last.
        this.player.draw(surface, currentTime);
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
