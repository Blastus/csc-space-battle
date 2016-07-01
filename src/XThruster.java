import java.awt.*;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 2 June 2016.
 */
class XThruster {
    private final XVector position;
    private final int particleCount;
    private final int minParticleLength;
    private final int maxParticleLength;
    private final double deviance;

    XThruster(XVector position, int particleCount, int minParticleLength, int maxParticleLength, double deviance) {
        this.position = position;
        this.particleCount = particleCount;
        this.minParticleLength = minParticleLength;
        this.maxParticleLength = maxParticleLength;
        this.deviance = deviance;
    }

    void draw(Graphics surface, double direction) {
        int x = this.position.getIntX();
        int y = this.position.getIntY();
        IntStream.range(0, this.particleCount).forEach(a -> {
            surface.setColor(XColor.RED.interpolateRandom(XColor.YELLOW));
            XVector end = XVector.polar(
                    XRandom.sRandInt(this.minParticleLength, this.maxParticleLength),
                    direction + XRandom.sUniform(-this.deviance, +this.deviance)
            );
            end.iAdd(this.position);
            surface.drawLine(x, y, end.getIntX(), end.getIntY());
        });
    }
}
