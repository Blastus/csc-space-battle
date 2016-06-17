import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XWeapon extends XCircle {
    private static final Color WEAPON_COLOR = XColor.RED.value();
    final XVector velocity;
    private final int lifeSpan;
    private final long bornTime;

    XWeapon(XVector position, long currentTime) {
        // A generic weapon will destroy an asteroid it overlaps, will not exist after moving, and will never be drawn.
        this(position, 0, currentTime);
    }

    XWeapon(XVector position, int diameter, long currentTime) {
        this(position, new XVector(), diameter, -1, currentTime);
    }

    XWeapon(XVector position, XVector velocity, int diameter, int lifeSpan, long currentTime) {
        super(position, diameter);
        this.velocity = velocity;
        this.lifeSpan = lifeSpan;
        this.bornTime = currentTime;
    }

    void move(Dimension size) {
        this.position.ipAdd(this.velocity);
        this.position.clampXY(size);
    }

    void draw(Graphics surface, long currentTime) {
        surface.setColor(WEAPON_COLOR);
        surface.fillOval(
                this.position.getIntX() - this.radius,
                this.position.getIntY() - this.radius,
                this.diameter,
                this.diameter);
    }

    boolean isAlive(long currentTime) {
        return currentTime - this.bornTime <= this.lifeSpan;
    }
}
