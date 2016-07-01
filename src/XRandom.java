import java.util.HashSet;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/*
 * Created by Stephen "Zero" Chappell on 6 June 2016.
 */
class XRandom extends Random {
    static final double NV_MAGIC_CONST = 4 * Math.exp(-0.5) / Math.sqrt(2);
    static final double TWO_PI = 2 * Math.PI;
    static final double LOG4 = Math.log(4);
    static final double SG_MAGIC_CONST = 1 + Math.log(4.5);
    static final double BPF = 53;
    static final double RECIPROCAL_BPF = Math.pow(2, -BPF);
    private static final XRandom CHAOS = new XRandom();

    static void sSeed() {
        CHAOS.seed();
    }

    static void sSeed(Object a) {
        CHAOS.seed(a);
    }

    static int sGetRandBits(int k) {
        return CHAOS.getRandBits(k);
    }

    static int sRandRange() {
        return CHAOS.randRange();
    }

    static int sRandRange(int stop) {
        return CHAOS.randRange(stop);
    }

    static int sRandRange(int start, int stop) {
        return CHAOS.randRange(start, stop);
    }

    static int sRandRange(int start, int stop, int step) {
        return CHAOS.randRange(start, stop, step);
    }

    static int sRandInt(int a, int b) {
        return CHAOS.randInt(a, b);
    }

    static Object sChoice(Object... seq) {
        return CHAOS.choice(seq);
    }

    static void sShuffle(Object[] x) {
        CHAOS.shuffle(x);
    }

    static void sShuffle(Object[] x, Supplier<Double> random) {
        CHAOS.shuffle(x, random);
    }

    static Object[] sSample(Object[] population, int k) {
        return CHAOS.sample(population, k);
    }

    static double sRandom() {
        return CHAOS.random();
    }

    static double sUniform(double a) {
        return CHAOS.uniform(a);
    }

    static double sUniform(double a, double b) {
        return CHAOS.uniform(a, b);
    }

    static double sTriangular() {
        return CHAOS.triangular();
    }

    static double sTriangular(double low) {
        return CHAOS.triangular(low);
    }

    static double sTriangular(double low, double high) {
        return CHAOS.triangular(low, high);
    }

    static double sTriangular(double low, double high, double mode) {
        return CHAOS.triangular(low, high, mode);
    }

    static double sBetaVariate(double alpha, double beta) {
        return CHAOS.betaVariate(alpha, beta);
    }

    static double sExpoVariate(double lambda) {
        return CHAOS.expoVariate(lambda);
    }

    static double sGammaVariate(double alpha, double beta) {
        return CHAOS.gammaVariate(alpha, beta);
    }

    static double sGauss(double mu, double sigma) {
        return CHAOS.gauss(mu, sigma);
    }

    static double sLogNormVariate(double mu, double sigma) {
        return CHAOS.logNormVariate(mu, sigma);
    }

    static double sNormalVariate(double mu, double sigma) {
        return CHAOS.normalVariate(mu, sigma);
    }

    static double sVonMisesVariate() {
        return CHAOS.vonMisesVariate();
    }

    static double sVonMisesVariate(double mu, double kappa) {
        return CHAOS.vonMisesVariate(mu, kappa);
    }

    static double sParetoVariate(double alpha) {
        return CHAOS.paretoVariate(alpha);
    }

    static double sWeibullVariate(double alpha, double beta) {
        return CHAOS.weibullVariate(alpha, beta);
    }

    static double interpolate(double x, double y, double z) {
        return x * (1 - z) + y * z;
    }

    static int interpolate(int x, int y, double z) {
        return (int) (x * (1 - z) + y * z + 0.5);
    }

    void seed() {
        this.setSeed(System.nanoTime());
    }

    void seed(Object a) {
        this.setSeed(a.hashCode());
    }

    int getRandBits(int k) {
        return this.next(k);
    }

    int randRange() {
        return this.nextInt(Integer.MAX_VALUE);
    }

    int randRange(int stop) {
        return this.nextInt(stop);
    }

    int randRange(int start, int stop) {
        return start + this.nextInt(stop - start);
    }

    int randRange(int start, int stop, int step) {
        if (step > 0)
            return start + step * this.nextInt((stop - start + step - 1) / step);
        if (step < 0)
            return start + step * this.nextInt((stop - start + step + 1) / step);
        throw new RuntimeException("zero step for randRange()");
    }

    int randInt(int a, int b) {
        return a + this.nextInt(b - a + 1);
    }

    Object choice(Object... seq) {
        return seq[this.nextInt(seq.length)];
    }

    void shuffle(Object[] x) {
        IntStream.range(1, x.length).forEach(i -> {
            i = x.length - i;
            int j = this.nextInt(i + 1);
            Object[] temp = {x[j], x[i]};
            x[i] = temp[0];
            x[j] = temp[1];
        });
    }

    void shuffle(Object[] x, Supplier<Double> random) {
        IntStream.range(1, x.length).forEach(i -> {
            i = x.length - i;
            int j = (int) (random.get() * (i + 1));
            Object[] temp = {x[j], x[i]};
            x[i] = temp[0];
            x[j] = temp[1];
        });
    }

