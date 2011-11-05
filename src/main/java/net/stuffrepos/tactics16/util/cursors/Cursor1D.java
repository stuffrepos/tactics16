package net.stuffrepos.tactics16.util.cursors;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Cursor1D {

    private int length = Integer.MIN_VALUE;
    private int current = Integer.MAX_VALUE;
    private int lastCurrent;
    private int lastMove;
    private GameKey nextKey, previousKey;
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

    public void setKeys(GameKey previousKey, GameKey nextKey) {
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
        return moveTo(this.current + d);
    }

    public boolean update(long elapsedTime) {
        if (getNextKey() != null && MyGame.getInstance().isKeyPressed(getNextKey())) {
            return moveNext();
        } else if (getPreviousKey() != null && MyGame.getInstance().isKeyPressed(getPreviousKey())) {
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

    public GameKey getNextKey() {
        return nextKey;
    }

    public void setNextKey(GameKey nextKey) {
        this.nextKey = nextKey;
    }

    public GameKey getPreviousKey() {
        return previousKey;
    }

    public void setPreviousKey(GameKey previousKey) {
        this.previousKey = previousKey;
    }

    @Override
    public String toString() {
        return String.format("%d/%d", getCurrent(), getLength());
    }

    public boolean moveTo(int p) {
        int tempCurrent;
        if (length > 0) {
            tempCurrent = p;
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
}
