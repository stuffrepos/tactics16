package net.stuffrepos.tactics16.battlegameengine;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Reach extends Cloneable {

    public Integer getMinimum();

    public Integer getMaximum();

    public Integer getRay();

    public boolean getDirect();

    public Reach clone();
}
