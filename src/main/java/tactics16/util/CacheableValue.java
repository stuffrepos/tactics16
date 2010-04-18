package tactics16.util;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CacheableValue<T> {

    private boolean calculated = false;
    private T value;

    public synchronized T getValue() {
        if (!calculated) {
            this.value = calculate();
            this.calculated = true;
        }

        return this.value;
    }

    protected abstract T calculate();

    public synchronized void clear() {
        this.calculated = false;
        this.value = null;
    }
}
