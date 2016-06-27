import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XGuidedMissile extends XWeapon {
    static final int COOLING_TIME = 1000;
    private static final int MISSILE_DIAMETER = 8;
    private static final int MISSILE_LIFE_SPAN = 5000;
    private static final int MOTOR_PARTICLES = 10;
    private static final int MIN_MOTOR_PARTICLE_LENGTH = 10;
    private static final int MAX_MOTOR_PARTICLE_LENGTH = 15;
    private static final double MOTOR_PARTICLE_DEVIANCE = 0.3;
    private final XAsteroid target;
    private final XThruster motor;

    XGuidedMissile(XVector position, XVector velocity, XAsteroidManager asteroidManager, long currentTime) {
        super(position, velocity, MISSILE_DIAMETER, MISSILE_LIFE_SPAN, currentTime);
        this.target = asteroidManager.findClosest(this.position);
        this.motor = new XThruster(
                this.position,
                MOTOR_PARTICLES,
                MIN_MOTOR_PARTICLE_LENGTH,
                MAX_MOTOR_PARTICLE_LENGTH,
                MOTOR_PARTICLE_DEVIANCE);
    }

    void move(Dimension size) {
        // Push missile in direction of target.
        XVector vectorToTarget = this.target.getPosition().sub(this.position);
        vectorToTarget.setMagnitude(2);
        this.velocity.ipAdd(vectorToTarget);
        // Respect the missile's max speed.
        this.velocity.clampMagnitude(XWeaponManager.MAX_WEAPON_SPEED);
        super.move(size);
    }

    void draw(Graphics surface, long currentTime) {
        this.motor.draw(surface, XVector.CIRCLE_6_8 - this.velocity.getDirection());
        super.draw(surface, currentTime);
    }
}
