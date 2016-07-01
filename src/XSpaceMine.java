import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XSpaceMine extends XWeapon {
    static final int COOLING_TIME = 500;
    private static final int MINE_RADIUS = 6;
    private static final int MINE_LIFE_SPAN = 100000;
    private static final int INDICATOR_TIME_SLICE = 500;
    private static final XColor[] INDICATOR = {
            XColor.YELLOW,
            XColor.BLACK.interpolate(0.5, XColor.GREEN)
    };

    XSpaceMine(Dimension size, XVector position, long currentTime) {
        super(size, position, new XVector(), MINE_RADIUS, MINE_LIFE_SPAN, currentTime);
    }

    void draw(Graphics surface, long currentTime) {
        super.draw(surface, currentTime);
        int offset = (int) (currentTime / INDICATOR_TIME_SLICE % INDICATOR.length);
        surface.setColor(INDICATOR[offset]);
        int indicatorDiameter = this.radius;
        int indicatorRadius = indicatorDiameter >> 1;
        surface.fillOval(
                this.position.getIntX() - indicatorRadius,
                this.position.getIntY() - indicatorRadius,
                indicatorDiameter,
                indicatorDiameter
        );
    }
}
