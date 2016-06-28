import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 8 June 2016.
 */
class XClusterCrack extends XWeapon {
    static final int COOLING_TIME = 850;
    private static final int PELLET_DIAMETER = 3;
    private static final int PELLET_LIFE_SPAN = 700;
    private static final int PELLET_BURST_COUNT = 15;
    private static final double PELLET_DEVIANCE = XVector.CIRCLE_1_8 / 9;

    private XClusterCrack(XVector position, XVector velocity, long currentTime) {
        super(position, velocity, PELLET_DIAMETER, PELLET_LIFE_SPAN, currentTime);
    }

    static ArrayList<XClusterCrack> fire(XPlayer player, long currentTime) {
        ArrayList<XClusterCrack> pellets = new ArrayList<>();
        IntStream.range(0, PELLET_BURST_COUNT).forEach(a -> {
            double offset = XSpaceBattle.CHAOS.uniform(-PELLET_DEVIANCE, +PELLET_DEVIANCE);
            XVector impulse = XVector.polar(
                    XWeaponManager.MAX_WEAPON_SPEED + XSpaceBattle.CHAOS.uniform(-1, +1),
                    XVector.CIRCLE_2_8 + offset - player.getDirection());
            XVector velocity = player.getVelocity().add(impulse);
            pellets.add(new XClusterCrack(player.getPosition().copy(), velocity, currentTime));
        });
        return pellets;
    }
}
