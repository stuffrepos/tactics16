package tactics16.game;

import tactics16.util.Nameable;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Job implements Nameable {

    public enum GameAction {

        STOPPED,
        WALKING,
        DAMAGED,
        ATTACKING
    }
    private String name;
    private Integer defense;
    private Integer agility;
    private Set<Action> actions = new HashSet<Action>();
    private java.util.Map<GameAction, SpriteAction> spriteActions =
            new TreeMap<GameAction, SpriteAction>();

    public Job(String name) {
        this.name = name;
        for (GameAction gameAction : GameAction.values()) {
            spriteActions.put(gameAction, new SpriteAction());
        }
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public java.util.Map<GameAction, SpriteAction> getSpriteActions() {
        return spriteActions;
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
}
