import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XAsteroid {
    private static final int DESIRED_CRATER_COUNT = 5;
    private static final int PATIENCE = 50;
    private static final double MIN_CRATER_DIAMETER_RATIO = 0.75;
    private static final double MAX_CRATER_DIAMETER_RATIO = 1.25;
    private static final int CRATER_DIAMETER_DIVISOR = 5;
    private static final double CRATER_DISTANCE_SCALE = 0.75;
    private final XCircle shape;
    private final XVector velocity;
    private final BufferedImage buffer;

    XAsteroid(XCircle shape, XVector velocity) {
        this.shape = shape;
        this.velocity = velocity;
        this.buffer = new BufferedImage(
                this.shape.getDiameter(),
                this.shape.getDiameter(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics surface = this.buffer.getGraphics();
        // Draw the background.
        for (int size = this.shape.getDiameter(); size > 0; size -= 3) {
            double ratio = 1.0 * size / this.shape.getDiameter();
            int value = (int) (110 * (1 - ratio) + 30 * ratio);
            surface.setColor(new Color(value, value, value));
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
            for (int count = 0; count < PATIENCE; count++) {
                craterDescription.setDiameter((int) (diameter * XSpaceBattle.CHAOS.uniform(
                        MIN_CRATER_DIAMETER_RATIO,
                        MAX_CRATER_DIAMETER_RATIO) / CRATER_DIAMETER_DIVISOR));
                craterDescription.getPosition().setXY(
                        XSpaceBattle.CHAOS.uniform(-radius, +radius),
                        XSpaceBattle.CHAOS.uniform(-radius, +radius));
                if (referenceShape.contains(craterDescription, craterDescription.getRadius()) && !craters.stream()
                        .anyMatch(crater -> craterDescription.overlaps(crater, CRATER_DISTANCE_SCALE))) {
                    craters.add(new XCrater(craterDescription));
                    break;
                }
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
        position.clampXY(size);
    }

    void draw(Graphics surface) {
        XVector position = this.shape.getPosition().sub(this.shape.getRadius());
        surface.drawImage(this.buffer, position.getIntX(), position.getIntY(), null);
    }
}
