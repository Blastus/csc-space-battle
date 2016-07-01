import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XColor extends Color {
    static final XColor BLACK = new XColor(Color.BLACK);
    static final XColor BLUE = new XColor(Color.BLUE);
    static final XColor CYAN = new XColor(Color.CYAN);
    static final XColor DARK_GRAY = new XColor(Color.DARK_GRAY);
    static final XColor GRAY = new XColor(Color.GRAY);
    static final XColor GREEN = new XColor(Color.GREEN);
    static final XColor LIGHT_GRAY = new XColor(Color.LIGHT_GRAY);
    static final XColor MAGENTA = new XColor(Color.MAGENTA);
    static final XColor ORANGE = new XColor(Color.ORANGE);
    static final XColor PINK = new XColor(Color.PINK);
    static final XColor RED = new XColor(Color.RED);
    static final XColor WHITE = new XColor(Color.WHITE);
    static final XColor YELLOW = new XColor(Color.YELLOW);

    XColor(Color other) {
        super(other.getRGB());
    }

    XColor(int red, int green, int blue) {
        super(red, green, blue);
    }

    XColor(double red, double green, double blue) {
        super((int) (red * 255 + 0.5), (int) (green * 255 + 0.5), (int) (blue * 255 + 0.5));
    }

    public static XColor decode(String nm) {
        return new XColor(Color.decode(nm));
    }

    public static XColor getColor(String nm) {
        return new XColor(Color.getColor(nm));
    }

    public static XColor getColor(String nm, Color v) {
        return new XColor(Color.getColor(nm, v));
    }

    public static XColor getColor(String nm, int v) {
        return new XColor(Color.getColor(nm, v));
    }

    public static XColor getHSBColor(double h, double s, double b) {
        return new XColor(Color.getHSBColor((float) h, (float) s, (float) b));
    }

    static XColor interpolate(double z, XColor... colors) {
        if (z <= 0)
            return colors[0];
        if (z >= 1)
            return colors[colors.length - 1];
        double share = 1.0 / (colors.length - 1);
        int index = (int) (z / share);
        return colors[index].interpolate(z % share / share, colors[index + 1]);
    }

    static XColor random() {
        return new XColor(XRandom.sRandom(), XRandom.sRandom(), XRandom.sRandom());
    }

    static XColor interpolateRandom(XColor... colors) {
        return XColor.interpolate(XRandom.sRandom(), colors);
    }

    public XColor brighter() {
        return new XColor(super.brighter());
    }

    public XColor darker() {
        return new XColor(super.darker());
    }

    XColor interpolate(double z, Color other) {
        int thisRGB = this.getRGB();
        int otherRGB = other.getRGB();
        return new XColor(
                XRandom.interpolate(thisRGB >> 16 & 0xFF, otherRGB >> 16 & 0xFF, z),
                XRandom.interpolate(thisRGB >> 8 & 0xFF, otherRGB >> 8 & 0xFF, z),
                XRandom.interpolate(thisRGB & 0xFF, otherRGB & 0xFF, z)
        );
    }

    XColor interpolateRandom(Color other) {
        return this.interpolate(XRandom.sRandom(), other);
    }
}