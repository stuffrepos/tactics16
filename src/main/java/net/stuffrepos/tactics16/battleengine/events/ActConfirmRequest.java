package net.stuffrepos.tactics16.battleengine.events;

import java.util.Collection;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.EnginePerson;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActConfirmRequest implements BattleRequest<Boolean> {

    private final EnginePerson person;
    private final Action selectedAction;
    private final MapCoordinate actionTarget;
    private final Collection<MapCoordinate> actRay;
    private final Collection<Integer> findAffectedActionPersons;

    public ActConfirmRequest(EnginePerson person, Action selectedAction,
            MapCoordinate actionTarget, Collection<MapCoordinate> actRay,
            Collection<Integer> findAffectedActionPersons) {
        this.person = person;
        this.selectedAction = selectedAction;
        this.actionTarget = actionTarget;
        this.actRay = actRay;
        this.findAffectedActionPersons = findAffectedActionPersons;
    }

    public EnginePerson getPerson() {
        return person;
    }

    public Action getSelectedAction() {
        return selectedAction;
    }

    public MapCoordinate getActionTarget() {
        return actionTarget;
    }

    public Collection<MapCoordinate> getActRay() {
        return actRay;
    }

    public Collection<Integer> getFindAffectedActionPersons() {
        return findAffectedActionPersons;
    }

    public int getPlayer() {
        return person.getPlayerId();
    }
}
