package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.battlegameengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleAction;

import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.effects.EffectAnimation;
import net.stuffrepos.tactics16.scenes.battle.personaction.PersonActionAnimation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PerformedActionNotifyProcessor extends EventProcessor<PerformedActionNotify> {

    public PerformedActionNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final PerformedActionNotify event) {
        return new Phase() {
            private PersonActionAnimation actionAnimation;
            private EffectAnimation effectAnimation;

            @Override
            public void enter(GameContainer container, StateBasedGame game) throws SlickException {
                super.enter(container, game);
                actionAnimation = new PersonActionAnimation(
                        getScene().getVisualBattleMap(),
                        getScene().getVisualBattleMap().getBattleGame().getPerson(event.getAgentPerson()),
                        event.getAction(),
                        Coordinate.fromMapCoordinate(event.getTarget()));
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);                

                if (effectAnimation != null) {
                    effectAnimation.update(delta);

                    if (effectAnimation.isFinalized()) {                        
                        getScene().stopCurrentEventPhase();
                    }
                }
                else if (actionAnimation.isFinalized()) {
                    this.effectAnimation = new EffectAnimation(
                            getScene().getVisualBattleMap(),
                            event);
                }
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                super.render(container, game, g);

                if (effectAnimation != null) {
                    effectAnimation.render(g);
                }

            }
        };
    }

    @Override
    public Class getEventClass() {
        return PerformedActionNotify.class;
    }
}
