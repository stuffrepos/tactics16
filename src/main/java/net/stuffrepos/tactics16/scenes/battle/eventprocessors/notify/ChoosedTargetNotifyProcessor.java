package net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify;

import net.stuffrepos.tactics16.battleengine.events.ChoosedTarget;
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
public class ChoosedTargetNotifyProcessor extends EventProcessor<ChoosedTarget> {

    public ChoosedTargetNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ChoosedTarget event) {
        return new Phase() {

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                getScene().stopCurrentEventPhase();
            }

        };
    }

    public static Class getObjectClass() {
        return ChoosedTarget.class;
    }
}
