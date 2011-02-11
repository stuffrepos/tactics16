package net.stuffrepos.tactics16.util;

import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class SizeConfig {

    private Integer min;
    private Integer max;
    private Integer current;
    private ListenerManager<SizeConfig> listenerManager = new ListenerManager<SizeConfig>(this);
    private CacheableValue<Integer> calculatedValue = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            return SizeConfig.this.calculate();
        }
    };

    protected abstract int calculate();    

    public int getValue() {
        int value = this.calculatedValue.getValue();

        if (max != null) {
            value = Math.min(value, max);
        }

        if (min != null) {
            value = Math.max(value, min);
        }


        if (current == null || value != current)  {
            current = value;
            listenerManager.fireChange();
        }

        return current;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void clear() {
        calculatedValue.clear();
    }

    public void addListener(Listener<SizeConfig> listener) {
        listenerManager.addListener(listener);
    }

    public void setFixed(Integer size) {
        this.max = size;
        this.min = size;
    }
}
