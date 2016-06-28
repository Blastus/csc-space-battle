import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 9 June 2016.
 */
class XSpecialEffects {
    private static double MIN_DEBRIS_VELOCITY;
    private static double MAX_DEBRIS_VELOCITY;
    private final Dimension size;
    private final ArrayList<XDebris> debris;
    private final ArrayList<XExplosion> explosions;

    XSpecialEffects(Dimension size) {
        this.size = size;
        this.debris = new ArrayList<>();
        this.explosions = new ArrayList<>();
    }

    void spawn(XAsteroid asteroid, long currentTime) {
    }

    void moveDebris() {
    }

    void drawDebris(Graphics surface, long currentTime) {
    }

    void spawn(XVector position, int effectSize, int particleSize, int particleCount, int lifeSpan, long currentTime) {
    }

    void moveExplosions(long currentTime) {
    }

    void drawExplosions(Graphics surface, long currentTime) {
    }
}
