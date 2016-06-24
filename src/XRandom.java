import java.util.Random;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XRandom extends Random {
    double uniform(double a) {
        return this.uniform(0.0, a);
    }

    double uniform(double a, double b) {
        return a + (b - a) * this.nextDouble();
    }
}
