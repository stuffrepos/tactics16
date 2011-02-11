package net.stuffrepos.tactics16.util.math;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Interval {

    private int min;
    private int max;

    public Interval(int initialValue) {
        min = initialValue;
        max = initialValue;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        normalize(min);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        normalize(max);
    }

    private void normalize(int reference) {
        if (max < min) {
            max = reference;
            min = reference;
        }
    }

    public boolean valueIn(int value) {
        return value >= min && value <= max;
    }

    public void setMinMax(int value) {
        setMin(value);
        setMax(value);
    }
}
