package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.math.Interval;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Reach implements net.stuffrepos.tactics16.battleengine.Reach {

    private final Interval distance = new Interval(1);
    private int ray = 0;
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

    public Integer getMinimum() {
        return distance.getMin();
    }

    public Integer getMaximum() {
        return distance.getMax();
    }

    public Integer getRay() {
        return ray;
    }
    
    public void setRay(int ray) {
        this.ray = ray;
    }

    public boolean getDirect() {
        return clearTrajetory;
    }

    
}
