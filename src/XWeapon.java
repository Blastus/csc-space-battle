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
        super(position, PROJECTILE_DIAMETER);
        this.velocity = velocity;
        this.bornTime = currentTime;
    }

    void move(Dimension size) {
        this.position.ipAdd(this.velocity);
        this.position.clampXY(size);
    }

    void draw(Graphics surface) {
        surface.setColor(WEAPON_COLOR);
        surface.fillOval(
                this.position.getIntX() - this.radius,
                this.position.getIntY() - this.radius,
                this.diameter,
                this.diameter
        );
    }

    boolean isAlive(long currentTime) {
        return currentTime - this.bornTime <= PROJECTILE_LIFE_SPAN;
    }
}
