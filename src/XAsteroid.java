import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
// TODO extend XCircle and refactor to use its functionality
class XAsteroid {
    static final int NEUTRAL_TAG = -1;
    private static final XColor DARK_GRAY = new XColor(30, 30, 30);
    private static final XColor LIGHT_GRAY = new XColor(110, 110, 110);
    private static final int DESIRED_CRATER_COUNT = 5;
    private static final int PATIENCE = 50;
    private static final double MIN_CRATER_DIAMETER_RATIO = 0.75;
    private static final double MAX_CRATER_DIAMETER_RATIO = 1.25;
    private static final int CRATER_DIAMETER_DIVISOR = 5;
    private static final double CRATER_DISTANCE_SCALE = 0.75;
    private final XCircle shape;
    private final XVector velocity;
    private final BufferedImage buffer;
    private int tag;

    // TODO store Dimension size in the asteroid
    XAsteroid(XCircle shape, XVector velocity) {
        this.shape = shape;
        this.velocity = velocity;
        this.tag = 0;
        this.buffer = new BufferedImage(
                this.shape.getDiameter(),
                this.shape.getDiameter(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics surface = this.buffer.getGraphics();
        // Draw the background.
        // TODO redesign loop to use IntStream instead
        for (int size = this.shape.getDiameter(); size > 0; size -= 3) {
            surface.setColor(LIGHT_GRAY.interpolate(1.0 * size / this.shape.getDiameter(), DARK_GRAY).value());
            surface.fillOval(this.shape.getDiameter() - size >> 2, this.shape.getDiameter() - size >> 2, size, size);
        }
        // Make the craters.
        ArrayList<XCrater> craters = new ArrayList<>();
        XCircle craterDescription = new XCircle();
        XCircle referenceShape = new XCircle(this.shape.getDiameter());
        int diameter = this.shape.getDiameter();
        int radius = this.shape.getRadius();
        IntStream.range(0, DESIRED_CRATER_COUNT).forEach(a -> {
            // Try to position craters so they do not overlap and are inside the asteroid.
            try {
                IntStream.range(0, PATIENCE).forEach(b -> {
                    craterDescription.setDiameter((int) (diameter *
                            XSpaceBattle.CHAOS.uniform(MIN_CRATER_DIAMETER_RATIO, MAX_CRATER_DIAMETER_RATIO) /
                            CRATER_DIAMETER_DIVISOR));
                    craterDescription.getPosition().setXY(
                            XSpaceBattle.CHAOS.uniform(-radius, +radius),
                            XSpaceBattle.CHAOS.uniform(-radius, +radius));
                    if (referenceShape.contains(craterDescription, craterDescription.getRadius()) &&
                            !craters.stream().anyMatch(crater -> craterDescription.overlaps(
                                    crater,
                                    CRATER_DISTANCE_SCALE)))
                        // TODO this class may be removed if proper streaming techniques are used
                        throw new XFoundEvent();
                });
            } catch (XFoundEvent event) {
                craters.add(new XCrater(craterDescription));
            }
        });
        // Draw the craters.
        craters.forEach(crater -> {
            crater.getPosition().ipAdd(this.shape.getRadius());
            crater.draw(surface);
        });
    }

    void move(Dimension size) {
        XVector position = this.shape.getPosition();
        position.ipAdd(this.velocity);
        // TODO use this.size instead
        position.clampXY(size);
    }

    void draw(Graphics surface) {
        XVector position = this.shape.getPosition().sub(this.shape.getRadius());
        surface.drawImage(this.buffer, position.getIntX(), position.getIntY(), null);
    }

    XVector getPosition() {
        return this.shape.getPosition();
    }

    int getTag() {
        return this.tag;
    }

    void setTag(int tag) {
        if (tag == NEUTRAL_TAG)
            throw new RuntimeException("tag may not be neutral");
        this.tag = tag;
    }

    void copyTag(XAsteroid other) {
        this.tag = other.tag;
    }

    int getRadius() {
        return this.shape.getRadius();
    }

    XCircle getShape() {
        return this.shape;
    }

    int getDiameter() {
        return this.shape.getDiameter();
    }

    XVector getVelocity() {
        return this.velocity;
    }
}
