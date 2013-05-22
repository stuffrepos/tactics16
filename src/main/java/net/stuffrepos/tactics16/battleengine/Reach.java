package net.stuffrepos.tactics16.battleengine;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Reach {

    public int getDistanceMin();

    public int getDistanceMax();
    
    public int getRayMin();

    public int getRayMax();

    public boolean getDirect();
}
