import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 9 June 2016.
 */
class XSpecialEffects {
    private static double MIN_DEBRIS_VELOCITY = 0.5;
    private static double MAX_DEBRIS_VELOCITY = 1.5;
    private final Dimension size;
    private final ArrayList<XDebris> debris;
    private final ArrayList<XExplosion> explosions;

    XSpecialEffects(Dimension size) {
        this.size = size;
        this.debris = new ArrayList<>();
        this.explosions = new ArrayList<>();
    }

    void spawn(XAsteroid asteroid, long currentTime) {
        IntStream.range(0, asteroid.getRadius()).forEach(a -> {
            XVector position = asteroid.getPosition().copy();
            XVector velocity = XVector.polar(
                    asteroid.getVelocity().getMagnitude() * XSpaceBattle.CHAOS.uniform(
                            MIN_DEBRIS_VELOCITY,
                            MAX_DEBRIS_VELOCITY
                    ),
                    XSpaceBattle.CHAOS.uniform(XVector.CIRCLE_8_8)
            );
            int diameter = XSpaceBattle.CHAOS.randomInteger(2, 4);
            this.debris.add(new XDebris(this.size, position, velocity, diameter, currentTime));
        });
    }

    void moveDebris() {
        this.debris.forEach(XDebris::move);
    }

    void drawDebris(Graphics surface, long currentTime) {
        this.debris.removeIf(particle -> particle.draw(surface, currentTime));
    }

    void spawn(XVector position, int effectSize, int particleSize, int particleCount, int lifeSpan, long currentTime) {
        this.explosions.add(new XExplosion(position, effectSize, particleSize, particleCount, lifeSpan, currentTime));
    }

    void drawExplosions(Graphics surface, long currentTime) {
        this.explosions.removeIf(explosion -> explosion.draw(surface, currentTime));
    }
}
