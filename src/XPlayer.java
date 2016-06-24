import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 2 June 2016.
 */
class XPlayer {
    static final int RADIUS = 14;
    private static final Color SHIP_HIGHLIGHT = Color.LIGHT_GRAY;
    private static final Color SHIP_COLOR = Color.BLUE;
    private static final double INITIAL_PLAYER_DIRECTION = XVector.CIRCLE_6_8;
    private static final double SLOW_MULTIPLIER = 0.95;
    private static final double BURN_IMPULSE = 0.25;
    private static final int SPEED_LIMIT = 20;
    private static final double DIR_IMPULSE = Math.PI / 900;
    private static final double DIR_LIMIT = Math.PI / 9;
    private static final int BRAKE_EXPANSION = 6;
    private static final XPolygon SHAPE = new XPolygon(
            XVector.polar(0.1, XVector.CIRCLE_2_8),
            XVector.polar(RADIUS, XVector.CIRCLE_3_8),
            XVector.polar(RADIUS * 10 / 7, XVector.CIRCLE_6_8),
            XVector.polar(RADIUS, XVector.CIRCLE_1_8)
    );
    private final Dimension size;
    private final XInput input;
    private final XVector position;
    private final XVector velocity;
    private double direction;
    private double dirSpeed;

    XPlayer(Dimension size, XInput input, XVector position) {
        this.size = size;
        this.input = input;
        this.position = position;
        this.velocity = new XVector();
        this.direction = INITIAL_PLAYER_DIRECTION;
        this.dirSpeed = 0;
    }

    void move() {
        if (this.input.requestsSlow()) {
            this.velocity.ipMul(SLOW_MULTIPLIER);
            this.dirSpeed *= SLOW_MULTIPLIER;
        }
        if (this.input.requestsBurn()) {
            this.velocity.ipAdd(XVector.polar(BURN_IMPULSE, XVector.CIRCLE_2_8 - this.direction));
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
        this.position.ipAdd(this.velocity);
        this.position.clampXY(this.size);
    }

    void draw(Graphics surface) {
        XPolygon shape = XPlayer.getShape(this.direction + XVector.CIRCLE_4_8);
        // Draw the brakes.
        if (this.input.requestsSlow()) {
            XPolygon brakes = shape.copy();
            brakes.expand(BRAKE_EXPANSION);
            brakes.translate(this.position);
            brakes.draw(surface, Color.MAGENTA);
        }
        shape.translate(this.position);
        shape.draw(surface, SHIP_HIGHLIGHT, SHIP_COLOR);
    }

    private static XPolygon getShape(double direction) {
        XPolygon shape = SHAPE.copy();
        shape.rotate(-direction);
        return shape;
    }

    XVector getPosition() {
        return this.position;
    }
}
