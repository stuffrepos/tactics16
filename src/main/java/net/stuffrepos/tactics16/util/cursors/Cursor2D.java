package net.stuffrepos.tactics16.util.cursors;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Cursor2D {

    private Cursor1D h = new Cursor1D();
    private Cursor1D v = new Cursor1D();
    private Coordinate position = new Coordinate();
    private ListenerManager<Cursor2D> listenerManager = new ListenerManager<Cursor2D>(this);

    public Cursor2D() {
        Listener<Cursor1D> cursor1dListener = new Listener<Cursor1D>() {

            public void onChange(Cursor1D cursor) {
                position.setXY(h.getCurrent(), v.getCurrent());
                listenerManager.fireChange();
            }
        };

        h.addListener(cursor1dListener);
        v.addListener(cursor1dListener);

        setKeys(GameKey.LEFT, GameKey.RIGHT, GameKey.UP, GameKey.DOWN);
    }

    public void addListener(Listener<Cursor2D> listener) {
        listenerManager.addListener(listener);
    }

    public int getX() {
        return h.getCurrent();
    }

    public int getY() {
        return v.getCurrent();
    }

    public int getWidth() {
        return h.getLength();
    }

    public int getHeight() {
        return v.getLength();
    }

    public void setKeys(GameKey leftKey, GameKey rightKey, GameKey upKey, GameKey downKey) {
        h.setKeys(leftKey, rightKey);
        v.setKeys(upKey, downKey);
    }

    public void update(long elapsedTime) {
        h.update(elapsedTime);
        v.update(elapsedTime);
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setDimension(int width, int height) {
        h.setLength(width);
        v.setLength(height);
    }

    @Override
    public String toString() {
        return String.format("h: %s, v: %s", h, v);
    }

    public void moveTo(Coordinate position) {
        h.moveTo(position.getX());
        v.moveTo(position.getY());
    }

    public void moveX(int increment) {
        h.moveTo(h.getCurrent() + increment);
    }

    public void moveY(int increment) {
        v.moveTo(v.getCurrent() + increment);
    }

    public void moveXY(int incrementX, int incrementY) {
        moveTo(new Coordinate(h.getCurrent() + incrementX, v.getCurrent() + incrementY));
    }
}
