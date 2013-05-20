package net.stuffrepos.tactics16.battleengine.events;

import java.util.Collection;
import java.util.Map;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.BattleNotify;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class NewTurn implements BattleNotify {
    private final int currentTurn;
    private final Collection<Map.Entry<Integer, BattleEngine.ValueChanged<Float>>> personsSpeeds;

    public NewTurn(int currentTurn, Collection<Map.Entry<Integer, BattleEngine.ValueChanged<Float>>> personsSpeeds) {
        this.currentTurn = currentTurn;
        this.personsSpeeds = personsSpeeds;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Collection<Map.Entry<Integer, BattleEngine.ValueChanged<Float>>> getPersonsSpeeds() {
        return personsSpeeds;
    }
    
}
