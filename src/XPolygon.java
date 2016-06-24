import java.awt.*;
import java.util.Arrays;

/*
 * Created by Stephen "Zero" Chappell on 2 June 2016.
 */
class XPolygon {
    final private XVector[] points;

    XPolygon(XVector... points) {
        this.points = points;
    }

    XPolygon copy() {
        return new XPolygon(Arrays.stream(this.points).map(XVector::copy).toArray(XVector[]::new));
    }

    void expand(double magnitude) {
        Arrays.stream(this.points).forEach(point -> point.addMagnitude(magnitude));
    }

    void rotate(double direction) {
        Arrays.stream(this.points).forEach(point -> point.addDirection(direction));
    }

    void translate(XVector position) {
        Arrays.stream(this.points).forEach(point -> point.ipAdd(position));
    }

    void draw(Graphics surface, Color outline) {
        this.draw(surface, outline, null);
    }

    void draw(Graphics surface, Color outline, Color fill) {
        Polygon shape = new Polygon(
                Arrays.stream(this.points).mapToInt(XVector::getIntX).toArray(),
                Arrays.stream(this.points).mapToInt(XVector::getIntY).toArray(),
                this.points.length);
        if (fill != null) {
            surface.setColor(fill);
            surface.fillPolygon(shape);
        }
        surface.setColor(outline);
        surface.drawPolygon(shape);
    }
}
