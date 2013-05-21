package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.Nameable;
import java.util.Set;
import net.stuffrepos.tactics16.battleengine.Action;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Job extends Nameable {

    public int getEvasiveness();

    public int getResistence();

    public int getMoviment();

    public float getSpeed();

    public Set<Action> getActions();

    public JobSpriteActionGroup getSpriteActionGroup();

    public int getMaximumSpecialPoints();

    public int getMaximumHealthPoints();

    public enum GameAction {

        STOPPED,
        WALKING,
        DAMAGED,
        ATTACKING,
        SELECTED,
        EFFECT,
        ON_ATTACKING,
        USED,
        DEADING
    }
}
