import java.awt.*;
import java.util.ArrayList;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XLightningSegment {
    private static final int SEGMENT_LENGTH = 25;
    private static final double DIRECTION_DEVIANCE = 0.1;
    private static final int FRAME_MOVE_DELAY = 1;
    private static final int FRAME_LIFE_SPAN = 4;
    private static final int MIN_TREE_DEPTH = 10;
    private static final int MAX_TREE_DEPTH = 50;
    private static final double BRANCH_RATE = 0.2;
    private static final double BRANCH_DEVIANCE = 0.6;
    private static final XColor[] COLOR_POSSIBILITIES = {
            XColor.BLUE,
            XColor.WHITE,
            XColor.YELLOW
    };
    private final Dimension size;
    private final XVector position;
    private final double direction;
    private final XAsteroidManager asteroidManager;
    private final int tag;
    private final XWeaponManager weaponManager;
    private final XVector endPoint;
    private final int treeDepth;
    private final ArrayList<XLightningSegment> segments;
    private int frames;

    XLightningSegment(
            Dimension size,
            XVector position,
            double direction,
            XAsteroidManager asteroidManager,
            int tag,
            XWeaponManager weaponManager,
            ArrayList<XLightningSegment> segments,
            long currentTime
    ) {
        this(size, position, direction, asteroidManager, tag, weaponManager, 1, segments, currentTime);
    }

    private XLightningSegment(
            Dimension size,
            XVector position,
            double direction,
            XAsteroidManager asteroidManager,
            int tag,
            XWeaponManager weaponManager,
            int treeDepth,
            ArrayList<XLightningSegment> segments,
            long currentTime
    ) {
        this.size = size;
        this.position = position;
        this.direction = direction + XRandom.sUniform(-DIRECTION_DEVIANCE, +DIRECTION_DEVIANCE);
        this.asteroidManager = asteroidManager;
        this.tag = tag;
        this.weaponManager = weaponManager;
        // Do not find an asteroid that has been given immunity to this tesla strike.
        XAsteroid target = asteroidManager.findClosest(position, tag, SEGMENT_LENGTH);
        if (target != null) {
            // Let the asteroid know which tesla strike is attacking it.
            target.setTag(tag);
            this.endPoint = target.getPosition();
            weaponManager.requestLightningHit(this.endPoint, currentTime);
        } else
            this.endPoint = position.add(XVector.polar(SEGMENT_LENGTH, this.direction));
        this.treeDepth = treeDepth;
        this.segments = segments;
        this.frames = 0;
    }

    void move(ArrayList<XLightningSegment> created, long currentTime) {
        // Create more segments as necessary.
        if (this.frames == FRAME_MOVE_DELAY && this.endPoint.inBox(0, 0, this.size.getWidth(), this.size.getHeight())) {
            if (this.treeDepth < MIN_TREE_DEPTH || XRandom.sRandom() >
                    1.0 * (this.treeDepth - MIN_TREE_DEPTH) / (MAX_TREE_DEPTH - MIN_TREE_DEPTH))
                created.add(new XLightningSegment(
                        this.size,
                        this.endPoint,
                        this.direction,
                        this.asteroidManager,
                        this.tag,
                        this.weaponManager,
                        this.treeDepth + 1,
                        this.segments,
                        currentTime
                ));
            if (XRandom.sRandom() <= BRANCH_RATE) {
                created.add(new XLightningSegment(
                        this.size,
                        this.endPoint,
                        this.direction + (Double) XRandom.sChoice(-BRANCH_DEVIANCE, +BRANCH_DEVIANCE),
                        this.asteroidManager,
                        this.tag,
                        this.weaponManager,
                        this.treeDepth + 1,
                        this.segments,
                        currentTime
                ));
            }
        }
    }

    boolean draw(Graphics surface) {
        surface.setColor(XColor.interpolateRandom(COLOR_POSSIBILITIES));
        surface.drawLine(
                this.position.getIntX(),
                this.position.getIntY(),
                this.endPoint.getIntX(),
                this.endPoint.getIntY()
        );
        // Let the caller know if the lightning segment is done.
        return ++this.frames >= FRAME_LIFE_SPAN;
    }
}
