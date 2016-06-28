import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

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

    XVector getPoint(int offset) {
        return this.points[offset];
    }

    void expand(double magnitude) {
        Arrays.stream(this.points).forEach(point -> point.addMagnitude(magnitude));
    }

    void rotate(double direction) {
        Arrays.stream(this.points).forEach(point -> point.addDirection(direction));
    }

    void scale(double magnitude) {
        Arrays.stream(this.points).forEach(point -> point.ipMul(magnitude));
    }

    void translate(XVector position) {
        Arrays.stream(this.points).forEach(point -> point.ipAdd(position));
    }

    Polygon value() {
        return new Polygon(
                Arrays.stream(this.points).mapToInt(XVector::getIntX).toArray(),
                Arrays.stream(this.points).mapToInt(XVector::getIntY).toArray(),
                this.points.length);
    }

    double getMinX() {
        Optional<Double> x = Arrays.stream(this.points).map(XVector::getX).reduce(Math::min);
        if (x.isPresent())
            return x.get();
        throw new RuntimeException("cannot get min x");
    }

    double getMinY() {
        Optional<Double> y = Arrays.stream(this.points).map(XVector::getY).reduce(Math::min);
        if (y.isPresent())
            return y.get();
        throw new RuntimeException("cannot get min y");
    }

    double getMaxX() {
        Optional<Double> x = Arrays.stream(this.points).map(XVector::getX).reduce(Math::max);
        if (x.isPresent())
            return x.get();
        throw new RuntimeException("cannot get max x");
    }

    double getMaxY() {
        Optional<Double> y = Arrays.stream(this.points).map(XVector::getY).reduce(Math::max);
        if (y.isPresent())
            return y.get();
        throw new RuntimeException("cannot get max y");
    }

    double getWidth() {
        return this.getMaxX() - this.getMinX();
    }

    double getHeight() {
        return this.getMaxY() - this.getMinY();
    }

    int getWidthInt() {
        return (int) Math.ceil(this.getWidth());
    }

    int getHeightInt() {
        return (int) Math.ceil(this.getHeight());
    }

    void draw(Graphics surface, Color outline) {
        this.draw(surface, outline, null);
    }

    void draw(Graphics surface, Color outline, Color fill) {
        Polygon shape = this.value();
        if (fill != null) {
            surface.setColor(fill);
            surface.fillPolygon(shape);
        }
        surface.setColor(outline);
        surface.drawPolygon(shape);
    }
}
