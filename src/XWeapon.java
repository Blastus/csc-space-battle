import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XWeapon extends XCircle {
    private static final XColor WEAPON_COLOR = XColor.RED;
    final XVector velocity;
    private final Dimension canvasSize;
    private final int lifeSpan;
    private final long bornTime;

    XWeapon(Dimension canvasSize, XVector position, long currentTime) {
        // A generic weapon will destroy an asteroid it overlaps, will not exist after moving, and will never be drawn.
        this(canvasSize, position, 0, currentTime);
    }

    XWeapon(Dimension canvasSize, XVector position, int radius, long currentTime) {
        this(canvasSize, position, new XVector(), radius, -1, currentTime);
    }

    XWeapon(Dimension canvasSize, XVector position, XVector velocity, int radius, int lifeSpan, long currentTime) {
        super(position, radius);
        this.canvasSize = canvasSize;
        this.velocity = velocity;
        this.lifeSpan = lifeSpan;
        this.bornTime = currentTime;
    }

    void move() {
        this.position.iAdd(this.velocity);
        this.position.clampXY(this.canvasSize);
    }

    void draw(Graphics surface, long currentTime) {
        surface.setColor(WEAPON_COLOR);
        surface.fillOval(
                this.position.getIntX() - this.radius,
                this.position.getIntY() - this.radius,
                this.diameter,
                this.diameter
        );
    }

    boolean isAlive(long currentTime) {
        return currentTime - this.bornTime <= this.lifeSpan;
    }
}
