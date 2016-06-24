import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XColor {
    static final XColor BLACK = new XColor(0, 0, 0);
    static final XColor DARK_GRAY = new XColor(64, 64, 64);
    static final XColor GRAY = new XColor(128, 128, 128);
    static final XColor LIGHT_GRAY = new XColor(192, 192, 192);
    static final XColor WHITE = new XColor(255, 255, 255);
    static final XColor RED = new XColor(255, 0, 0);
    static final XColor GREEN = new XColor(0, 255, 0);
    static final XColor BLUE = new XColor(0, 0, 255);
    static final XColor CYAN = new XColor(0, 255, 255);
    static final XColor MAGENTA = new XColor(255, 0, 255);
    static final XColor YELLOW = new XColor(255, 255, 0);
    private final int red;
    private final int green;
    private final int blue;

    XColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    Color value() {
        return new Color(this.red, this.green, this.blue);
    }

    XColor interpolateRandom(XColor other) {
        return this.interpolate(XSpaceBattle.CHAOS.random(), other);
    }

    XColor interpolate(double bias, XColor other) {
        double alpha = 1 - bias;
        return new XColor(
                (int) (this.red * alpha + other.red * bias),
                (int) (this.green * alpha + other.green * bias),
                (int) (this.blue * alpha + other.blue * bias));
    }
}
