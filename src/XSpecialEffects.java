import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 9 June 2016.
 */
class XSpecialEffects {
    static final XColor[] COOL = {
            XColor.BLUE,
            XColor.WHITE
    };
    static final XColor[] WARM = {
            XColor.RED,
            XColor.YELLOW
    };
    private static final double MIN_DEBRIS_VELOCITY = 0.5;
    private static final double MAX_DEBRIS_VELOCITY = 1.5;
    private static final int EXPLOSION_EMITTER_LIFE_SPAN = 500;
    private static final int HYPERSPACE_EMITTER_LIFE_SPAN = 1000;
    private final Dimension size;
    private final ArrayList<XExplosion> explosions;
    private final ArrayList<XTimothyExplosion> timothyExplosions;
    private final ArrayList<XTimothyHyperspace> timothyHyperspaces;
    private final ArrayList<XDebris> debris;
    private boolean useNewEffects;

    XSpecialEffects(Dimension size) {
        this.size = size;
        this.useNewEffects = false;
        this.explosions = new ArrayList<>();
        this.timothyExplosions = new ArrayList<>();
        this.timothyHyperspaces = new ArrayList<>();
        this.debris = new ArrayList<>();
    }

    void toggleEffects() {
        this.useNewEffects = !this.useNewEffects;
    }

    void spawn(
            XVector position,
            int effectSize,
            int particleSize,
            int particleCount,
            int lifeSpan,
            XColor[] palette,
            long currentTime) {
        if (!this.useNewEffects)
            this.explosions.add(new XExplosion(
                    position,
                    effectSize,
                    particleSize,
                    particleCount,
                    lifeSpan,
                    palette,
                    currentTime));
        else if (palette == WARM)
            this.timothyExplosions.add(new XTimothyExplosion(position, EXPLOSION_EMITTER_LIFE_SPAN, currentTime));
        else if (palette == COOL)
            this.timothyHyperspaces.add(new XTimothyHyperspace(position, HYPERSPACE_EMITTER_LIFE_SPAN, currentTime));
        else
            throw new RuntimeException("palette could not be handled");
    }

    void moveExplosions(long currentTime) {
        this.timothyExplosions.forEach(explosion -> explosion.move(currentTime));
        this.timothyHyperspaces.forEach(explosion -> explosion.move(currentTime));
    }

    void drawExplosions(Graphics surface, long currentTime) {
        this.explosions.removeIf(explosion -> explosion.draw(surface, currentTime));
        this.timothyExplosions.removeIf(explosion -> explosion.draw(surface, currentTime));
        this.timothyHyperspaces.removeIf(hyperspace -> hyperspace.draw(surface, currentTime));
    }

    void spawn(XAsteroid asteroid, long currentTime) {
        IntStream.range(0, asteroid.getRadius()).forEach(a -> {
            XVector position = asteroid.getPosition().copy();
            XVector velocity = XVector.polar(
                    asteroid.getVelocity().getMagnitude() * XSpaceBattle.CHAOS.uniform(
                            MIN_DEBRIS_VELOCITY,
                            MAX_DEBRIS_VELOCITY),
                    XSpaceBattle.CHAOS.uniform(XVector.CIRCLE_8_8));
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
}
