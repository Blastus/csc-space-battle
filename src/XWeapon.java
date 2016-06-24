import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XWeapon extends XCircle {
    static final int COOLING_TIME = 50;
    private static final int PROJECTILE_DIAMETER = 5;
    private static final int PROJECTILE_LIFE_SPAN = 500;
    private static final Color WEAPON_COLOR = XColor.RED.value();
    private XVector velocity;
    private long bornTime;

    XWeapon(XVector position, XVector velocity, long currentTime) {
    }

    void move(Dimension size) {
    }

    void draw(Graphics surface) {
    }

    boolean isAlive(long currentTime) {
        return false;
    }
}
