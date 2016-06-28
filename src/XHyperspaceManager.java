import java.awt.*;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 9 June 2016.
 */
class XHyperspaceManager {
    static int LIVING_LIFE_SPAN;
    private static long HYPERSPACE_RESET;
    private static int PATIENCE = 50;
    private static int SAFETY_FACTOR;
    private static int LIVING_EFFECT_SIZE;
    private static int LIVING_PARTICLE_SIZE;
    private static int LIVING_PARTICLE_COUNT;
    private static int DEAD_EFFECT_SIZE;
    private static int DEAD_PARTICLE_SIZE;
    private static int DEAD_PARTICLE_COUNT;
    private static int DEAD_LIFE_SPAN;
    private final Dimension size;
    private final XInput input;
    private final XPlayer player;
    private final XAsteroidManager asteroidManager;
    private final XSpecialEffects specialEffects;
    private final XWeaponManager weaponManager;
    private long hyperspaceTimer;

    XHyperspaceManager(
            Dimension size,
            XInput input,
            XPlayer player,
            XAsteroidManager asteroidManager,
            XSpecialEffects specialEffects,
            XWeaponManager weaponManager,
            long currentTime) {
        this.size = size;
        this.input = input;
        this.player = player;
        this.asteroidManager = asteroidManager;
        this.specialEffects = specialEffects;
        this.weaponManager = weaponManager;
        this.player.setHyperspaceManager(this);
        this.resetTimer(currentTime);
    }

    void resetTimer(long currentTime) {
    }

    boolean available(long currentTime) {
        return true;
    }

    void handlePanicRequest(long currentTime) {
    }

    boolean initiateHyperspaceJump(long currentTime) {
        XVector position = new XVector();
        try {
            IntStream.range(0, PATIENCE).forEach(a -> {
                position.random(this.size);
                if (!this.asteroidManager.getAsteroids().stream().anyMatch(asteroid -> position.sub(
                        asteroid.getPosition()).getMagnitude() / SAFETY_FACTOR <
                        asteroid.getRadius() + XPlayer.RADIUS))
                    throw new XFoundEvent();
            });
            // A safe position was not found.
            this.weaponManager.initiateHyperspaceStorm(currentTime);
            return false;
        } catch (XFoundEvent event) {
            // Jump to the safe position.
            boolean playerIsAlive = this.player.isAlive();
            XVector playerPosition = player.getPosition();
            if (playerIsAlive)
                // Spawn a special effect at the starting point.
                this.specialEffects.spawn(
                        playerPosition.copy(),
                        LIVING_EFFECT_SIZE,
                        LIVING_PARTICLE_SIZE,
                        LIVING_PARTICLE_COUNT,
                        LIVING_LIFE_SPAN,
                        XSpecialEffects.COOL,
                        currentTime);
            playerPosition.copy(position);
            if (playerIsAlive)
                // Spawn a special effect at the ending point.
                this.specialEffects.spawn(
                        position,
                        LIVING_EFFECT_SIZE,
                        LIVING_PARTICLE_SIZE,
                        LIVING_PARTICLE_COUNT,
                        LIVING_LIFE_SPAN,
                        XSpecialEffects.COOL,
                        currentTime);
            else
                // Spawn a special effect for the initial warp into the asteroid field.
                this.specialEffects.spawn(
                        position,
                        DEAD_EFFECT_SIZE,
                        DEAD_PARTICLE_SIZE,
                        DEAD_PARTICLE_COUNT,
                        DEAD_LIFE_SPAN,
                        XSpecialEffects.COOL,
                        currentTime);
            return true;
        }
    }
}
