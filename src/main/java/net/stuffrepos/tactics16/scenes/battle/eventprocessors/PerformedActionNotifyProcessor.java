package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.animation.EntitiesSequence;
import net.stuffrepos.tactics16.animation.EntitiesSequence.EntityBuilder;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
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
            EntitiesSequence sequence;
            
            @Override
            public void enter(GameContainer container, StateBasedGame game) throws SlickException {
                super.enter(container, game);
                if (event.getAction() != null) {
                    sequence = new EntitiesSequence(
                            buildActionAnimation(),
                            buildEffectAnimation());
                } else {
                    sequence = new EntitiesSequence();
                }
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);

                sequence.update(delta);

                if (sequence.isFinalized()) {
                    getScene().stopCurrentEventPhase();
                }

            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                super.render(container, game, g);
                sequence.render(g);
            }

            private EntityBuilder buildActionAnimation() {
                return new EntityBuilder() {
                    public VisualEntity buildEntity() {
                        return new PersonActionAnimation(
                                getScene().getVisualBattleMap().getBattleGame().getPerson(event.getAgentPerson()),
                                event.getAction(),
                                Coordinate.fromMapCoordinate(event.getTarget()));
                    }
                };
            }

            private EntityBuilder buildEffectAnimation() {
                return new EntityBuilder() {
                    public VisualEntity buildEntity() {
                        return new EffectAnimation(
                                getScene().getVisualBattleMap(),
                                event);
                    }
                };
            }
        };
    }

    @Override
    public Class getEventClass() {
        return PerformedActionNotify.class;
    }
}
