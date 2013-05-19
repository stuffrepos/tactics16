package net.stuffrepos.tactics16.scenes.battle.effects;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.animation.TemporaryAnimation;
import net.stuffrepos.tactics16.battleengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.scenes.battle.PersonInfo;
import net.stuffrepos.tactics16.scenes.battle.VisualBattleMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectAnimation {

    private EntitiesLayer effects;
    private EntitiesLayer info;
    private PerformedActionNotify event;
    private final VisualBattleMap visualBattleMap;

    public EffectAnimation(
            VisualBattleMap visualBattleMap,
            PerformedActionNotify event) {
        
        this.effects = new EntitiesLayer();
        this.event = event;
        this.visualBattleMap = visualBattleMap;
        
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
            effects.addEntity(effect);

            if (event.getAffectedPersonResult(affectedPerson).isHits()) {
                visualBattleMap.getBattleGame().getPerson(affectedPerson).getGameActionControl().advance(GameAction.DAMAGED);
            }
        }
    }

    public void update(int delta) {
        if (info == null) {
            effects.update(delta);
        } else {
            info.update(delta);
        }

        if (effects.isFinalized() && info == null) {
            info = new EntitiesLayer();
            for (int affectedPerson : event.getAffectedPersons()) {
                visualBattleMap.getBattleGame().getPerson(affectedPerson).getGameActionControl().back();

                PersonInfo.Type type;
                String value;

                if (event.getAffectedPersonResult(affectedPerson).isHits()) {
                    type = PersonInfo.Type.DAMAGE;
                    value = String.format("-%d HP", event.getAffectedPersonResult(affectedPerson).getDamage());
                } else {
                    type = PersonInfo.Type.NEUTRAL;
                    value = "Missed";
                }

                info.addEntity(new PersonInfo(
                        visualBattleMap.getBattleGame().getPerson(affectedPerson),
                        type, value));
            }
        }
    }

    public void render(Graphics g) {
        if (info == null) {
            effects.render(g);
        } else {
            info.render(g);
        }
    }

    public boolean isFinalized() {
        if (info == null) {
            return false;
        } else {
            return info.isFinalized();
        }
    }
}
