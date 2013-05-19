/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battlegameengine.events;

import net.stuffrepos.tactics16.battlegameengine.BattleNotify;
import net.stuffrepos.tactics16.battlegameengine.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class OutOfReachMoviment implements BattleNotify {
    final Map.MapCoordinate coordinate;
    final Integer person;

    public OutOfReachMoviment(Integer person, Map.MapCoordinate coordinate) {
        this.person = person;
        this.coordinate = coordinate;
    }
    
}
