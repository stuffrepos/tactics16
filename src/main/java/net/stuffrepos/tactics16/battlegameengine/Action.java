package net.stuffrepos.tactics16.battlegameengine;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Action extends Cloneable {

    public int costSpecialPoints();
    
    public int getPower();

    public Reach getReach();

    public Action clone();

    public int getAccuracy();
}
