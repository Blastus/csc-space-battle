import java.awt.*;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 10 June 2016.
 */
class XExplosion {
    static final double START_SIZE = 0.5;
    private static final double MIDDLE_BIAS = 0.2;
    private static final double STOP_SIZE = 0.1;
    private static final double MIDDLE_SIZE = 1.0;
    private final XVector position;
    private final int effectSize;
    private final int particleSize;
    private final int particleCount;
    private final int lifeSpan;
    private final XColor[] palette;
    private final long bornTime;

    XExplosion(
            XVector position,
            int effectSize,
            int particleSize,
            int particleCount,
            int lifeSpan,
            XColor[] palette,
            long currentTime
    ) {
        this.position = position;
        this.effectSize = effectSize;
        this.particleSize = particleSize;
        this.particleCount = particleCount;
        this.lifeSpan = lifeSpan;
        this.palette = palette;
        this.bornTime = currentTime;
    }

    boolean draw(Graphics surface, long currentTime) {
        double ratio = 1.0 * (currentTime - this.bornTime) / this.lifeSpan;
        if (ratio > 1)
            // Signal the caller to remove the explosion.
            return true;
        double scale = this.getScale(ratio);
        double currentEffectSize = this.effectSize * scale;
        double currentParticleSize = this.particleSize * scale;
        int particleDiameter = (int) currentParticleSize;
        double particleRadius = currentParticleSize / 2;
        IntStream.range(0, this.particleCount).forEach(a -> {
            surface.setColor(XColor.interpolateRandom(this.palette));
            XVector offset = XVector.polar(XRandom.sUniform(currentEffectSize), XRandom.sVonMisesVariate());
            offset.iAdd(this.position);
            surface.fillOval(
                    (int) (offset.getX() - particleRadius),
                    (int) (offset.getY() - particleRadius),
                    particleDiameter,
                    particleDiameter
            );
        });
        return false;
    }

    private double getScale(double ratio) {
        return ratio < MIDDLE_BIAS ?
                // If MIDDLE_SIZE is larger than START_SIZE, the explosion is getting larger.
                XRandom.interpolate(START_SIZE, MIDDLE_SIZE, ratio / MIDDLE_BIAS) :
                // If MIDDLE_SIZE is larger than STOP_SIZE, the explosion is getting smaller.
                XRandom.interpolate(MIDDLE_SIZE, STOP_SIZE, (ratio - MIDDLE_BIAS) / (1 - MIDDLE_BIAS));
    }
}
