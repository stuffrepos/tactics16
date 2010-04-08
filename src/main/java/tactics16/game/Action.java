package tactics16.game;

import tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Action implements Nameable {

    private ActionType type;
    private Integer power;
    private String name;
    private Integer dificulty;
    private Reach reach;

    public Action(String name) {
        this.name = name;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDificulty() {
        return dificulty;
    }

    public void setDificulty(Integer dificulty) {
        this.dificulty = dificulty;
    }

    public Reach getReach() {
        return reach;
    }

    public void setReach(Reach reach) {
        this.reach = reach;
    }
}
