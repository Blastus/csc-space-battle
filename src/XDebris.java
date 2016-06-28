import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 10 June 2016.
 */
class XDebris extends XCircle {
    private static final double MIN_HEAT = 1.0 / 3;
    private static final double MAX_HEAT = 1.0;
    private static final int MIN_FADE_TIME = 2500;
    private static final int MAX_FADE_TIME = 7500;
    private static final XColor[] HEAT_PROGRESSION = {
            XColor.DARK_GRAY,
            XColor.RED,
            XColor.YELLOW,
            XColor.WHITE
    };
    private final Dimension size;
    private final XVector velocity;
    private final double heat;
    private final int fadeTime;
    private final long bornTime;

    XDebris(Dimension size, XVector position, XVector velocity, int diameter, long currentTime) {
        super(position, diameter);
        this.size = size;
        this.velocity = velocity;
        this.heat = XSpaceBattle.CHAOS.uniform(MIN_HEAT, MAX_HEAT);
        this.fadeTime = XSpaceBattle.CHAOS.randomInteger(MIN_FADE_TIME, MAX_FADE_TIME);
        this.bornTime = currentTime;
    }

    void move() {
        this.position.ipAdd(this.velocity);
    }

    boolean draw(Graphics surface, long currentTime) {
        if (!this.position.inBox(
                0 - this.radius,
                0 - this.radius,
                this.size.getWidth() + this.radius,
                this.size.getHeight() + this.radius))
            // Signal the caller to remove the debris.
            return true;
        double ratio = 1.0 * (currentTime - this.bornTime) / this.fadeTime;
        surface.setColor(XColor.interpolate(Math.max(this.heat * (1 - ratio), 0), HEAT_PROGRESSION).value());
        surface.fillOval(
                this.position.getIntX() - this.radius,
                this.position.getIntY() - this.radius,
                this.diameter,
                this.diameter);
        return false;
    }
}