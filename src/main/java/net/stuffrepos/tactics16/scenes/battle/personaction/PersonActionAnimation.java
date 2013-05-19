package net.stuffrepos.tactics16.scenes.battle.personaction;

import java.util.Set;
import net.stuffrepos.tactics16.battlegameengine.Action;

import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.VisualBattleMap;
import net.stuffrepos.tactics16.util.math.Interval;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionAnimation {

    private Person selectedPerson;
    private final Set<Coordinate> targets;
    private final VisualBattleMap visualBattleMap;

    public PersonActionAnimation(
            VisualBattleMap visualBattleMap,
            Person selectedPerson,
            Action selectedAction,
            Coordinate target) {
        this.visualBattleMap = visualBattleMap;
        this.selectedPerson = selectedPerson;
        this.selectedPerson.getGameActionControl().advance(GameAction.ATTACKING);
        this.selectedPerson.setSide(target.getX() - selectedPerson.getMapPosition().getX());
        this.targets = this.visualBattleMap.getBattleGame().calculateTargetActionRayArea(
                target, new Interval(0, selectedAction.getReach().getRay()));
    }

    public boolean isFinalized() {
        return selectedPerson.getAnimationLoopCount() >= 1;
    }
}
