import java.awt.*;
import java.awt.geom.Rectangle2D;

/*
 * Created by Stephen "Zero" Chappell on 15 June 2016.
 */
class XTextWriter {
    private static final double FOREGROUND_TO_BACKGROUND_BIAS = 0.5;
    private static final int SHADOW_OFFSET = 2;
    private final Dimension size;
    private final Font typeface;
    private final Color foreground;
    private final Color background;

    XTextWriter(Dimension size, Font typeface, XColor foreground) {
        this.size = size;
        this.typeface = typeface;
        this.foreground = foreground.value();
        this.background = XColor.BLACK.interpolate(FOREGROUND_TO_BACKGROUND_BIAS, foreground).value();
    }

    void write(Graphics surface, String text, String canvasAnchor, String stringAnchor, XVector offset) {
        double canvasWidth = this.size.getWidth();
        double canvasHeight = this.size.getHeight();
        XVector canvasAnchorPosition = this.getAnchorPosition(canvasAnchor, (int) canvasWidth, (int) canvasHeight);
        surface.setFont(this.typeface);
        FontMetrics metrics = surface.getFontMetrics();
        Rectangle2D stringBounds = metrics.getStringBounds(text, surface);
        double stringWidth = stringBounds.getWidth();
        double stringHeight = stringBounds.getHeight();
        XVector stringAnchorPosition = this.getAnchorPosition(stringAnchor, (int) stringWidth, (int) stringHeight);
        XVector finalPosition = canvasAnchorPosition.sub(stringAnchorPosition).add(offset);
        surface.setColor(this.background);
        surface.drawString(text, finalPosition.getIntX() + SHADOW_OFFSET, finalPosition.getIntY() + SHADOW_OFFSET);
        surface.setColor(this.foreground);
        surface.drawString(text, finalPosition.getIntX(), finalPosition.getIntY());
    }

    private XVector getAnchorPosition(String anchor, int width, int height) {
        switch (anchor) {
            case "CENTER":
                return new XVector(width >> 1, height >> 1);
            case "NORTH":
                return new XVector(width >> 1, 0);
            case "NORTH_EAST":
                return new XVector(width, 0);
            case "EAST":
                return new XVector(width, height >> 1);
            case "SOUTH_EAST":
                return new XVector(width, height);
            case "SOUTH":
                return new XVector(width >> 1, height);
            case "SOUTH_WEST":
                return new XVector(0, height);
            case "WEST":
                return new XVector(0, height >> 1);
            case "NORTH_WEST":
                return new XVector();
            default:
                throw new RuntimeException("anchor case was not handled");
        }
    }
}
