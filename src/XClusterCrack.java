import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XClusterCrack extends XWeapon {
    static final int COOLING_TIME = 850;
    private static final int PELLET_RADIUS = 2;
    private static final int PELLET_LIFE_SPAN = 700;
    private static final int PELLET_BURST_COUNT = 15;
    private static final int PELLET_SPEED_DEVIANCE = 1;
    private static final double PELLET_DIRECTION_DEVIANCE = XVector.CIRCLE_1_8 / 9;

    private XClusterCrack(Dimension canvasSize, XVector position, XVector velocity, long currentTime) {
        super(canvasSize, position, velocity, PELLET_RADIUS, PELLET_LIFE_SPAN, currentTime);
    }

    static ArrayList<XClusterCrack> fire(Dimension canvasSize, XPlayer player, long currentTime) {
        ArrayList<XClusterCrack> pellets = new ArrayList<>();
        IntStream.range(0, PELLET_BURST_COUNT).forEach(a -> {
            double speedDeviance = XRandom.sUniform(-PELLET_SPEED_DEVIANCE, +PELLET_SPEED_DEVIANCE);
            double directionDeviance = XRandom.sUniform(-PELLET_DIRECTION_DEVIANCE, +PELLET_DIRECTION_DEVIANCE);
            XVector impulse = XVector.polar(
                    XWeaponManager.MAX_WEAPON_SPEED + speedDeviance,
                    player.getDirection() + directionDeviance
            );
            XVector velocity = player.getVelocity().add(impulse);
            pellets.add(new XClusterCrack(canvasSize, player.getPosition().copy(), velocity, currentTime));
        });
        return pellets;
    }
}
