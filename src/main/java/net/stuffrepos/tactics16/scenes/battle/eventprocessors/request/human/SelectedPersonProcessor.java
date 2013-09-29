package net.stuffrepos.tactics16.scenes.battle.eventprocessors.request.human;

import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.SelectedPersonNotify;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectedPersonProcessor extends EventProcessor<SelectedPersonNotify> {

    public SelectedPersonProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(SelectedPersonNotify event) {
        return null;
    }

    @Override
    public Class getEventClass() {
        return SelectedPersonNotify.class;
    }
    
}
