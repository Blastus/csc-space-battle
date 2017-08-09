import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 7 June 2016.
 */
class XRotaryCannon extends XWeapon {
    static final int COOLING_TIME = 50;
    private static final int PROJECTILE_RADIUS = 2;
    private static final int PROJECTILE_LIFE_SPAN = 500;

    XRotaryCannon(Dimension canvasSize, XVector position, XVector velocity, long currentTime) {
        super(canvasSize, position, velocity, PROJECTILE_RADIUS, PROJECTILE_LIFE_SPAN, currentTime);
    }
}
