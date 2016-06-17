import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XCrater extends XCircle {
    private static final Color DARK_AREA = XColor.GRAY.value();
    private static final Color LIGHT_AREA = XColor.BLACK.interpolate(0.75, XColor.DARK_GRAY).value();
    private static final double NORTH_WEST_RESIZE = 1.0;
    private static final double SOUTH_EAST_RESIZE = 0.8;

    XCrater(XCircle description) {
        super(description.getPosition().copy(), description.getDiameter());
    }

    void draw(Graphics surface) {
        int x = position.getIntX();
        int y = position.getIntY();
        // Draw bottom of crater.
        surface.setColor(DARK_AREA);
        surface.fillOval(x - this.radius, y - this.radius, this.diameter, this.diameter);
        // Draw top of crater.
        surface.setColor(LIGHT_AREA);
        surface.fillOval(
                x - (int) (this.radius * NORTH_WEST_RESIZE),
                y - (int) (this.radius * NORTH_WEST_RESIZE),
                (int) (this.diameter * SOUTH_EAST_RESIZE),
                (int) (this.diameter * SOUTH_EAST_RESIZE));
    }
}
