package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.math.Interval;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Reach {

    private final Interval distance = new Interval(1);
    private final Interval ray = new Interval(0);
    private boolean clearTrajetory = true;

    public boolean isClearTrajetory() {
        return clearTrajetory;
    }

    public void setClearTrajetory(boolean clearTrajetory) {
        this.clearTrajetory = clearTrajetory;
    }

    public Interval getDistance() {
        return distance;
    }

    public Interval getRay() {
        return ray;
    }
}
