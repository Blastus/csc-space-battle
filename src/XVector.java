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
        return new XVector(magnitude * Math.cos(direction), magnitude * Math.sin(direction));
    }

    public String toString() {
        return MessageFormat.format("{0}({1}, {2})", this.getClass().getName(), this.x, this.y);
    }

    String toPolarString() {
        return MessageFormat.format(
                "{0}.polar({1}, {2})",
                this.getClass().getName(),
                this.getMagnitude(),
                this.getDirection()
        );
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
        this.iMul(magnitude / this.getMagnitude());
    }

    void addMagnitude(double magnitude) {
        this.setMagnitude(this.getMagnitude() + magnitude);
    }

    double getDirection() {
        return Math.atan2(this.y, this.x);
    }


    void setDirection(double direction) {
        double magnitude = this.getMagnitude();
        this.x = magnitude * Math.cos(direction);
        this.y = magnitude * Math.sin(direction);
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

    XVector add(double value) {
        return new XVector(this.x + value, this.y + value);
    }

    XVector add(XVector other) {
        return new XVector(this.x + other.x, this.y + other.y);
    }

    XVector sub(double value) {
        return new XVector(this.x - value, this.y - value);
    }

    XVector sub(XVector other) {
        return new XVector(this.x - other.x, this.y - other.y);
    }

    XVector mul(double value) {
        return new XVector(this.x * value, this.y * value);
    }

    XVector mul(XVector other) {
        return new XVector(this.x * other.x, this.y * other.y);
    }

    XVector div(double value) {
        return new XVector(this.x / value, this.y / value);
    }

    XVector div(XVector other) {
        return new XVector(this.x / other.x, this.y / other.y);
    }

    XVector mod(double value) {
        return new XVector(this.x % value, this.y % value);
    }

    XVector mod(XVector other) {
        return new XVector(this.x % other.x, this.y % other.y);
    }

    XVector pow(double value) {
        return new XVector(Math.pow(this.x, value), Math.pow(this.y, value));
    }

    XVector pow(XVector other) {
        return new XVector(Math.pow(this.x, other.x), Math.pow(this.y, other.y));
    }

    void iAdd(double value) {
        this.x += value;
        this.y += value;
    }

    void iAdd(XVector other) {
        this.x += other.x;
        this.y += other.y;
    }

    void iSub(double value) {
        this.x -= value;
        this.y -= value;
    }

    void iSub(XVector other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    void iMul(double value) {
        this.x *= value;
        this.y *= value;
    }

    void iMul(XVector other) {
        this.x *= other.x;
        this.y *= other.y;
    }

    void iDiv(double value) {
        this.x /= value;
        this.y /= value;
    }

    void iDiv(XVector other) {
        this.x /= other.x;
        this.y /= other.y;
    }

    void iMod(double value) {
        this.x %= value;
        this.y %= value;
    }

    void iMod(XVector other) {
        this.x %= other.x;
        this.y %= other.y;
    }

    void iPow(double value) {
        this.x = Math.pow(this.x, value);
        this.y = Math.pow(this.y, value);
    }

    void iPow(XVector other) {
        this.x = Math.pow(this.x, other.x);
        this.y = Math.pow(this.y, other.y);
    }

    void random(Dimension size) {
        this.x = XRandom.sUniform(size.getWidth());
        this.y = XRandom.sUniform(size.getHeight());
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
