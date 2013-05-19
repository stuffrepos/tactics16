/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battleengine.events;

import net.stuffrepos.tactics16.battleengine.BattleNotify;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectedPersonNotify implements BattleNotify {
    final Integer selectedPerson;

    public SelectedPersonNotify(Integer selectedPerson) {
        this.selectedPerson = selectedPerson;
    }
    
}
