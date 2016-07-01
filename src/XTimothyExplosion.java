import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 13 June 2016.
 */
class XTimothyExplosion {
    private static final int PRIMARY_EXPLOSION_DEVIANCE = 25;
    private static final int PRIMARY_EXPLOSION_START_DIAMETER = 25;
    private static final int PRIMARY_EXPLOSION_STOP_DIAMETER = 0;
    private static final int PRIMARY_EXPLOSION_LIFE_SPAN = 500;
    private static final XColor[] PALETTE = {
            XColor.YELLOW,
            XColor.RED
    };
    private static final int SECONDARY_EXPLOSION_EMITTER_COUNT = 3;
    private static final int SECONDARY_EXPLOSION_MIN_SPEED = 2;
    private static final int SECONDARY_EXPLOSION_MAX_SPEED = 6;
    private static final int SECONDARY_EXPLOSION_START_DIAMETER = 15;
    private static final int SECONDARY_EXPLOSION_STOP_DIAMETER = 0;
    private static final int SECONDARY_EXPLOSION_LIFE_SPAN = 500;
    private final XVector position;
    private final XVector velocity;
    private final int emitterLifeSpan;
    private final ArrayList<XTimothyLight> primaryLights;
    private final ArrayList<XTimothyLight> secondaryLights;
    private final long bornTime;

    XTimothyExplosion(XVector position, int emitterLifeSpan, long currentTime) {
        this(position, new XVector(), emitterLifeSpan, currentTime);
    }

    XTimothyExplosion(XVector position, XVector velocity, int emitterLifeSpan, long currentTime) {
        this.position = position;
        this.velocity = velocity;
        this.emitterLifeSpan = emitterLifeSpan;
        this.primaryLights = new ArrayList<>();
        this.secondaryLights = new ArrayList<>();
        this.bornTime = currentTime;
    }

    void move(long currentTime) {
        this.secondaryLights.forEach(XTimothyLight::move);
        if (currentTime - this.bornTime < this.emitterLifeSpan) {
            this.emitLights(currentTime);
            this.position.iAdd(this.velocity);
        }
    }

    private void emitLights(long currentTime) {
        // Add primary lights.
        XVector offset = XVector.polar(XRandom.sUniform(PRIMARY_EXPLOSION_DEVIANCE), XRandom.sVonMisesVariate());
        this.primaryLights.add(new XTimothyLight(
                this.position.add(offset),
                new XVector(),
                PRIMARY_EXPLOSION_START_DIAMETER,
                PRIMARY_EXPLOSION_STOP_DIAMETER,
                PRIMARY_EXPLOSION_LIFE_SPAN,
                0,
                PALETTE,
                currentTime
        ));
        // Add secondary lights.
        IntStream.range(0, SECONDARY_EXPLOSION_EMITTER_COUNT).forEach(a -> {
            XVector velocity = XVector.polar(
                    XRandom.sUniform(SECONDARY_EXPLOSION_MIN_SPEED, SECONDARY_EXPLOSION_MAX_SPEED),
                    XRandom.sVonMisesVariate()
            );
            this.secondaryLights.add(new XTimothyLight(
                    this.position.copy(),
                    velocity,
                    SECONDARY_EXPLOSION_START_DIAMETER,
                    SECONDARY_EXPLOSION_STOP_DIAMETER,
                    SECONDARY_EXPLOSION_LIFE_SPAN,
                    0,
                    PALETTE,
                    currentTime
            ));
        });
    }

    boolean draw(Graphics surface, long currentTime) {
        this.secondaryLights.removeIf(light -> light.draw(surface, currentTime));
        this.primaryLights.removeIf(light -> light.draw(surface, currentTime));
        // Let the caller know if the explosion is done.
        return this.primaryLights.isEmpty() && this.secondaryLights.isEmpty();
    }
}
