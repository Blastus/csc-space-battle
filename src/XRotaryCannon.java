/*
 * Created by Stephen "Zero" Chappell on 7 June 2016.
 */
class XRotaryCannon extends XWeapon {
    static final int COOLING_TIME = 50;
    private static final int PROJECTILE_DIAMETER = 5;
    private static final int PROJECTILE_LIFE_SPAN = 500;

    XRotaryCannon(XVector position, XVector velocity, long currentTime) {
        super(position, velocity, PROJECTILE_DIAMETER, PROJECTILE_LIFE_SPAN, currentTime);
    }
}
