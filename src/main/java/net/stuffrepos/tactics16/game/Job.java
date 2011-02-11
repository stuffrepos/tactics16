package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.util.Nameable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Job implements Nameable {

    private String name;
    private Integer defense;
    private Integer evasiveness;
    private Set<Action> actions = new HashSet<Action>();
    private JobSpriteActionGroup spriteActionGroup = new JobSpriteActionGroup();

    public Job(String name) {
        this.name = name;
        /*
        for (GameAction gameAction : GameAction.values()) {
            job.put(gameAction, new SpriteAction());
        }*/
    }

    public Integer getEvasiveness() {
        return evasiveness;
    }

    public void setEvasiveness(Integer evasiveness) {
        this.evasiveness = evasiveness;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public JobSpriteActionGroup getSpriteActionGroup() {
        return spriteActionGroup;
    }

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
