package net.stuffrepos.tactics16.battleengine;

import java.util.Collection;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Person {

    public int getEvasiveness();

    public int getResistence();

    public int getMoviment();

    public int getMaximumHealthPoints();

    public int getMaximumSpecialPoints();

    public Collection<Action> getActions();

    public float getSpeed();
}
