package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.Nameable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Job extends Nameable {

    public int getEvasiveness();

    public int getResistence();

    public int getMoviment();

    public Set<Action> getActions();

    public JobSpriteActionGroup getSpriteActionGroup();

    public enum GameAction {

        STOPPED,
        WALKING,
        DAMAGED,
        ATTACKING,
        SELECTED,
        EFFECT,
        ON_ATTACKING,
        USED
    }
}
