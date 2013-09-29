package net.stuffrepos.tactics16.scenes.battle.eventprocessors.request.cpu;

import net.stuffrepos.tactics16.battleengine.events.ActConfirmRequest;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.RequestProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActConfirmRequestProcessor extends RequestProcessor<ActConfirmRequest, Boolean> {

    public ActConfirmRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ActConfirmRequest event) {
        return new Phase() {
            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                answer(true);
            }
        };
    }

    @Override
    public Class getEventClass() {
        return ActConfirmRequest.class;
    }
}
