package net.stuffrepos.tactics16.battleengine;

import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Action extends Nameable, Comparable<Action> {

    public int getHealthPointsCost();
    
    public int getSpecialPointsCost();
    
    public int getPower();

    public Reach getReach();

    public int getAccuracy();
}
