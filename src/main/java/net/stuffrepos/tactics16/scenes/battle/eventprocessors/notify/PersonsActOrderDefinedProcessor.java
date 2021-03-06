package net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify;

import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.PersonsActOrderDefined;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonsActOrderDefinedProcessor extends EventProcessor<PersonsActOrderDefined> {    

    public PersonsActOrderDefinedProcessor(final BattleScene outer) {
        super(outer);
    }
    
    public Phase init(PersonsActOrderDefined event) {
        return null;
    }
    
    public static Class getObjectClass() {
        return PersonsActOrderDefined.class;
    }
}
