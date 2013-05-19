package net.stuffrepos.tactics16.battleengine;

import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Action extends Nameable {

    public int costSpecialPoints();
    
    public int getPower();

    public Reach getReach();

    public int getAccuracy();
}
