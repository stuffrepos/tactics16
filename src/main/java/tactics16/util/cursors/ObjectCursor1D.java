package tactics16.util.cursors;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ObjectCursor1D<T> {

    private List<T> list;
    private Cursor1D cursor = new Cursor1D();

    public ObjectCursor1D() {
        this(null);
    }

    public ObjectCursor1D(List<T> list) {
        this.setList(list);
    }

    public List<T> getList() {
        return list;
    }

    public T getSelected() {
        return this.list.get(getCursor().getCurrent());
    }

    public void update(long elapsedTime) {
        getCursor().setLength(list.size());
        getCursor().update(elapsedTime);
    }

    public void setList(List<T> list) {
        this.list = (list == null ? new ArrayList<T>() : list);
        this.getCursor().setLength(this.list.size());
    }

    public Cursor1D getCursor() {
        return cursor;
    }

    public void clear() {
        list.clear();
    }
}
