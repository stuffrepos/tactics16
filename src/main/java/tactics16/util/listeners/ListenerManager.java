package tactics16.util.listeners;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ListenerManager<T> {

    private List<Listener<T>> listeners = new LinkedList<Listener<T>>();
    private T source;

    public ListenerManager(T source) {
        this.source = source;
    }
    
    public void addListener(Listener<T> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            listener.onChange(source);
        }
    }

    public void fireChange() {
        for (Listener<T> listener : listeners) {
            listener.onChange(source);
        }
    }
}
