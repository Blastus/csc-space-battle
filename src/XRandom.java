import java.util.Random;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
// TODO class should be given an optional static interface via a singleton
class XRandom extends Random {
    int randomRange(int stop) {
        return this.randomRange(0, stop);
    }

    int randomRange(int start, int stop) {
        return this.randomRange(start, stop, 1);
    }

    int randomRange(int start, int stop, int step) {
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
        // TODO change condition to (number <= 1)
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
        // TODO the first argument can be changed from 0.0 to 0
        return this.uniform(0.0, a);
    }

    double uniform(double a, double b) {
        // TODO should be implemented with "c = r(); d = 1 - c; return a * d + b * c" pattern
        return a + (b - a) * this.random();
    }
}
