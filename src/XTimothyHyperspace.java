import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 13 June 2016.
 */
class XTimothyHyperspace {
    private static final XColor[] PALETTE = {
            XColor.WHITE,
            XColor.BLUE
    };
    private static final int EMITTER_COUNT = 2;
    private static final int SPEED = 6;
    private static final int START_DIAMETER = 10;
    private static final int STOP_DIAMETER = 0;
    private static final int LIFE_SPAN = 400;
    private static final double DIRECTION_CHANGE_PER_FRAME = XVector.CIRCLE_2_8 / 5;
    private final XVector position;
    private final XVector velocity;
    private final int emitterLifeSpan;
    private final ArrayList<XTimothyLight> lights;
    private final long bornTime;

    XTimothyHyperspace(XVector position, int emitterLifeSpan, long currentTime) {
        this(position, new XVector(), emitterLifeSpan, currentTime);
    }

    XTimothyHyperspace(XVector position, XVector velocity, int emitterLifeSpan, long currentTime) {
        this.position = position;
        this.velocity = velocity;
        this.emitterLifeSpan = emitterLifeSpan;
        this.lights = new ArrayList<>();
        this.bornTime = currentTime;
    }

    void move(long currentTime) {
        if (currentTime - this.bornTime < this.emitterLifeSpan) {
            this.emitLights(currentTime);
            this.position.iAdd(this.velocity);
        }
        this.lights.forEach(XTimothyLight::move);
    }

    private void emitLights(long currentTime) {
        IntStream.range(0, EMITTER_COUNT).forEach(a -> {
            XVector velocity = XVector.polar(SPEED, XRandom.sVonMisesVariate());
            this.lights.add(new XTimothyLight(
                    this.position.copy(),
                    velocity,
                    START_DIAMETER,
                    STOP_DIAMETER,
                    LIFE_SPAN,
                    (Double) XRandom.sChoice(-DIRECTION_CHANGE_PER_FRAME, +DIRECTION_CHANGE_PER_FRAME),
                    PALETTE,
                    currentTime
            ));
        });
    }

    boolean draw(Graphics surface, long currentTime) {
        this.lights.removeIf(light -> light.draw(surface, currentTime));
        // Let the caller know if the animation is done.
        return this.lights.isEmpty();
    }
}
