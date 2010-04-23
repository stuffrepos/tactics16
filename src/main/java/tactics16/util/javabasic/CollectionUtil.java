package tactics16.util.javabasic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CollectionUtil {

    public static <T> List<T> listFromArray(T[] array) {
        List<T> list = new ArrayList<T>(array.length);

        for (T e : array) {
            list.add(e);
        }

        return list;
    }

    public static <T> Iterable<T> iterableFromIterator(final Iterator<T> iterator) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }
}
