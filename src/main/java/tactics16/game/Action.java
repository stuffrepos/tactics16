package tactics16.game;

import tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Action implements Nameable {
    
    private Integer power;
    private String name;
    private Reach reach;
    private Integer agility;

    public Action(String name) {
        this.name = name;
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

    public Reach getReach() {
        return reach;
    }

    public void setReach(Reach reach) {
        this.reach = reach;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }
}
