package tactics16.scenes.battle.effects;

import java.awt.Graphics2D;
import java.util.Map;
import tactics16.animation.EntitiesLayer;
import tactics16.animation.TemporaryAnimation;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.scenes.battle.BattleAction;
import tactics16.scenes.battle.Person;
import tactics16.scenes.battle.VisualBattleMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectAnimation {

    private EntitiesLayer effects;
    private final BattleAction battleAction;

    public EffectAnimation(
            VisualBattleMap visualBattleMap,
            BattleAction battleAction,
            Map<Person, EvadeSelector.Response> personsEvaded) {
        this.battleAction = battleAction;
        this.battleAction.getAgent().setCurrentGameAction(GameAction.STOPPED);
        effects = new EntitiesLayer();
        boolean first = true;

        for (Coordinate rayTarget : this.battleAction.getRayTargets()) {
            TemporaryAnimation effect = new TemporaryAnimation(
                    this.battleAction.getAgent().getJob().getSpriteActionGroup().getSpriteAction(
                    GameAction.EFFECT),
                    1);
            effect.getPosition().set(visualBattleMap.getVisualMap().getTerrainPosition(rayTarget));
            if (first) {
                first = false;
            } else {
                effect.update(-(long) (Math.random() * 250));
            }
            effects.addEntity(effect);
            Person person = visualBattleMap.getBattleGame().getPersonOnMapPosition(rayTarget);
            if (person != null && EvadeSelector.Response.NO.equals(personsEvaded.get(person))) {
                person.setCurrentGameAction(GameAction.DAMAGED);
            }
        }
    }

    public void update(long elapsedTime) {
        effects.update(elapsedTime);

        if (isFinalized()) {
            for (Person person : battleAction.getPersonsTargets()) {
                person.setCurrentGameAction(GameAction.STOPPED);
            }
        }
    }

    public void render(Graphics2D g) {
        effects.render(g);
    }

    public boolean isFinalized() {
        return effects.isFinalized();
    }
}
