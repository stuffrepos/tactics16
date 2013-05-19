package net.stuffrepos.tactics16.battlegameengine.events;


import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.battlegameengine.BattleRequest;
import net.stuffrepos.tactics16.battlegameengine.Map;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionRequest implements BattleRequest<Action> {

    private final Integer person;
    private final Map map;
    private final MapCoordinate position;
    private final java.util.Map<Action, Boolean> classifyPersonActions;

    public PersonActionRequest(Integer personId, Map map, MapCoordinate position,
            java.util.Map<Action, Boolean> classifyPersonActions) {
        this.person = personId;
        this.map = map;
        this.position = position;
        this.classifyPersonActions = classifyPersonActions;
    }

    public int getPerson() {
        return person;
    }

    public Map getMap() {
        return map;
    }

    public MapCoordinate getPosition() {
        return position;
    }

    public java.util.Map<Action, Boolean> getClassifyPersonActions() {
        return classifyPersonActions;
    }
}
