import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XHealthManager {
    private static final int STARTING_LIFE_COUNT = 3;
    private static final int INDICATOR_MARGIN = 5;
    private static final int POINTS_FOR_EXTRA_LIFE = 100;
    private static final XColor SHIP_HIGHLIGHT = XPlayer.SHIP_HIGHLIGHT.interpolate(0.5, XColor.BLACK);
    private static final XColor SHIP_COLOR = XPlayer.SHIP_COLOR.interpolate(0.5, XColor.BLACK);
    private static final XColor TEXT_COLOR = XColor.WHITE;
    private static final int MAX_LIFE_DISPLAY = 7;
    private final Dimension size;
    private final BufferedImage buffer;
    private final XTextWriter lifeWriter;
    private int livesRemaining;
    private int totalPoints;

    XHealthManager(Dimension size, XPolygon shape, Font typeface) {
        this.size = size;
        this.livesRemaining = STARTING_LIFE_COUNT;
        this.totalPoints = 0;
        this.buffer = new BufferedImage(
                shape.getWidthInt() + (INDICATOR_MARGIN << 1),
                shape.getHeightInt() + (INDICATOR_MARGIN << 1),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics surface = this.buffer.getGraphics();
        shape.translate(new XVector(-shape.getMinX(), -shape.getMinY()).add(INDICATOR_MARGIN));
        shape.draw(surface, SHIP_HIGHLIGHT, SHIP_COLOR);
        this.lifeWriter = new XTextWriter(size, typeface, TEXT_COLOR);
    }

    void addPoints(int points) {
        int oldEarnedLives = this.totalPoints / POINTS_FOR_EXTRA_LIFE;
        this.totalPoints += points;
        int newEarnedLives = this.totalPoints / POINTS_FOR_EXTRA_LIFE;
        this.livesRemaining += newEarnedLives - oldEarnedLives;
    }

    void draw(Graphics surface) {
        int sizeWidth = (int) this.size.getWidth();
        int sizeHeight = (int) this.size.getHeight();
        int bufferWidth = this.buffer.getWidth();
        int bufferHeight = this.buffer.getHeight();
        int livesToShow = Math.min(this.livesRemaining, MAX_LIFE_DISPLAY);
        IntStream.range(1, livesToShow + 1).forEach(offset -> surface.drawImage(
                this.buffer,
                sizeWidth - bufferWidth * offset,
                sizeHeight - bufferHeight,
                null
        ));
        int livesNotShown = this.livesRemaining - livesToShow;
        if (livesNotShown > 0)
            this.lifeWriter.write(
                    surface,
                    MessageFormat.format("+{0}", livesNotShown),
                    new XVector(sizeWidth - bufferWidth * MAX_LIFE_DISPLAY, sizeHeight),
                    XAnchor.EAST
            );
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
