import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XHealthManager {
    private static int STARTING_LIFE_COUNT;
    private static int INDICATOR_MARGIN;
    private static int POINTS_FOR_EXTRA_LIFE;
    private static Color SHIP_HIGHLIGHT;
    private static Color SHIP_COLOR;
    private int livesRemaining;
    private int totalPoints;

    XHealthManager(Dimension size, XPolygon shape) {
    }

    void addPoints(int points) {
    }

    void draw(Graphics surface) {
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
