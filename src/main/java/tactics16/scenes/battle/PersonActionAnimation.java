package tactics16.scenes.battle;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import tactics16.animation.EntitiesLayer;
import tactics16.animation.TemporaryAnimation;
import tactics16.game.Action;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionAnimation {

    private EntitiesLayer effects;
    private List<Person> personsTargets;
    private Person selectedPerson;
    private boolean personWasAnimated = false;
    private final List<Coordinate> targets;
    private final VisualBattleMap visualBattleMap;

    public PersonActionAnimation(
            VisualBattleMap visualBattleMap,
            Person selectedPerson,
            Action selectedAction,
            Coordinate target) {
        this.visualBattleMap = visualBattleMap;
        this.selectedPerson = selectedPerson;
        this.selectedPerson.setCurrentGameAction(GameAction.ATTACKING);
        this.selectedPerson.setSide(target.getX() - selectedPerson.getMapPosition().getX());
        this.targets = this.visualBattleMap.getBattleGame().calculateTargetActionRayArea(
                target, selectedAction.getReach().getRay());

        effects = new EntitiesLayer();
        personsTargets = new LinkedList<Person>();
        boolean first = true;
        for (Coordinate rayTarget : targets) {
            TemporaryAnimation effect = new TemporaryAnimation(
                    selectedPerson.getJob().getSpriteActionGroup().getSpriteAction(
                    GameAction.EFFECT),
                    1);
            effect.getPosition().set(visualBattleMap.getVisualMap().getTerrainPosition(rayTarget));
            if (first) {
                first = false;
            } else {
                effect.update(-(long) (Math.random() * 250));
            }
            effects.addEntity(effect);

        }
    }

    public void update(long elapsedTime) {
        if (personWasAnimated) {
            effects.update(elapsedTime);
        } else {
            if (selectedPerson.getAnimationLoopCount() >= 1) {
                personWasAnimated = true;
                selectedPerson.setCurrentGameAction(GameAction.STOPPED);
                // Persons Targets
                for (Coordinate target : targets) {
                    Person person = visualBattleMap.getBattleGame().getPersonOnMapPosition(target);
                    if (person != null) {
                        person.setCurrentGameAction(GameAction.DAMAGED);
                        personsTargets.add(person);
                    }
                }
            }
        }

        if (isFinalized()) {
            for (Person person : personsTargets) {
                person.setCurrentGameAction(GameAction.STOPPED);
            }
        }
    }

    public void render(Graphics2D g) {
        if (personWasAnimated) {
            effects.render(g);
        }
    }

    public boolean isFinalized() {
        return effects.isFinalized();
    }
}
