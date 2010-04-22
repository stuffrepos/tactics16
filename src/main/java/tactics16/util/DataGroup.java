package tactics16.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import tactics16.animation.SpriteAnimation;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataGroup<T extends Nameable> implements Iterable<T> {

    private java.util.Map<String, T> map = new TreeMap<String, T>();

    public boolean add(T data) {

        T found = this.get(data.getName());

        if (found != null) {
            throw new RuntimeException("Data with name \"" +
                    data.getName() + "\" already exists");
        }

        this.map.put(data.getName(), data);
        return true;
    }

    public T getRequired(String name) {
        T data = this.get(name);

        if (data == null) {
            throw new RuntimeException("Data not found: " + name);
        } else {
            return data;
        }
    }

    public T get(String name) {
        return this.map.get(name);
    }

    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }

    public void addIgnore(T o) {
        if (this.get(o.getName()) == null) {
            add(o);
        }
    }

    public boolean has(String name) {
        return this.get(name) != null;
    }

    public Iterable<Entry<String, T>> entrySet() {
        return map.entrySet();
    }
}
