package tactics16.util.cursors;

import tactics16.util.cache.CacheableValue;
import tactics16.util.*;
import java.util.LinkedList;
import java.util.List;
import tactics16.GameKey;
import tactics16.MyGame;
import tactics16.game.Coordinate;
import tactics16.util.listeners.Listener;
import tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ObjectCursor2D<T> {

    private ListenerManager<ObjectCursor2D<T>> listenerManager =
            new ListenerManager<ObjectCursor2D<T>>(this);
    private T lastSelected = null;
    private Cursor2D cursor = new Cursor2D();
    private List<ObjectArea> objects = new LinkedList<ObjectArea>();
    private CacheableValue<T> selected = new CacheableValue<T>() {

        @Override
        protected T calculate() {
            for (ObjectArea obj : objects) {
                if (obj.inArea(cursor.getPosition())) {
                    return obj.getObject();
                }
            }

            return null;
        }
    };

    public ObjectCursor2D() {
        cursor.addListener(new Listener<Cursor2D>() {

            public void onChange(Cursor2D source) {
                selected.clear();
            }
        });
    }

    public void setObject(T obj, int x, int y, int w, int h) {
        objects.add(new ObjectArea(obj, x, y, w, h));
        cursor.setDimension(Math.max(x + 1, cursor.getWidth()), Math.max(y + 1, cursor.getHeight()));
    }

    private void moveCursor(int directionX, int directionY) {

        int incrementX = (int) Math.signum(directionX);
        int incrementY = (int) Math.signum(directionY);
        lastSelected = getSelected();        
        do {
            cursor.moveXY(incrementX, incrementY);
        } while (getSelected() == null || getSelected() == lastSelected);

        listenerManager.fireChange();
    }

    public void update(long elapsedTime) {
        if (MyGame.getInstance().isKeyPressed(GameKey.UP)) {
            moveCursor(0, -1);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.DOWN)) {
            moveCursor(0, 1);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.LEFT)) {
            moveCursor(-1, 0);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.RIGHT)) {
            moveCursor(1, 0);
        }
    }

    public T getSelected() {
        return selected.getValue();
    }

    public void addListener(Listener<ObjectCursor2D<T>> listener) {
        listenerManager.addListener(listener);
    }

    public T getLastSelected() {
        return lastSelected;
    }

    private class ObjectArea {

        private T object;
        private Coordinate position;
        private Coordinate size;

        public ObjectArea(T object, int x, int y, int w, int h) {
            this.object = object;
            this.position = new Coordinate(x, y);
            this.size = new Coordinate(w, h);
        }

        public boolean inArea(Coordinate p) {
            return p.inRectangle(position, size);
        }

        public T getObject() {
            return object;
        }

        @Override
        public String toString() {
            return position.toStringInt() + " / " + size.toStringInt();
        }
    }
}
