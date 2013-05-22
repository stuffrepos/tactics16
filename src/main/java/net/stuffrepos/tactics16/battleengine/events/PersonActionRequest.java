package net.stuffrepos.tactics16.battleengine.events;


import java.util.SortedMap;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionRequest implements BattleRequest<BattleEngine.SelectedAction> {

    private final Integer person;
    private final Map map;
    private final MapCoordinate position;
    private final SortedMap<Action, Boolean> classifyPersonActions;

    public PersonActionRequest(Integer personId, Map map, MapCoordinate position,
            SortedMap<Action, Boolean> classifyPersonActions) {
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

    public SortedMap<Action, Boolean> getClassifyPersonActions() {
        return classifyPersonActions;
    }
}
