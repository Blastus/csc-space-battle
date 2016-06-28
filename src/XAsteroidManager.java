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
    private static final int STARTING_ASTEROID_DIAMETER = 50;
    private static final int CAN_CREATE_FRAGMENTS = 25;
    private static final int STARTING_ASTEROID_SPEED = 2;
    private static final int SAFETY_SCALE = 3;
    private static final int TIME_MULTIPLIER = 20;
    private static final int ADDITIONAL_TIME = 200;
    private static final double[] FRAGMENT_SPEED_MULTIPLIERS = {
            1.5,
            2.0
    };
    private final Dimension size;
    private final int asteroidCountIncrement;
    private final XPlayer player;
    private final XSpecialEffects specialEffects;
    private final ArrayList<XAsteroid> asteroids;
    private int currentAsteroidCount;

    XAsteroidManager(Dimension size, XPlayer player, XSpecialEffects specialEffects) {
        this.size = size;
        this.currentAsteroidCount = STARTING_ASTEROID_COUNT;
        this.asteroidCountIncrement = ASTEROID_COUNT_INCREMENT;
        this.player = player;
        this.specialEffects = specialEffects;
        this.asteroids = new ArrayList<>();
        this.spawn();
    }

    private void spawn() {
        XVector position = new XVector();
        int safetyMargin = ((STARTING_ASTEROID_DIAMETER >> 1) + XPlayer.RADIUS) * SAFETY_SCALE;
        XVector playerPosition = this.player.getPosition();
        IntStream.range(0, this.currentAsteroidCount).forEach(a -> {
            do {
                position.random(this.size);
            } while (position.sub(playerPosition).getMagnitude() < safetyMargin);
            this.asteroids.add(new XAsteroid(
                    new XCircle(position.copy(), STARTING_ASTEROID_DIAMETER),
                    XVector.polar(STARTING_ASTEROID_SPEED, XSpaceBattle.CHAOS.uniform(XVector.CIRCLE_8_8))));
        });
        this.currentAsteroidCount += this.asteroidCountIncrement;
    }

    void move(boolean cheating, long currentTime) {
        XCircle playerShape = this.player.isAlive() ?
                new XCircle(this.player.getPosition(), XPlayer.RADIUS << 1) :
                null;
        ArrayList<XAsteroid> collisions = this.asteroids.stream().filter(asteroid -> {
            asteroid.move(this.size);
            return playerShape != null && asteroid.getShape().overlaps(playerShape);
        }).collect(Collectors.toCollection(ArrayList<XAsteroid>::new));
        if (collisions.size() > 0) {
            if (!cheating)
                this.player.kill(currentTime);
            this.destroy(collisions, cheating, currentTime);
        }
    }

    void draw(Graphics surface) {
        this.asteroids.stream().forEach(asteroid -> asteroid.draw(surface));
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
                .filter(asteroid -> asteroid.getShape().overlaps(weapon))
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
                        currentTime);
            }
            // Create debris from the destruction.
            this.specialEffects.spawn(asteroid, currentTime);
            this.createFragments(asteroid);
        });
        this.asteroids.removeAll(deadAsteroids);
    }

    private void createFragments(XAsteroid asteroid) {
        if (asteroid.getDiameter() >= CAN_CREATE_FRAGMENTS) {
            Arrays.stream(FRAGMENT_SPEED_MULTIPLIERS).forEach(speedMultiplier -> {
                XVector position = asteroid.getPosition().copy();
                XVector velocity = XVector.polar(
                        asteroid.getVelocity().getMagnitude() * speedMultiplier,
                        XSpaceBattle.CHAOS.uniform(XVector.CIRCLE_8_8));
                XAsteroid newAsteroid = new XAsteroid(new XCircle(position, asteroid.getRadius()), velocity);
                if (XSpaceBattle.CHAOS.random() > XTeslaStrike.FRAGMENT_HIT_CHANCE)
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
