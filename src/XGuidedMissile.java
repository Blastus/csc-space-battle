import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XGuidedMissile extends XWeapon {
    static final int COOLING_TIME = 1000;
    private static final int MISSILE_RADIUS = 4;
    private static final int MISSILE_LIFE_SPAN = 5000;
    private static final int MOTOR_PARTICLES = 10;
    private static final int MIN_MOTOR_PARTICLE_LENGTH = 10;
    private static final int MAX_MOTOR_PARTICLE_LENGTH = 15;
    private static final double MOTOR_PARTICLE_DEVIANCE = 0.3;
    private static final int IMPULSE_STRENGTH = 2;
    private final XAsteroid target;
    private final XThruster motor;

    XGuidedMissile(
            Dimension canvasSize,
            XVector position,
            XVector velocity,
            XAsteroidManager asteroidManager,
            long currentTime
    ) {
        super(canvasSize, position, velocity, MISSILE_RADIUS, MISSILE_LIFE_SPAN, currentTime);
        this.target = asteroidManager.findClosest(this.position);
        this.motor = new XThruster(
                this.position,
                MOTOR_PARTICLES,
                MIN_MOTOR_PARTICLE_LENGTH,
                MAX_MOTOR_PARTICLE_LENGTH,
                MOTOR_PARTICLE_DEVIANCE
        );
    }

    void move() {
        // Push missile in direction of target.
        XVector impulse = this.target.getPosition().sub(this.position);
        impulse.setMagnitude(IMPULSE_STRENGTH);
        this.velocity.iAdd(impulse);
        // Respect the missile's max speed.
        this.velocity.clampMagnitude(XWeaponManager.MAX_WEAPON_SPEED);
        super.move();
    }

    void draw(Graphics surface, long currentTime) {
        this.motor.draw(surface, this.velocity.getDirection() + XVector.CIRCLE_4_8);
        super.draw(surface, currentTime);
    }
}
