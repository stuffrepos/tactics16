package net.stuffrepos.tactics16.scenes.battle.personaction;

import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.scenes.battle.Person;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionAnimation implements VisualEntity {

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

    public void update(int delta) {
    }

    public void render(Graphics g) {
        //Do nothing
    }
}
