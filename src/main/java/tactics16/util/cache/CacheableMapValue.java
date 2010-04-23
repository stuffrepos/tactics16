package tactics16.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CacheableMapValue<Key, Value> {

    private Map<Key, CacheableValue<Value>> map = new HashMap<Key, CacheableValue<Value>>();

    public Value getValue(final Key key) {
        CacheableValue<Value> value = map.get(key);
        if (value == null) {
            value = new CacheableValue<Value>() {

                @Override
                protected Value calculate() {
                    return CacheableMapValue.this.calculate(key);
                }
            };
            map.put(key, value);
        }

        return value.getValue();
    }

    public void clear() {
        map.clear();
    }

    protected abstract Value calculate(Key key);
}