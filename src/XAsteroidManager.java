import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XAsteroidManager {
    private static final int STARTING_ASTEROID_COUNT = 2;
    private static final int ASTEROID_COUNT_INCREMENT = 1;
    private static final int STARTING_ASTEROID_RADIUS = 25;
    private static final int CAN_CREATE_FRAGMENTS = 12;
    private static final int STARTING_ASTEROID_SPEED = 2;
    private static final int SAFETY_SCALE = 3;
    private static final int TIME_MULTIPLIER = 20;
    private static final int ADDITIONAL_TIME = 200;
    private static final double[] FRAGMENT_SPEED_MULTIPLIERS = {
            1.5,
            2.0
    };
    private final Dimension size;
    private final XPlayer player;
    private final XSpecialEffects specialEffects;
    private final ArrayList<XAsteroid> asteroids;
    private int currentAsteroidCount;

    XAsteroidManager(Dimension size, XPlayer player, XSpecialEffects specialEffects) {
        this.size = size;
        this.currentAsteroidCount = STARTING_ASTEROID_COUNT;
        this.player = player;
        this.specialEffects = specialEffects;
        this.asteroids = new ArrayList<>();
        this.spawn();
    }

    private void spawn() {
        XVector asteroidPosition = new XVector();
        int safetyMargin = (STARTING_ASTEROID_RADIUS + this.player.getRadius()) * SAFETY_SCALE;
        XVector playerPosition = this.player.getPosition();
        IntStream.range(0, this.currentAsteroidCount).forEach(a -> {
            do {
                asteroidPosition.random(this.size);
            } while (asteroidPosition.sub(playerPosition).getMagnitude() < safetyMargin);
            this.asteroids.add(new XAsteroid(
                    this.size,
                    new XCircle(asteroidPosition.copy(), STARTING_ASTEROID_RADIUS),
                    XVector.polar(STARTING_ASTEROID_SPEED, XRandom.sVonMisesVariate())
            ));
        });
        this.currentAsteroidCount += ASTEROID_COUNT_INCREMENT;
    }

    void move(boolean cheating, long currentTime) {
        ArrayList<XAsteroid> collisions = this.asteroids.stream().filter(asteroid -> {
            asteroid.move();
            return this.player.isAlive() && asteroid.overlaps(this.player);
        }).collect(Collectors.toCollection(ArrayList<XAsteroid>::new));
        if (collisions.size() > 0) {
            if (!cheating)
                this.player.kill(currentTime);
            this.destroy(collisions, cheating, currentTime);
        }
    }

    void draw(Graphics surface) {
        this.asteroids.forEach(asteroid -> asteroid.draw(surface));
    }

    XAsteroid findClosest(XVector position) {
        return this.findClosest(position, XAsteroid.NEUTRAL_TAG, Double.POSITIVE_INFINITY);
    }

    XAsteroid findClosest(XVector position, int avoidTag, double maxDistance) {
        XAsteroid match = null;
        double distance = maxDistance;
        for (XAsteroid asteroid : this.asteroids) {
            if (asteroid.getTag() != avoidTag) {
                double difference = position.sub(asteroid.getPosition()).getMagnitude() - asteroid.getRadius();
                if (difference < distance) {
                    match = asteroid;
                    distance = difference;
                }
            }
        }
        return match;
    }

    ArrayList<XAsteroid> findInRange(XWeapon weapon) {
        return this.asteroids.stream()
                .filter(asteroid -> asteroid.overlaps(weapon))
                .collect(Collectors.toCollection(ArrayList<XAsteroid>::new));
    }

    void destroy(ArrayList<XAsteroid> asteroids, long currentTime) {
        this.destroy(asteroids, true, currentTime);
    }

    private void destroy(ArrayList<XAsteroid> deadAsteroids, boolean createAsteroidExplosions, long currentTime) {
        deadAsteroids.forEach(asteroid -> {
            if (createAsteroidExplosions) {
                int diameter = asteroid.getDiameter();
                int radius = asteroid.getRadius();
                this.specialEffects.spawn(
                        asteroid.getPosition(),
                        diameter,
                        radius,
                        radius,
                        diameter * TIME_MULTIPLIER + ADDITIONAL_TIME,
                        XSpecialEffects.WARM,
                        currentTime
                );
            }
            // Create debris from the destruction.
            this.specialEffects.spawn(asteroid, currentTime);
            this.createFragments(asteroid);
        });
        this.asteroids.removeAll(deadAsteroids);
    }

    private void createFragments(XAsteroid asteroid) {
        if (asteroid.getRadius() >= CAN_CREATE_FRAGMENTS) {
            Arrays.stream(FRAGMENT_SPEED_MULTIPLIERS).forEach(speedMultiplier -> {
                XVector position = asteroid.getPosition().copy();
                XVector velocity = XVector.polar(
                        asteroid.getVelocity().getMagnitude() * speedMultiplier,
                        XRandom.sVonMisesVariate()
                );
                XAsteroid newAsteroid = new XAsteroid(
                        this.size,
                        new XCircle(position, asteroid.getRadius() >> 1),
                        velocity
                );
                if (XRandom.sRandom() > XTeslaStrike.FRAGMENT_HIT_CHANCE)
                    // Asteroids may not be hit by a tesla strike if they have the same tag as the tesla strike.
                    newAsteroid.copyTag(asteroid);
                this.asteroids.add(newAsteroid);
            });
        }
    }

    ArrayList<XAsteroid> getAsteroids() {
        return this.asteroids;
    }

    void ensureAsteroidAvailability() {
        if (this.asteroids.isEmpty())
            this.spawn();
    }
}
