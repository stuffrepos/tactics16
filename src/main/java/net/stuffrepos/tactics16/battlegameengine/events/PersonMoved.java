/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battlegameengine.events;

import net.stuffrepos.tactics16.battlegameengine.BattleNotify;
import net.stuffrepos.tactics16.battlegameengine.Map;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonMoved implements BattleNotify {
    private final Integer person;
    private final MapCoordinate originalPosition;
    private final MapCoordinate movimentTarget;

    public PersonMoved(Integer person, Map.MapCoordinate originalPosition, Map.MapCoordinate movimentTarget) {
        this.person = person;
        this.originalPosition = originalPosition;
        this.movimentTarget = movimentTarget;
    }

    public Integer getPerson() {
        return person;
    }

    public MapCoordinate getOriginalPosition() {
        return originalPosition;
    }

    public MapCoordinate getMovimentTarget() {
        return movimentTarget;
    }
    
}
