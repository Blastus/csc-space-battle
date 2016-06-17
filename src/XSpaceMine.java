import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XSpaceMine extends XWeapon {
    static final int COOLING_TIME = 500;
    private static final int MINE_DIAMETER = 12;
    private static final int MINE_LIFE_SPAN = 100000;
    private static final int INDICATOR_TIME_SLICE = 500;
    private static final Color[] INDICATOR = {
            XColor.YELLOW.value(),
            XColor.BLACK.interpolate(0.5, XColor.GREEN).value()
    };

    XSpaceMine(XVector position, long currentTime) {
        super(position, new XVector(), MINE_DIAMETER, MINE_LIFE_SPAN, currentTime);
    }

    void draw(Graphics surface, long currentTime) {
        super.draw(surface, currentTime);
        int offset = (int) (currentTime / INDICATOR_TIME_SLICE % INDICATOR.length);
        surface.setColor(INDICATOR[offset]);
        int halfRadius = this.radius >> 1;
        surface.fillOval(
                this.position.getIntX() - halfRadius,
                this.position.getIntY() - halfRadius,
                this.radius,
                this.radius);
    }
}
