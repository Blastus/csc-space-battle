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
            surface.setColor(XColor.RED.interpolateRandom(XColor.YELLOW).value());
            XVector end = XVector.polar(
                    XSpaceBattle.CHAOS.randomInteger(this.minParticleLength, this.maxParticleLength),
                    XSpaceBattle.CHAOS.uniform(-this.deviance, +this.deviance) - direction + XVector.CIRCLE_2_8);
            end.ipAdd(this.position);
            surface.drawLine(x, y, end.getIntX(), end.getIntY());
        });
    }
}
