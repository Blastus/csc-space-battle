/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XCircle {
    final XVector position;
    int radius;
    int diameter;

    XCircle() {
        this(0);
    }

    XCircle(int radius) {
        this(new XVector(), radius);
    }

    XCircle(XCircle other) {
        this(other.position.copy(), other.radius);
    }

    XCircle(XVector position, int radius) {
        this.position = position;
        this.setRadius(radius);
    }

    XVector getPosition() {
        return this.position;
    }

    int getRadius() {
        return this.radius;
    }

    void setRadius(int value) {
        this.radius = value;
        this.diameter = radius << 1;
    }

    int getDiameter() {
        return this.diameter;
    }

    boolean contains(XVector value) {
        return this.position.getDistance(value) <= this.radius;
    }

    boolean contains(XCircle other) {
        return this.position.getDistance(other.position) + other.radius <= this.radius;
    }

    boolean containsWithMargin(XCircle other) {
        return this.position.getDistance(other.position) + other.diameter <= this.radius;
    }

    boolean overlaps(XCircle other) {
        return this.getMarginalDistance(other) < 0;
    }

    boolean overlaps(XCircle other, int safetyFactor) {
        return this.position.getDistance(other.position) / safetyFactor < this.radius + other.radius;
    }

    boolean overlaps(XCircle other, double distanceScale) {
        return this.position.getDistance(other.position) * distanceScale < this.radius + other.radius;
    }

    double getMarginalDistance(XCircle other) {
        return this.position.getDistance(other.position) - this.radius - other.radius;
    }
}
