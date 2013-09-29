/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battleengine.events;

import java.util.Collection;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleNotify;
import net.stuffrepos.tactics16.battleengine.EnginePerson;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class OutOfReachNotify implements BattleNotify {

    private final MapCoordinate targetPosition;
    private final Collection<MapCoordinate> range;
    private final Action action;
    private final EnginePerson person;

    public OutOfReachNotify(EnginePerson person, Action action, Collection<MapCoordinate> range, MapCoordinate targetPosition) {
        this.person = person;
        this.action = action;
        this.range = range;
        this.targetPosition = targetPosition;
    }

    public MapCoordinate getTargetPosition() {
        return targetPosition;
    }

    public Collection<MapCoordinate> getRange() {
        return range;
    }

    public Action getAction() {
        return action;
    }

    public EnginePerson getPerson() {
        return person;
    }
}
