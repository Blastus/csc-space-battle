/*
 * Created by Stephen "Zero" Chappell on 3 June 2016.
 */
class XCircle {
    final XVector position;
    int diameter;
    int radius;

    XCircle() {
        this(0);
    }

    XCircle(int diameter) {
        this(new XVector(), diameter);
    }

    XCircle(XVector position, int diameter) {
        this.position = position;
        this.diameter = diameter;
        this.radius = diameter >> 1;
    }

    XVector getPosition() {
        return this.position;
    }

    int getDiameter() {
        return this.diameter;
    }

    void setDiameter(int diameter) {
        this.diameter = diameter;
        this.radius = diameter >> 1;
    }

    int getRadius() {
        return this.radius;
    }

    boolean contains(XCircle other, int margin) {
        return this.position.sub(other.position).getMagnitude() + other.radius <= this.radius - margin;
    }

    boolean overlaps(XCircle other) {
        return this.getMarginalDistance(other) < 0;
    }

    boolean overlaps(XCircle other, double distanceScale) {
        return this.position.sub(other.position).getMagnitude() * distanceScale < this.radius + other.radius;
    }

    private double getMarginalDistance(XCircle other) {
        return this.position.sub(other.position).getMagnitude() - this.radius - other.radius;
    }
}
