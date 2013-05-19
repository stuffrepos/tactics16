package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.battlegameengine.events.ConfirmCancelledNotify;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ConfirmCancelledNotifyNotifyProcessor extends EventProcessor<ConfirmCancelledNotify> {

    public ConfirmCancelledNotifyNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ConfirmCancelledNotify event) {
        return new Phase() {

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                getScene().stopCurrentEventPhase();
            }

        };
    }

    @Override
    public Class getEventClass() {
        return ConfirmCancelledNotify.class;
    }
}
