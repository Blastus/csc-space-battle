import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 13 June 2016.
 */
class XTimothyLight {
    private final XVector position;
    private final XVector velocity;
    private final int startDiameter;
    private final int stopDiameter;
    private final int lifeSpan;
    private final double directionChangePerFrame;
    private final XColor[] palette;
    private final long bornTime;

    XTimothyLight(
            XVector position,
            XVector velocity,
            int startDiameter,
            int stopDiameter,
            int lifeSpan,
            double directionChangePerFrame,
            XColor[] palette,
            long currentTime) {
        this.position = position;
        this.velocity = velocity;
        this.startDiameter = startDiameter;
        this.stopDiameter = stopDiameter;
        this.lifeSpan = lifeSpan;
        this.directionChangePerFrame = directionChangePerFrame;
        this.palette = palette;
        this.bornTime = currentTime;
    }

    void move() {
        this.position.ipAdd(this.velocity);
        this.velocity.addDirection(this.directionChangePerFrame);
    }

    boolean draw(Graphics surface, long currentTime) {
        double ratio = 1.0 * (currentTime - this.bornTime) / this.lifeSpan;
        if (ratio >= 1)
            // Let the caller know that the light is done.
            return true;
        surface.setColor(XColor.interpolate(ratio, this.palette).value());
        int diameter = this.startDiameter + (int) ((this.stopDiameter - this.startDiameter) * ratio);
        int radius = diameter >> 1;
        surface.fillOval(this.position.getIntX() - radius, this.position.getIntY() - radius, diameter, diameter);
        // Let the caller know that the light is not done.
        return false;
    }
}
