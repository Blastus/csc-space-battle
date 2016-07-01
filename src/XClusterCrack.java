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
    private static final double PELLET_DEVIANCE = XVector.CIRCLE_1_8 / 9;

    private XClusterCrack(Dimension size, XVector position, XVector velocity, long currentTime) {
        super(size, position, velocity, PELLET_RADIUS, PELLET_LIFE_SPAN, currentTime);
    }

    static ArrayList<XClusterCrack> fire(Dimension size, XPlayer player, long currentTime) {
        ArrayList<XClusterCrack> pellets = new ArrayList<>();
        IntStream.range(0, PELLET_BURST_COUNT).forEach(a -> {
            double deviance = XRandom.sUniform(-PELLET_DEVIANCE, +PELLET_DEVIANCE);
            XVector impulse = XVector.polar(
                    XWeaponManager.MAX_WEAPON_SPEED + XRandom.sUniform(-1, +1),
                    player.getDirection() + deviance
            );
            XVector velocity = player.getVelocity().add(impulse);
            pellets.add(new XClusterCrack(size, player.getPosition().copy(), velocity, currentTime));
        });
        return pellets;
    }
}
