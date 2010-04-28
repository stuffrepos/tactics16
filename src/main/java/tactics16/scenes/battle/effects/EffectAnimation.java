package tactics16.scenes.battle.effects;

import java.awt.Graphics2D;
import java.util.Map;
import tactics16.animation.EntitiesLayer;
import tactics16.animation.TemporaryAnimation;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.scenes.battle.BattleAction;
import tactics16.scenes.battle.BattleActionResult;
import tactics16.scenes.battle.Person;
import tactics16.scenes.battle.PersonInfo;
import tactics16.scenes.battle.VisualBattleMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectAnimation {

    private EntitiesLayer effects;
    private EntitiesLayer info;
    private final BattleActionResult battleActionResult;

    public EffectAnimation(
            VisualBattleMap visualBattleMap,
            BattleActionResult battleActionResult) {
        this.battleActionResult = battleActionResult;
        this.battleActionResult.getAction().getAgent().getGameActionControl().back();        
        effects = new EntitiesLayer();
        boolean first = true;

        for (Coordinate rayTarget : this.battleActionResult.getAction().getRayTargets()) {
            TemporaryAnimation effect = new TemporaryAnimation(
                    this.battleActionResult.getAction().getAgent().getJob().getSpriteActionGroup().getSpriteAction(
                    GameAction.EFFECT),
                    1);
            effect.getPosition().set(visualBattleMap.getVisualMap().getPersonPosition(rayTarget));
            if (first) {
                first = false;
            } else {
                effect.update(-(long) (Math.random() * 250));
            }
            effects.addEntity(effect);
            Person person = visualBattleMap.getBattleGame().getPersonOnMapPosition(rayTarget);
            if (person != null && !battleActionResult.isPersonEvaded(person)) {
                person.getGameActionControl().advance(GameAction.DAMAGED);
            }
        }
    }

    public void update(long elapsedTime) {
        if (info == null) {
            effects.update(elapsedTime);
        } else {
            info.update(elapsedTime);
        }

        if (effects.isFinalized() && info == null) {
            info = new EntitiesLayer();
            for (Person person : battleActionResult.getAction().getPersonsTargets()) {
                person.getGameActionControl().back();

                PersonInfo.Type type;
                String value;

                if (battleActionResult.isPersonEvaded(person)) {
                    type = PersonInfo.Type.AGILITY;
                    value = String.format("-%d AP", battleActionResult.getAction().agilityPointsNeededToEvade(person));
                } else {
                    type = PersonInfo.Type.DAMAGE;
                    value = String.format("-%d HP", battleActionResult.getAction().calculateDamage(person));
                }

                info.addEntity(new PersonInfo(
                        person,
                        type, value));
            }
        }
    }

    public void render(Graphics2D g) {
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
