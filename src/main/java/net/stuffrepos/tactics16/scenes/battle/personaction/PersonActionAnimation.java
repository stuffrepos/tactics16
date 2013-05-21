package net.stuffrepos.tactics16.scenes.battle.personaction;

import java.util.Set;
import net.stuffrepos.tactics16.battleengine.Action;

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

    public PersonActionAnimation(            
            Person selectedPerson,
            Action selectedAction,
            Coordinate target) {
        this.selectedPerson = selectedPerson;
        this.selectedPerson.getGameActionControl().advance(GameAction.ATTACKING);
        this.selectedPerson.setSide(target.getX() - selectedPerson.getEnginePerson().getPosition().getX());        
    }

    public boolean isFinalized() {
        return selectedPerson.getAnimationLoopCount() >= 1;
    }
}
