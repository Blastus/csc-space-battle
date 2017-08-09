import java.awt.*;
import java.awt.geom.Rectangle2D;

/*
 * Created by Stephen "Zero" Chappell on 15 June 2016.
 */
class XTextWriter {
    private static final double FOREGROUND_TO_BACKGROUND_BIAS = 0.5;
    private static final int SHADOW_OFFSET = 2;
    private final Dimension canvasSize;
    private final Font typeface;
    private final XColor foreground;
    private final XColor background;

    XTextWriter(Dimension canvasSize, Font typeface, XColor foreground) {
        this(canvasSize, typeface, foreground, XColor.BLACK.interpolate(FOREGROUND_TO_BACKGROUND_BIAS, foreground));
    }

    XTextWriter(Dimension canvasSize, Font typeface, XColor foreground, XColor background) {
        this.canvasSize = canvasSize;
        this.typeface = typeface;
        this.foreground = foreground;
        this.background = background;
    }

    void write(Graphics surface, String text, XAnchor canvasAnchor, XAnchor stringAnchor, XVector offset) {
        double canvasWidth = this.canvasSize.getWidth();
        double canvasHeight = this.canvasSize.getHeight();
        XVector canvasAnchorPosition = this.getAnchorPosition(canvasAnchor, (int) canvasWidth, (int) canvasHeight);
        this.write(surface, text, canvasAnchorPosition.add(offset), stringAnchor);
    }

    void write(Graphics surface, String text, XVector startingPoint, XAnchor stringAnchor) {
        surface.setFont(this.typeface);
        FontMetrics metrics = surface.getFontMetrics();
        Rectangle2D stringBounds = metrics.getStringBounds(text, surface);
        double stringWidth = stringBounds.getWidth();
        double stringHeight = stringBounds.getHeight();
        XVector stringAnchorPosition = this.getAnchorPosition(stringAnchor, (int) stringWidth, (int) stringHeight);
        XVector finalPosition = startingPoint.sub(stringAnchorPosition);
        surface.setColor(this.background);
        surface.drawString(
                text,
                finalPosition.getIntX() + SHADOW_OFFSET,
                finalPosition.getIntY() + SHADOW_OFFSET
        );
        surface.setColor(this.foreground);
        surface.drawString(text, finalPosition.getIntX(), finalPosition.getIntY());
    }

    private XVector getAnchorPosition(XAnchor anchor, int width, int height) {
        switch (anchor) {
            case CENTER:
                return new XVector(width >> 1, height >> 1);
            case NORTH:
                return new XVector(width >> 1, 0);
            case NORTH_EAST:
                return new XVector(width, 0);
            case EAST:
                return new XVector(width, height >> 1);
            case SOUTH_EAST:
                return new XVector(width, height);
            case SOUTH:
                return new XVector(width >> 1, height);
            case SOUTH_WEST:
                return new XVector(0, height);
            case WEST:
                return new XVector(0, height >> 1);
            case NORTH_WEST:
                return new XVector(0, 0);
            default:
                throw new RuntimeException("anchor case was not handled");
        }
    }
}
