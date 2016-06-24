import java.util.Random;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XRandom extends Random {
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
        if (a <= b)
            return a + this.randomBelow(b - a + 1);
        throw new RuntimeException("a must be less than or equal to b");
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
