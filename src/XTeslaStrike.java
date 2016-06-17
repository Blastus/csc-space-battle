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

    XTeslaStrike(XPlayer player, XAsteroidManager asteroidManager, XWeaponManager weaponManager, long currentTime) {
        this.segments = new ArrayList<>();
        this.segments.add(new XLightningSegment(
                player.getNosePosition(),
                player.getDirection() + XVector.CIRCLE_4_8,
                asteroidManager,
                XTeslaStrike.tag++,
                weaponManager,
                1,
                this.segments,
                currentTime));
    }

    void move(Dimension size, long currentTime) {
        ArrayList<XLightningSegment> created = new ArrayList<>();
        this.segments.forEach(segment -> segment.move(size, created, currentTime));
        this.segments.addAll(created);
    }

    void draw(Graphics surface) {
        this.segments.removeIf(segment -> segment.draw(surface));
    }

    boolean isAlive() {
        return !this.segments.isEmpty();
    }
}
