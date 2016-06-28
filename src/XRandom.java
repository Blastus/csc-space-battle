import java.util.Random;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XRandom extends Random {
    private int randomRange(int stop) {
        return this.randomRange(0, stop);
    }

    private int randomRange(int start, int stop) {
        return this.randomRange(start, stop, 1);
    }

    private int randomRange(int start, int stop, int step) {
        int width = stop - start;
        if (step == 1 && width > 0)
            return start + this.randomBelow(width);
        int number;
        if (step > 0)
            number = (width + step - 1) / step;
        else if (step < 0)
            number = (width + step + 1) / step;
        else
            throw new RuntimeException("zero step for randomRange");
        if (number <= 0)
            throw new RuntimeException("empty range for randomRange");
        return start + step * this.randomBelow(number);
    }

    private int randomBelow(int number) {
        if (number == 0)
            return 0;
        int bits = 32 - Integer.numberOfLeadingZeros(number);
        while (true) {
            int result = this.next(bits);
            if (result < number)
                return result;
        }
    }

    int randomInteger(int a, int b) {
        return this.randomRange(a, b + 1);
    }

    Object choice(Object... objects) {
        return objects[this.randomRange(objects.length)];
    }

    double random() {
        return super.nextDouble();
    }

    double uniform(double a) {
        return this.uniform(0.0, a);
    }

    double uniform(double a, double b) {
        return a + (b - a) * this.random();
    }
}
