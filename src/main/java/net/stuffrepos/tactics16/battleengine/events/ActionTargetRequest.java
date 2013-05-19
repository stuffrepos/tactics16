package net.stuffrepos.tactics16.battleengine.events;

import java.util.Collection;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActionTargetRequest implements BattleRequest<MapCoordinate> {

    private final Integer person;
    private final MapCoordinate position;
    private final Action selectedAction;
    private final Collection<MapCoordinate> actionRange;

    public ActionTargetRequest(Integer person, Map map, MapCoordinate position, Action selectedAction, Collection<MapCoordinate> actionRange) {
        this.person = person;
        this.position = position;
        this.selectedAction = selectedAction;
        this.actionRange = actionRange;
    }

    public int getPerson() {
        return person;
    }

    public MapCoordinate getPosition() {
        return position;
    }

    public Action getSelectedAction() {
        return selectedAction;
    }

    public Collection<MapCoordinate> getActionRange() {
        return actionRange;
    }
}
