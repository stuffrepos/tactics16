package net.stuffrepos.tactics16.util.cache;

import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CacheableValue<T> {

    private boolean calculated = false;
    private T value;
    private T oldValue;
    private ListenerManager<CacheableValue<T>> listenerManager =
            new ListenerManager<CacheableValue<T>>(this);

    public synchronized T getValue() {
        if (!calculated) {
            oldValue = this.value;
            this.value = calculate();

            if (!compareValues(oldValue, this.value)) {
                listenerManager.fireChange();
            }
           
            this.calculated = true;
        }

        return this.value;
    }

    protected abstract T calculate();

    public synchronized void clear() {
        this.calculated = false;
        this.value = null;
    }

    public void addListener(Listener<CacheableValue<T>> listener) {
        listenerManager.addListener(listener);
    }

    private boolean compareValues(T o1, T o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            return o1.equals(o2);
        }
    }

    public T getOldValue() {
        return oldValue;
    }
}
