import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 2 June 2016.
 */
class XPlayer {
    private static final int RADIUS = 14;
    private static final Color SHIP_HIGHLIGHT = Color.LIGHT_GRAY;
    private static final Color SHIP_COLOR = Color.BLUE;
    private static final double INITIAL_PLAYER_DIRECTION = XVector.CIRCLE_6_8;
    private static final double SLOW_MULTIPLIER = 0.95;
    private static final double BURN_IMPULSE = 0.25;
    private static final int SPEED_LIMIT = 20;
    private static final double DIR_IMPULSE = Math.PI / 900;
    private static final double DIR_LIMIT = Math.PI / 9;
    private static final XPolygon SHAPE = new XPolygon(
            XVector.polar(0.1, XVector.CIRCLE_2_8),
            XVector.polar(RADIUS, XVector.CIRCLE_3_8),
            XVector.polar(RADIUS * 10 / 7, XVector.CIRCLE_6_8),
            XVector.polar(RADIUS, XVector.CIRCLE_1_8)
    );
    private Dimension size;
    private XInput input;
    private XVector position;
    private XVector velocity;
    private double direction;
    private double dirSpeed;

    XPlayer(Dimension size, XInput input, XVector position) {
    }

    void move() {
    }

    void draw(Graphics surface) {
    }

    private static XPolygon getShape(double direction) {
        return SHAPE;
    }
}
