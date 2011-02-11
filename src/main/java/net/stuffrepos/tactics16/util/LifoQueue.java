package net.stuffrepos.tactics16.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class LifoQueue<T> implements Iterable<T>, Cloneable {

    private List<T> stack = new ArrayList<T>();

    public LifoQueue(Collection<? extends T> c) {
        stack.addAll(c);
    }

    public LifoQueue() {
    }

    public boolean add(T e) {
        return stack.add(e);
    }

    public T poll() {
        if (stack.isEmpty()) {
            return null;
        } else {
            T e = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
            return e;
        }
    }

    public T peek() {
        if (stack.isEmpty()) {
            return null;
        } else {
            return stack.get(stack.size() - 1);
        }
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void addAll(Collection<? extends T> c) {
        stack.addAll(c);
    }

    public Iterator<T> iterator() {
        return stack.iterator();
    }

    public boolean contains(T e) {
        return stack.contains(e);
    }

    @Override
    public LifoQueue<T> clone() {        
        return new LifoQueue<T>(stack);
    }

    public void clear() {
        stack.clear();
    }

    public int size() {
        return stack.size();
    }
}
