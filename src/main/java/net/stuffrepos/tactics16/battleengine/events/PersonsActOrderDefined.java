/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battleengine.events;

import java.util.List;
import net.stuffrepos.tactics16.battleengine.BattleNotify;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonsActOrderDefined implements BattleNotify {
    final List<Integer> persons;

    public PersonsActOrderDefined(List<Integer> persons) {
        this.persons = persons;
    }
    
}
