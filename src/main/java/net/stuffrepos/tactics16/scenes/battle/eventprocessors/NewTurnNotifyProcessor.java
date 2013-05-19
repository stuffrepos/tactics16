package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.battlegameengine.events.NewTurn;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class NewTurnNotifyProcessor extends EventProcessor<NewTurn> {

    public NewTurnNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final NewTurn event) {
        return new Phase() {
            MessageBox messageBox = new MessageBox("Turn " + event.getCurrentTurn(), getScene().getVisualBattleMap().getVisualMap(), 1000);
            private long time;

            @Override
            public void init(GameContainer container, StateBasedGame game) throws SlickException {
                super.init(container, game);
                this.time = 0;
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                messageBox.update(delta);
                this.time += delta;
                if (this.time > 1000) {
                    getScene().stopCurrentEventPhase();
                }
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                super.render(container, game, g);
                messageBox.render(g);
            }
        };
    }

    @Override
    public Class getEventClass() {
        return NewTurn.class;
    }
}
