package net.stuffrepos.tactics16.scenes.battle.effects;

import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.animation.EntitiesSequence;
import net.stuffrepos.tactics16.animation.TemporaryAnimation;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.PersonInfo;
import net.stuffrepos.tactics16.scenes.battle.VisualBattleMap;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectAnimation extends EntitiesSequence {

    public EffectAnimation(
            VisualBattleMap visualBattleMap,
            PerformedActionNotify event) {
        super(effectLayer(visualBattleMap, event),
                resultLayer(visualBattleMap, event));
    }

    private static EntityBuilder effectLayer(final VisualBattleMap visualBattleMap, final PerformedActionNotify event) {
        return new EntityBuilder() {
            public VisualEntity buildEntity() {
                EntitiesLayer layer = new EntitiesLayer();
                boolean first = true;
                visualBattleMap.getBattleGame().getPerson(event.getAgentPerson()).getGameActionControl().back();

                for (Integer affectedPerson : event.getAffectedPersons()) {
                    TemporaryAnimation effect = new TemporaryAnimation(
                            visualBattleMap.getBattleGame().getPerson(event.getAgentPerson()).getJob().getSpriteActionGroup().getSpriteAction(
                            GameAction.EFFECT),
                            1);
                    effect.getPosition().set(visualBattleMap.getBattleGame().getPerson(affectedPerson).getPosition());
                    if (first) {
                        first = false;
                    } else {
                        effect.update(-(int) (Math.random() * 250));
                    }
                    layer.addEntity(effect);

                    if (event.getAffectedPersonResult(affectedPerson).isHits()) {
                        visualBattleMap.getBattleGame().getPerson(affectedPerson).getGameActionControl().advance(GameAction.DAMAGED);
                        layer.addEntity(new PersonInfo(
                                visualBattleMap.getBattleGame().getPerson(affectedPerson),
                                PersonInfo.Type.DAMAGE,
                                String.format("-%d", event.getAffectedPersonResult(affectedPerson).getDamage())));
                    } else {
                        layer.addEntity(new PersonInfo(
                                visualBattleMap.getBattleGame().getPerson(affectedPerson),
                                PersonInfo.Type.NEUTRAL,
                                "Missed"));
                    }

                }
                
                if (event.getAction().getHealthPointsCost() > 0) {
                    layer.addEntity(new PersonInfo(
                                visualBattleMap.getBattleGame().getPerson(event.getAgentPerson()),
                                PersonInfo.Type.DAMAGE,
                                String.format("-%d", event.getAction().getHealthPointsCost())));
                }

                return layer;
            }
        };

    }

    private static EntityBuilder resultLayer(final VisualBattleMap visualBattleMap, final PerformedActionNotify event) {
        return new EntityBuilder() {
            public VisualEntity buildEntity() {
                EntitiesLayer layer = new EntitiesLayer();

                for (final Person person : visualBattleMap.getBattleGame().getPersons()) {
                    if (person.getEnginePerson().isAlive()) {
                        person.getGameActionControl().back();
                    } else {
                        person.getGameActionControl().advance(GameAction.DEADING);
                        layer.addEntity(new VisualEntity() {
                            public void update(int delta) {
                            }

                            public void render(Graphics g) {
                            }

                            public boolean isFinalized() {
                                return person.isFinalized();
                            }
                        });
                    }
                }

                return layer;
            }
        };

    }
}
