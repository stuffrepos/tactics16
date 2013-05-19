/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battlegameengine;

import java.util.Collection;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActConfirmRequest implements BattleRequest<Boolean> {

    private final Integer person;
    private final Action selectedAction;
    private final MapCoordinate actionTarget;
    private final Collection<MapCoordinate> actRay;
    private final Collection<Integer> findAffectedActionPersons;

    public ActConfirmRequest(Integer person, Action selectedAction,
            MapCoordinate actionTarget, Collection<MapCoordinate> actRay,
            Collection<Integer> findAffectedActionPersons) {
        this.person = person;
        this.selectedAction = selectedAction;
        this.actionTarget = actionTarget;
        this.actRay = actRay;
        this.findAffectedActionPersons = findAffectedActionPersons;
    }

    public Integer getPerson() {
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
}
