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
public class MovimentCancelledNotify implements BattleNotify {
    private final Integer person;
    private final MapCoordinate movimentTarget;
    private final MapCoordinate originalPosition;

    public MovimentCancelledNotify(Integer person, Map.MapCoordinate originalPosition, Map.MapCoordinate movimentTarget) {
        this.person = person;
        this.movimentTarget = movimentTarget;
        this.originalPosition = originalPosition;
    }

    public Integer getPerson() {
        return person;
    }

    public MapCoordinate getMovimentTarget() {
        return movimentTarget;
    }

    public MapCoordinate getOriginalPosition() {
        return originalPosition;
    }
    
}