    Object[] sample(Object[] population, int k) {
        if (k > population.length)
            throw new RuntimeException("sample larger than population");
        Object[] result = new Object[k];
        int setSize = 21;
        if (k > 5)
            setSize += Math.pow(4, Math.ceil(Math.log(k * 3) / Math.log(4)));
        if (population.length > setSize) {
            HashSet<Integer> selected = new HashSet<>();
            IntStream.range(0, k).forEach(i -> {
                int j;
                do {
                    j = this.nextInt(population.length);
                } while (selected.contains(j));
                selected.add(j);
                result[i] = population[j];
            });
        } else {
            Object[] pool = population.clone();
            IntStream.range(0, k).forEach(i -> {
                int j = this.nextInt(population.length - i);
                result[i] = pool[j];
                pool[j] = pool[population.length - i - 1];
            });
        }
        return result;
    }

    double random() {
        return this.nextDouble();
    }

    double uniform(double a) {
        return a * this.nextDouble();
    }

    double uniform(double a, double b) {
        return XRandom.interpolate(a, b, this.nextDouble());
    }

    double triangular() {
        return this.nextDouble();
    }

    double triangular(double low) {
        return XRandom.interpolate(low, 1, this.nextDouble());
    }

    double triangular(double low, double high) {
        return XRandom.interpolate(low, high, this.nextDouble());
    }

    double triangular(double low, double high, double mode) {
        double u = this.nextDouble();
        double c;
        try {
            c = (mode - low) / (high - low);
        } catch (ArithmeticException error) {
            return low;
        }
        if (u > c) {
            u = 1 - u;
            c = 1 - c;
            double[] temp = {high, low};
            low = temp[0];
            high = temp[1];
        }
        return low + (high - low) * Math.sqrt(u * c);
    }

    double betaVariate(double alpha, double beta) {
        double y = this.gammaVariate(alpha, 1);
        return y == 0 ? 0 : y / (y + this.gammaVariate(beta, 1));
    }

    double expoVariate(double lambda) {
        return -Math.log(1 - this.nextDouble()) / lambda;
    }

    double gammaVariate(double alpha, double beta) {
        if (alpha <= 0 || beta <= 0)
            throw new RuntimeException("gammaVariate: alpha and beta must be > 0");
        if (alpha > 1) {
            double ainv, bbb, ccc, u1, v, x, z, r;
            ainv = Math.sqrt(2 * alpha - 1);
            bbb = alpha - LOG4;
            ccc = alpha + ainv;
            while (true) {
                u1 = this.nextDouble();
                if (u1 <= 1e-7 || u1 >= .9999999)
                    continue;
                v = Math.log(u1 / (1 - u1)) / ainv;
                x = alpha * Math.exp(v);
                z = u1 * u1 * (1 - this.nextDouble());
                r = bbb + ccc * v - x;
                if (r + SG_MAGIC_CONST - 4.5 * z >= 0 || r >= Math.log(z))
                    return x * beta;
            }
        }
        if (alpha < 1) {
            double b, p, x, u1;
            while (true) {
                b = (Math.E + alpha) / Math.E;
                p = b * this.nextDouble();
                x = p <= 1 ? Math.pow(p, 1 / alpha) : -Math.log((b - p) / alpha);
                u1 = this.nextDouble();
                if (p > 1) {
                    if (u1 <= Math.pow(x, alpha - 1))
                        break;
                } else if (u1 <= Math.exp(-x))
                    break;
            }
            return x * beta;
        }
        double u;
        do {
            u = this.nextDouble();
        } while (u <= 1e-7);
        return -Math.log(u) * beta;
    }

    double gauss(double mu, double sigma) {
        return mu + this.nextGaussian() * sigma;
    }

    double logNormVariate(double mu, double sigma) {
        return Math.exp(mu + this.nextGaussian() * sigma);
    }

    double normalVariate(double mu, double sigma) {
        return mu + this.nextGaussian() * sigma;
    }

    double vonMisesVariate() {
        return TWO_PI * this.nextDouble();
    }

    double vonMisesVariate(double mu, double kappa) {
        if (kappa <= 1e-6)
            return TWO_PI * this.nextDouble();
        double s = 0.5 / kappa;
        double r = s + Math.sqrt(1 + s * s);
        double z;
        while (true) {
            z = Math.cos(Math.PI * this.nextDouble());
            double d = z / (r + z);
            double u2 = this.nextDouble();
            if (u2 < 1 - d * d || u2 <= (1 - d) * Math.exp(d))
                break;
        }
        double q = 1 / r;
        return (mu + Math.acos((q + z) / (1 + q * z)) * (this.nextDouble() > 0.5 ? +1 : -1)) % TWO_PI;
    }

    double paretoVariate(double alpha) {
        return 1 / Math.pow(1 - this.nextDouble(), 1 / alpha);
    }

    double weibullVariate(double alpha, double beta) {
        return alpha * Math.pow(-Math.log(1 - this.nextDouble()), 1 / beta);
    }
}