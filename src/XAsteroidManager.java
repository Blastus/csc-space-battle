import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XAsteroidManager {
    private static final int STARTING_ASTEROID_COUNT = 6;
    private static final int STARTING_ASTEROID_DIAMETER = 50;
    private static final int STARTING_ASTEROID_SPEED = 2;
    private static final int SAFETY_SCALE = 3;
    private Dimension size;
    private XPlayer player;
    private ArrayList<XAsteroid> asteroids;

    XAsteroidManager(Dimension size, XPlayer player) {
        this.size = size;
        this.player = player;
        this.asteroids = new ArrayList<>();
    }

    void spawn() {
        XVector asteroidPosition = new XVector();
        int safetyMargin = ((STARTING_ASTEROID_DIAMETER >> 1) + XPlayer.RADIUS) * SAFETY_SCALE;
        XVector playerPosition = this.player.getPosition();
        IntStream.range(0, STARTING_ASTEROID_COUNT).forEach(a -> {
            do {
                asteroidPosition.random(this.size);
            } while (asteroidPosition.sub(playerPosition).getMagnitude() < safetyMargin);
            this.asteroids.add(new XAsteroid(
                    new XCircle(asteroidPosition.copy(), STARTING_ASTEROID_DIAMETER),
                    XVector.polar(STARTING_ASTEROID_SPEED, XSpaceBattle.CHAOS.uniform(XVector.CIRCLE_8_8))
            ));
        });
    }

    void move() {
        this.asteroids.forEach(asteroid -> asteroid.move(this.size));
    }

    void draw(Graphics surface) {
        this.asteroids.forEach(asteroid -> asteroid.draw(surface));
    }
}
