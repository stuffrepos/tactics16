package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import java.util.Map.Entry;
import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.events.NewTurn;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.PersonInfo;
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
            private MessageBox messageBox = new MessageBox("Turn " + event.getCurrentTurn(), getScene().getVisualBattleMap().getVisualMap(), 1000);
            private EntitiesLayer personsSpeedsInfos;
            private long time;

            @Override
            public void init(GameContainer container, StateBasedGame game) throws SlickException {
                super.init(container, game);
                this.time = 0;
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                if (personsSpeedsInfos == null) {
                    messageBox.update(delta);
                    this.time += delta;
                    if (this.time > 1000) {
                        personsSpeedsInfos = new EntitiesLayer();
                        for (Entry<Integer, BattleEngine.ValueChanged<Float>> personSpeed : event.getPersonsSpeeds()) {
                            personsSpeedsInfos.addEntity(
                                    new PersonInfo(
                                    getScene().getVisualBattleMap().getBattleGame().getPerson(personSpeed.getKey()), 
                                    PersonInfo.Type.SPEED, 
                                    String.format("%.1f", personSpeed.getValue().getNewValue())));
                        }
                    }
                }
                else {
                    personsSpeedsInfos.update(delta);
                    
                    if (personsSpeedsInfos.isFinalized()) {
                        getScene().stopCurrentEventPhase();
                    }
                }
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                super.render(container, game, g);
                if (personsSpeedsInfos == null) {
                    messageBox.render(g);
                }
                else {
                    personsSpeedsInfos.render(g);
                }
                
            }
        };
    }

    @Override
    public Class getEventClass() {
        return NewTurn.class;
    }
}
