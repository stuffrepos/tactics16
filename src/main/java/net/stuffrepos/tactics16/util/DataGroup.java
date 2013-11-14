package net.stuffrepos.tactics16.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataGroup<T extends Nameable> implements Iterable<T> {

    private java.util.Map<String, T> map = new TreeMap<String, T>();

    public void add(T data) {

        T found = this.get(data.getName());

        if (found != null) {
            throw new RuntimeException("Data with name \"" +
                    data.getName() + "\" already exists");
        }

        put(data);
    }

    public void put(T data) {
        this.map.put(data.getName(), data);
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

    public int size() {
        return map.size();
    }

    public Collection<T> getValues() {
        return map.values();
    }

    public void remove(String name) {
        map.remove(name);
    }
}
