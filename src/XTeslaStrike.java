import java.awt.*;
import java.util.ArrayList;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XTeslaStrike {
    static final double FRAGMENT_HIT_CHANCE = 0.5;
    static final int COOLING_TIME = 7500;
    private static int tag = 1;
    private final ArrayList<XLightningSegment> segments;

    XTeslaStrike(
            Dimension size,
            XPlayer player,
            XAsteroidManager asteroidManager,
            XWeaponManager weaponManager,
            long currentTime
    ) {
        this.segments = new ArrayList<>();
        this.segments.add(new XLightningSegment(
                size,
                player.getNosePosition(),
                player.getDirection(),
                asteroidManager,
                XTeslaStrike.tag++,
                weaponManager,
                this.segments,
                currentTime
        ));
    }

    void move(long currentTime) {
        ArrayList<XLightningSegment> created = new ArrayList<>();
        this.segments.forEach(segment -> segment.move(created, currentTime));
        this.segments.addAll(created);
    }

    void draw(Graphics surface) {
        this.segments.removeIf(segment -> segment.draw(surface));
    }

    boolean isAlive() {
        return !this.segments.isEmpty();
    }
}
