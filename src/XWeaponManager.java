import java.awt.*;
import java.util.ArrayList;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XWeaponManager {
    private static final int MAX_WEAPON_SPEED = 15;
    private static final int MAX_WEAPON_COUNT = 200;
    private static final int[] WEAPON_COOLING_TIMES = {
            XWeapon.COOLING_TIME
    };
    private final Dimension size;
    private final XAsteroidManager asteroidManager;
    private final XInput input;
    private final XPlayer player;
    private final ArrayList<XWeapon> weapons;
    private final ArrayList<XWeapon> newWeapons;
    private long lastFireTime;

    XWeaponManager(Dimension size, XAsteroidManager asteroidManager, XInput input, XPlayer player) {
        this.size = size;
        this.asteroidManager = asteroidManager;
        this.input = input;
        this.player = player;
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
                // Destroying asteroids may also create asteroids, but this weapon cannot hit the new ones.
                this.asteroidManager.destroy(asteroids);
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

    void drawProjectiles(Graphics surface) {
        this.weapons.stream().forEach(weapon -> weapon.draw(surface));
    }

    void handleFireRequest(long currentTime) {
        if (this.input.requestsFire() &&
                this.player.isAlive() &&
                this.weapons.size() < MAX_WEAPON_COUNT &&
                currentTime - this.lastFireTime > WEAPON_COOLING_TIMES[0]) {
            this.lastFireTime = currentTime;
            this.newWeapons.add(new XWeapon(
                    this.player.getPosition().copy(),
                    this.getProjectileVelocity(),
                    currentTime));
        }
    }

    private XVector getProjectileVelocity() {
        return this.player.getVelocity().add(XVector.polar(
                MAX_WEAPON_SPEED,
                XVector.CIRCLE_2_8 - this.player.getDirection()));
    }
}
