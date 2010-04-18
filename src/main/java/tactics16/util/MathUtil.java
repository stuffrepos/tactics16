package tactics16.util;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MathUtil {

    public static long getLoopCurrentIndex(long n, long elapsed, long loopSize) {
        long cicleN = n * 2 - 1;
        long cicle = (int) (elapsed / loopSize) % cicleN;
        return cicle >= n
                ? cicleN - cicle - 1
                : cicle;
    }
}
