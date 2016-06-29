import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XHealthManager {
    private static final int STARTING_LIFE_COUNT = 3;
    private static final int INDICATOR_MARGIN = 5;
    private static final int POINTS_FOR_EXTRA_LIFE = 100;
    private static final Color SHIP_HIGHLIGHT = XPlayer.SHIP_HIGHLIGHT.interpolate(0.5, XColor.BLACK).value();
    private static final Color SHIP_COLOR = XPlayer.SHIP_COLOR.interpolate(0.5, XColor.BLACK).value();
    private final Dimension size;
    private final BufferedImage buffer;
    private int livesRemaining;
    private int totalPoints;

    XHealthManager(Dimension size, XPolygon shape) {
        this.size = size;
        this.livesRemaining = STARTING_LIFE_COUNT;
        this.totalPoints = 0;
        this.buffer = new BufferedImage(
                shape.getWidthInt() + (INDICATOR_MARGIN << 1),
                shape.getHeightInt() + (INDICATOR_MARGIN << 1),
                BufferedImage.TYPE_INT_ARGB);
        Graphics surface = this.buffer.getGraphics();
        shape.translate(new XVector(-shape.getMinX(), -shape.getMinY()).add(INDICATOR_MARGIN));
        shape.draw(surface, SHIP_HIGHLIGHT, SHIP_COLOR);
    }

    void addPoints(int points) {
        int oldEarnedLives = this.totalPoints / POINTS_FOR_EXTRA_LIFE;
        this.totalPoints += points;
        int newEarnedLives = this.totalPoints / POINTS_FOR_EXTRA_LIFE;
        this.livesRemaining += newEarnedLives - oldEarnedLives;
    }

    void draw(Graphics surface) {
        // TODO +X should be shown when this.livesRemaining exceeds some limit
        int sizeWidth = (int) this.size.getWidth();
        int sizeHeight = (int) this.size.getHeight();
        int bufferWidth = this.buffer.getWidth();
        int bufferHeight = this.buffer.getHeight();
        IntStream.range(1, this.livesRemaining + 1).forEach(offset -> surface.drawImage(
                this.buffer,
                sizeWidth - bufferWidth * offset,
                sizeHeight - bufferHeight,
                null));
    }

    int getScore() {
        return this.totalPoints;
    }

    boolean canRevive() {
        return this.livesRemaining > 0;
    }

    void commitRevive() {
        this.livesRemaining -= 1;
    }
}
