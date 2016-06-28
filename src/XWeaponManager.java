import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XWeaponManager {
    static final int MAX_WEAPON_SPEED = 15;
    private static final int MAX_WEAPON_COUNT = 200;
    private static final int HYPERSPACE_STORM_EFFECT_SIZE = 15;
    private static final int HYPERSPACE_STORM_PARTICLE_SIZE = 10;
    private static final int HYPERSPACE_STORM_PARTICLE_COUNT = 10;
    private static final int STATUS_WIDTH = 200;
    private static final int STATUS_HEIGHT = 20;
    private static final XColor[] STATUS_PALETTE = {
            XColor.RED,
            XColor.YELLOW,
            XColor.GREEN
    };
    private static final int[] WEAPON_COOLING_TIMES = {
            XRotaryCannon.COOLING_TIME,
            XGuidedMissile.COOLING_TIME,
            XSpaceMine.COOLING_TIME,
            XClusterCrack.COOLING_TIME
    };
    static final int SUPPORTED_WEAPONS = WEAPON_COOLING_TIMES.length;
    private static final String[] WEAPON_NAMES = {
            "Rotary Cannon",
            "Guided Missile",
            "Space Mine",
            "Cluster Crack"
    };
    private static final int HYPERSPACE_STORM_SIZE = 50;
    private final Dimension size;
    private final XAsteroidManager asteroidManager;
    private final XHealthManager healthManager;
    private final XInput input;
    private final XPlayer player;
    private final XSpecialEffects specialEffects;
    private final ArrayList<XWeapon> weapons;
    private final ArrayList<XWeapon> newWeapons;
    private long lastFireTime;

    XWeaponManager(
            Dimension size,
            XAsteroidManager asteroidManager,
            XHealthManager healthManager,
            XInput input,
            XPlayer player,
            XSpecialEffects specialEffects
    ) {
        this.size = size;
        this.asteroidManager = asteroidManager;
        this.healthManager = healthManager;
        this.input = input;
        this.player = player;
        this.specialEffects = specialEffects;
        this.weapons = new ArrayList<>();
        this.lastFireTime = 0;
        this.newWeapons = new ArrayList<>();
    }

    void move(long currentTime) {
        // Move all live ammunition and collide with asteroids.
        this.weapons.removeIf(weapon -> {
            weapon.move(this.size);
            ArrayList<XAsteroid> asteroids = this.asteroidManager.findInRange(weapon);
            int hitAsteroids = asteroids.size();
            if (hitAsteroids > 0) {
                this.healthManager.addPoints(hitAsteroids);
                // Destroying asteroids may also create asteroids, but this weapon cannot hit the new ones.
                this.asteroidManager.destroy(asteroids, currentTime);
                return true;
            }
            return !weapon.isAlive(currentTime);
        });
        // New weapons do not need to be moved, but they do need to be drawn.
        // Note that they cannot interact with asteroids in the first frame.
        if (!this.newWeapons.isEmpty()) {
            this.weapons.addAll(this.newWeapons);
            this.newWeapons.clear();
        }
    }

    void drawProjectiles(Graphics surface, long currentTime) {
        this.weapons.stream().forEach(weapon -> weapon.draw(surface, currentTime));
    }

    void drawStatus(Graphics surface, XVector offset, long currentTime) {
        int requestedWeapon = this.input.requestsWeapon();
        // Do not draw the status for the Rotary Cannon.
        if (requestedWeapon != 0) {
            double timeSinceLastFire = currentTime - this.lastFireTime;
            double status = Math.min(1.0, timeSinceLastFire / WEAPON_COOLING_TIMES[requestedWeapon]);
            XVector canvasAnchorPosition = new XVector(0, this.size.getHeight());
            XVector figureAnchorPosition = new XVector(0, STATUS_HEIGHT);
            XVector finalPosition = canvasAnchorPosition.sub(figureAnchorPosition).add(offset);
            surface.setColor(XColor.interpolate(status, STATUS_PALETTE).value());
            surface.fillRoundRect(
                    finalPosition.getIntX(),
                    finalPosition.getIntY(),
                    (int) (status * STATUS_WIDTH) + STATUS_HEIGHT,
                    STATUS_HEIGHT,
                    STATUS_HEIGHT,
                    STATUS_HEIGHT);
            surface.setColor(XColor.interpolate(1 - status, STATUS_PALETTE).value());
            surface.drawRoundRect(
                    finalPosition.getIntX(),
                    finalPosition.getIntY(),
                    (int) (status * STATUS_WIDTH) + STATUS_HEIGHT,
                    STATUS_HEIGHT,
                    STATUS_HEIGHT,
                    STATUS_HEIGHT);
        }
    }

    void handleFireRequest(long currentTime) {
        int requestedWeapon = this.input.requestsWeapon();
        if (this.input.requestsFire() &&
                this.player.isAlive() &&
                this.weapons.size() < MAX_WEAPON_COUNT &&
                currentTime - this.lastFireTime > WEAPON_COOLING_TIMES[requestedWeapon]) {
            this.lastFireTime = currentTime;
            switch (requestedWeapon) {
                case 0:
                    // Rotary Cannon
                    this.newWeapons.add(new XRotaryCannon(
                            this.player.getPosition().copy(),
                            this.getProjectileVelocity(),
                            currentTime));
                    break;
                case 1:
                    // Guided Missile
                    this.newWeapons.add(new XGuidedMissile(
                            this.player.getPosition().copy(),
                            this.getProjectileVelocity(),
                            this.asteroidManager,
                            currentTime));
                    break;
                case 2:
                    // Space Mine
                    this.newWeapons.add(new XSpaceMine(this.player.getPosition().copy(), currentTime));
                    break;
                case 3:
                    // Cluster Crack
                    this.newWeapons.addAll(XClusterCrack.fire(this.player, currentTime));
                    break;
                default:
                    throw new RuntimeException("requested weapon case was not handled");
            }
        }
    }

    String getCurrentWeaponName() {
        return WEAPON_NAMES[this.input.requestsWeapon()];
    }

    private XVector getProjectileVelocity() {
        return this.player.getVelocity().add(XVector.polar(
                MAX_WEAPON_SPEED,
                XVector.CIRCLE_2_8 - this.player.getDirection()));
    }

    void initiateHyperspaceStorm(long currentTime) {
        IntStream.range(0, HYPERSPACE_STORM_SIZE).forEach(a -> {
            XVector position = new XVector();
            position.random(this.size);
            this.specialEffects.spawn(
                    position,
                    HYPERSPACE_STORM_EFFECT_SIZE,
                    HYPERSPACE_STORM_PARTICLE_SIZE,
                    HYPERSPACE_STORM_PARTICLE_COUNT,
                    XHyperspaceManager.LIVING_LIFE_SPAN,
                    XSpecialEffects.COOL,
                    currentTime);
            this.weapons.add(new XWeapon(
                    position,
                    (int) (HYPERSPACE_STORM_EFFECT_SIZE * XExplosion.START_SIZE),
                    currentTime));
        });
    }
}
