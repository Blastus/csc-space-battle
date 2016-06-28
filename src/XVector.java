import java.awt.*;
import java.text.MessageFormat;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XVector {
    static final double CIRCLE_0_8 = Math.PI * 0 / 4;
    static final double CIRCLE_1_8 = Math.PI * 1 / 4;
    static final double CIRCLE_2_8 = Math.PI * 2 / 4;
    static final double CIRCLE_3_8 = Math.PI * 3 / 4;
    static final double CIRCLE_4_8 = Math.PI * 4 / 4;
    static final double CIRCLE_5_8 = Math.PI * 5 / 4;
    static final double CIRCLE_6_8 = Math.PI * 6 / 4;
    static final double CIRCLE_7_8 = Math.PI * 7 / 4;
    static final double CIRCLE_8_8 = Math.PI * 8 / 4;
    private double x;
    private double y;

    XVector() {
        this(0, 0);
    }

    XVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    static XVector polar(double magnitude, double direction) {
        return new XVector(magnitude * Math.sin(direction), magnitude * Math.cos(direction));
    }

    public String toString() {
        return MessageFormat.format("{0}({1}, {2})", this.getClass().getName(), this.x, this.y);
    }

    String toPolarString() {
        return MessageFormat.format(
                "{0}.polar({1}, {2})",
                this.getClass().getName(),
                this.getMagnitude(),
                this.getDirection());
    }

    double getX() {
        return this.x;
    }

    double getY() {
        return this.y;
    }

    int getIntX() {
        return (int) this.x;
    }

    int getIntY() {
        return (int) this.y;
    }

    void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double getMagnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    void setMagnitude(double magnitude) {
        this.ipMul(magnitude / this.getMagnitude());
    }

    void addMagnitude(double magnitude) {
        this.setMagnitude(this.getMagnitude() + magnitude);
    }

    double getDirection() {
        return Math.atan2(this.x, this.y);
    }


    void setDirection(double direction) {
        double magnitude = this.getMagnitude();
        this.x = magnitude * Math.sin(direction);
        this.y = magnitude * Math.cos(direction);
    }

    void addDirection(double direction) {
        this.setDirection(this.getDirection() + direction);
    }

    XVector copy() {
        return new XVector(this.x, this.y);
    }

    void copy(XVector other) {
        this.x = other.x;
        this.y = other.y;
    }

    XVector add(double other) {
        return new XVector(this.x + other, this.y + other);
    }

    XVector add(XVector other) {
        return new XVector(this.x + other.x, this.y + other.y);
    }

    XVector sub(double other) {
        return new XVector(this.x - other, this.y - other);
    }

    XVector sub(XVector other) {
        return new XVector(this.x - other.x, this.y - other.y);
    }

    XVector mul(double other) {
        return new XVector(this.x * other, this.y * other);
    }

    XVector mul(XVector other) {
        return new XVector(this.x * other.x, this.y * other.y);
    }

    XVector div(double other) {
        return new XVector(this.x / other, this.y / other);
    }

    XVector div(XVector other) {
        return new XVector(this.x / other.x, this.y / other.y);
    }

    XVector mod(double other) {
        return new XVector(this.x % other, this.y % other);
    }

    XVector mod(XVector other) {
        return new XVector(this.x % other.x, this.y % other.y);
    }

    XVector pow(double other) {
        return new XVector(Math.pow(this.x, other), Math.pow(this.y, other));
    }

    XVector pow(XVector other) {
        return new XVector(Math.pow(this.x, other.x), Math.pow(this.y, other.y));
    }

    void ipAdd(double other) {
        this.x += other;
        this.y += other;
    }

    void ipAdd(XVector other) {
        this.x += other.x;
        this.y += other.y;
    }

    void ipSub(double other) {
        this.x -= other;
        this.y -= other;
    }

    void ipSub(XVector other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    void ipMul(double other) {
        this.x *= other;
        this.y *= other;
    }

    void ipMul(XVector other) {
        this.x *= other.x;
        this.y *= other.y;
    }

    void ipDiv(double other) {
        this.x /= other;
        this.y /= other;
    }

    void ipDiv(XVector other) {
        this.x /= other.x;
        this.y /= other.y;
    }

    void ipMod(double other) {
        this.x %= other;
        this.y %= other;
    }

    void ipMod(XVector other) {
        this.x %= other.x;
        this.y %= other.y;
    }

    void ipPow(double other) {
        this.x = Math.pow(this.x, other);
        this.y = Math.pow(this.y, other);
    }

    void ipPow(XVector other) {
        this.x = Math.pow(this.x, other.x);
        this.y = Math.pow(this.y, other.y);
    }

    void random(Dimension size) {
        this.x = XSpaceBattle.CHAOS.uniform(size.getWidth());
        this.y = XSpaceBattle.CHAOS.uniform(size.getHeight());
    }

    void clampXY(Dimension size) {
        double width = size.getWidth();
        double height = size.getHeight();
        this.x = (this.x % width + width) % width;
        this.y = (this.y % height + height) % height;
    }

    void clampMagnitude(double magnitude) {
        if (this.getMagnitude() > magnitude)
            this.setMagnitude(magnitude);
    }

    boolean inBox(double minX, double minY, double maxX, double maxY) {
        return minX <= this.x && this.x <= maxX && minY <= this.y && this.y <= maxY;
    }
}
