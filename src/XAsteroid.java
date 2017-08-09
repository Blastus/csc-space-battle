import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XAsteroid extends XCircle {
    static final int NEUTRAL_TAG = -1;
    private static final XColor SHADOW = new XColor(30, 30, 30);
    private static final XColor BRIGHT = new XColor(110, 110, 110);
    private static final int DESIRED_CRATER_COUNT = 5;
    private static final int PATIENCE = 50;
    private static final double MIN_CRATER_DIAMETER_RATIO = 0.75;
    private static final double MAX_CRATER_DIAMETER_RATIO = 1.25;
    private static final int CRATER_DIAMETER_DIVISOR = 5;
    private static final double CRATER_DISTANCE_SCALE = 0.75;
    private final Dimension canvasSize;
    private final XVector velocity;
    private final BufferedImage buffer;
    private int tag;

    XAsteroid(Dimension canvasSize, XCircle shape, XVector velocity) {
        super(shape);
        this.canvasSize = canvasSize;
        this.velocity = velocity;
        this.tag = 0;
        this.buffer = new BufferedImage(this.diameter, this.diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics surface = this.buffer.getGraphics();
        // Draw the background.
        IntStream.range(0, (this.diameter + 2) / 3).forEach(offset -> {
            int gradientDiameter = this.diameter - offset * 3;
            surface.setColor(BRIGHT.interpolate(1.0 * gradientDiameter / this.diameter, SHADOW));
            surface.fillOval(
                    this.diameter - gradientDiameter >> 2,
                    this.diameter - gradientDiameter >> 2,
                    gradientDiameter,
                    gradientDiameter
            );
        });
        // Make the craters.
        ArrayList<XCrater> craters = new ArrayList<>();
        XCircle craterDescription = new XCircle();
        XCircle referenceShape = new XCircle(this.radius);
        IntStream.range(0, DESIRED_CRATER_COUNT).forEach(a -> {
            // Try to position craters so they do not overlap and are inside the asteroid.
            IntStream.range(0, PATIENCE).filter(b -> {
                craterDescription.setRadius(
                        (int) (this.radius * XRandom.sUniform(
                                MIN_CRATER_DIAMETER_RATIO,
                                MAX_CRATER_DIAMETER_RATIO
                        ) / CRATER_DIAMETER_DIVISOR)
                );
                craterDescription.getPosition().setXY(
                        XRandom.sUniform(-this.radius, +this.radius),
                        XRandom.sUniform(-this.radius, +this.radius)
                );
                return referenceShape.containsWithMargin(craterDescription) &&
                        !craters.stream().anyMatch(crater -> craterDescription.overlaps(crater, CRATER_DISTANCE_SCALE));
            }).findFirst().ifPresent(b -> craters.add(new XCrater(craterDescription)));
        });
        // Draw the craters.
        craters.forEach(crater -> {
            crater.getPosition().iAdd(this.radius);
            crater.draw(surface);
        });
    }

    void move() {
        this.position.iAdd(this.velocity);
        this.position.clampXY(this.canvasSize);
    }

    void draw(Graphics surface) {
        XVector position = this.position.sub(this.radius);
        surface.drawImage(this.buffer, position.getIntX(), position.getIntY(), null);
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

    XVector getVelocity() {
        return this.velocity;
    }
}
