package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Action implements Nameable, net.stuffrepos.tactics16.battlegameengine.Action {
    
    private Integer power;
    private String name;
    private Reach reach;    
    private int accuracy;
    private int costSpecialPoints;

    public Action(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public net.stuffrepos.tactics16.battlegameengine.Reach getReach() {
        return reach;
    }

    public void setReach(Reach reach) {
        this.reach = reach;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public int costSpecialPoints() {
        return costSpecialPoints;
    }

    public int getAccuracy() {
        return this.accuracy;
    }
}
