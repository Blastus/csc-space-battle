import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 2 June 2016.
 */
class XPlayer extends XCircle {
    static final int DEATH_LIFE_SPAN = 2000;
    static final XColor SHIP_HIGHLIGHT = XColor.LIGHT_GRAY;
    static final XColor SHIP_COLOR = XColor.BLUE;
    static final double INITIAL_PLAYER_DIRECTION = XVector.CIRCLE_6_8;
    private static final int RADIUS = 14;
    private static final double SLOW_MULTIPLIER = 0.95;
    private static final double BURN_IMPULSE = 0.25;
    private static final int SPEED_LIMIT = 20;
    private static final double DIR_IMPULSE = Math.PI / 900;
    private static final double DIR_LIMIT = Math.PI / 9;
    private static final int MOTOR_PARTICLES = 10;
    private static final int MIN_MOTOR_PARTICLE_LENGTH = 10;
    private static final int MAX_MOTOR_PARTICLE_LENGTH = 35;
    private static final double MOTOR_PARTICLE_DEVIANCE = Math.PI / 10;
    private static final int BRAKE_EXPANSION = 6;
    private static final int REACTOR_PARTICLES = 3;
    private static final int MIN_REACTOR_PARTICLE_LENGTH = 2;
    private static final int MAX_REACTOR_PARTICLE_LENGTH = 5;
    private static final double REACTOR_PARTICLE_DEVIANCE = 0.5;
    private static final int DEATH_EFFECT_SIZE = 100;
    private static final int DEATH_PARTICLE_SIZE = 40;
    private static final int DEATH_PARTICLE_COUNT = 40;
    private static final XPolygon SHAPE = new XPolygon(
            XVector.polar(RADIUS * 10 / 7, XVector.CIRCLE_0_8),
            XVector.polar(RADIUS, XVector.CIRCLE_3_8),
            XVector.polar(0.1, XVector.CIRCLE_4_8),
            XVector.polar(RADIUS, XVector.CIRCLE_5_8)
    );
    private final Dimension size;
    private final XInput input;
    private final XThruster motor;
    private final XSpecialEffects specialEffects;
    private final Runnable handleDeath;
    private XVector velocity;
    private double direction;
    private double dirSpeed;
    private boolean alive;
    private XHyperspaceManager hyperspaceManager;

    XPlayer(Dimension size, XInput input, XVector position, XSpecialEffects specialEffects, Runnable handleDeath) {
        super(position, RADIUS);
        this.size = size;
        this.input = input;
        this.motor = new XThruster(
                this.position,
                MOTOR_PARTICLES,
                MIN_MOTOR_PARTICLE_LENGTH,
                MAX_MOTOR_PARTICLE_LENGTH,
                MOTOR_PARTICLE_DEVIANCE
        );
        this.specialEffects = specialEffects;
        this.handleDeath = handleDeath;
        this.hyperspaceManager = null;
        this.revive(0);
    }

    static XPolygon getShape(double direction) {
        XPolygon shape = SHAPE.copy();
        shape.rotate(direction);
        return shape;
    }

    void move(long currentTime) {
        if (this.alive) {
            if (this.input.requestsSlow()) {
                this.velocity.iMul(SLOW_MULTIPLIER);
                this.dirSpeed *= SLOW_MULTIPLIER;
            }
            if (this.hyperspaceManager.available(currentTime) && this.input.requestsBurn()) {
                this.velocity.iAdd(XVector.polar(BURN_IMPULSE, this.direction));
                // Obey the speed limit.
                this.velocity.clampMagnitude(SPEED_LIMIT);
            }
            // Handle rotations.
            if (this.input.requestsLeft())
                this.dirSpeed -= DIR_IMPULSE;
            if (this.input.requestsRight())
                this.dirSpeed += DIR_IMPULSE;
            this.dirSpeed = Math.min(Math.max(this.dirSpeed, -DIR_LIMIT), +DIR_LIMIT);
            // Update physics state.
            this.direction += this.dirSpeed;
            this.position.iAdd(this.velocity);
            this.position.clampXY(this.size);
        }
    }

    void draw(Graphics surface, long currentTime) {
        if (this.alive) {
            boolean enabled = this.hyperspaceManager.available(currentTime);
            if (enabled && this.input.requestsBurn())
                this.motor.draw(surface, this.direction + XVector.CIRCLE_4_8);
            XPolygon shape = XPlayer.getShape(this.direction);
            // Draw the brakes.
            if (this.input.requestsSlow()) {
                XPolygon brakes = shape.copy();
                brakes.expand(BRAKE_EXPANSION);
                brakes.translate(this.position);
                brakes.draw(surface, XColor.BLACK.interpolateRandom(XColor.MAGENTA));
            }
            shape.translate(this.position);
            // Draw the reaction control system thrusters.
            boolean left = this.input.requestsLeft();
            boolean right = this.input.requestsRight();
            if (left || right) {
                XVector position = new XVector();
                XThruster reactor = new XThruster(
                        position,
                        REACTOR_PARTICLES,
                        MIN_REACTOR_PARTICLE_LENGTH,
                        MAX_REACTOR_PARTICLE_LENGTH,
                        REACTOR_PARTICLE_DEVIANCE
                );
                // Draw the nose thrusters.
                position.copy(shape.getPoint(0));
                if (left)
                    reactor.draw(surface, this.direction + XVector.CIRCLE_2_8);
                if (right)
                    reactor.draw(surface, this.direction - XVector.CIRCLE_2_8);
                // Draw the corner thrusters.
                if (left) {
                    position.copy(shape.getPoint(3));
                    reactor.draw(surface, this.direction - XVector.CIRCLE_2_8);
                }
                if (right) {
                    position.copy(shape.getPoint(1));
                    reactor.draw(surface, this.direction + XVector.CIRCLE_2_8);
                }
            }
            shape.draw(surface, (enabled ? SHIP_HIGHLIGHT : XColor.random()), (enabled ? SHIP_COLOR : XColor.random()));
        }
    }

    XVector getNosePosition() {
        XVector nose = SHAPE.getPoint(0).copy();
        nose.addDirection(this.direction);
        nose.iAdd(this.position);
        return nose;
    }

    double getDirection() {
        return this.direction;
    }

    XVector getVelocity() {
        return this.velocity;
    }

    boolean isAlive() {
        return this.alive;
    }

    void setHyperspaceManager(XHyperspaceManager hyperspaceManager) {
        this.hyperspaceManager = hyperspaceManager;
    }

    void kill(long currentTime) {
        this.alive = false;
        this.specialEffects.spawn(
                this.position,
                DEATH_EFFECT_SIZE,
                DEATH_PARTICLE_SIZE,
                DEATH_PARTICLE_COUNT,
                DEATH_LIFE_SPAN,
                XSpecialEffects.WARM,
                currentTime
        );
        this.handleDeath.run();
    }

    void revive(long currentTime) {
        this.velocity = new XVector();
        this.direction = INITIAL_PLAYER_DIRECTION;
        this.dirSpeed = 0;
        this.alive = true;
        if (this.hyperspaceManager != null)
            this.hyperspaceManager.resetTimer(currentTime);
    }
}
