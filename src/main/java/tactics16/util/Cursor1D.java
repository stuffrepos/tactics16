package tactics16.util;

import tactics16.MyGame;
import tactics16.util.listeners.Listener;
import tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Cursor1D {

    private int length = Integer.MIN_VALUE;
    private int current = Integer.MAX_VALUE;
    private int lastCurrent;
    private int lastMove;
    private int nextKey, previousKey;
    private ListenerManager<Cursor1D> listenerManager = new ListenerManager<Cursor1D>(this);

    private void setCurrent(int current) {
        this.lastCurrent = this.current;
        this.current = current;
        if (this.current != this.getLastCurrent()) {
            this.listenerManager.fireChange();
        }
    }

    public void addListener(Listener<Cursor1D> listener) {
        listenerManager.addListener(listener);
    }

    private int getLastCurrent() {
        return lastCurrent;
    }

    public int getLastMove() {
        return lastMove;
    }

    public Cursor1D() {
        this(0);
    }

    public Cursor1D(int length) {
        this.setLength(length);
    }

    public void setKeys(int previousKey, int nextKey) {
        this.setNextKey(nextKey);
        this.setPreviousKey(previousKey);
    }

    public boolean moveNext() {
        return this.move(1);
    }

    public boolean movePrevious() {
        return this.move(-1);
    }

    private boolean move(int d) {
        lastMove = d;
        int tempCurrent;
        if (length > 0) {
            tempCurrent = this.current + d;

            if (tempCurrent < 0) {
                tempCurrent = getLength() - 1;
            } else if (tempCurrent >= getLength()) {
                tempCurrent = 0;
            }
        } else {
            tempCurrent = 0;
        }

        setCurrent(tempCurrent);

        return lastCurrent != current;
    }

    public boolean update(long elapsedTime) {
        if (MyGame.getInstance().keyPressed(getNextKey())) {
            return moveNext();
        } else if (MyGame.getInstance().keyPressed(getPreviousKey())) {
            return movePrevious();
        } else {
            return false;
        }
    }

    public void setLength(int length) {
        assert length >= 0;
        int old = this.length;
        this.length = length;

        if (old != this.length) {
            listenerManager.fireChange();
        }

        if (this.length == 0) {
            setCurrent(0);
        } else if (getCurrent() >= this.length) {
            setCurrent(this.length - 1);
        }


    }

    public int getLength() {
        return length;
    }

    public int getCurrent() {
        return current;
    }

    public int getNextKey() {
        return nextKey;
    }

    public void setNextKey(int nextKey) {
        this.nextKey = nextKey;
    }

    public int getPreviousKey() {
        return previousKey;
    }

    public void setPreviousKey(int previousKey) {
        this.previousKey = previousKey;
    }

    @Override
    public String toString() {
        return String.format("%d/%d", getCurrent(), getLength());
    }
}
