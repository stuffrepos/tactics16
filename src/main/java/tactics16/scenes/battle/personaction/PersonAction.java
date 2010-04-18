package tactics16.scenes.battle.personaction;

import tactics16.scenes.battle.*;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.util.MultiLinearMoviment;
import tactics16.components.VisualMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonAction {

    private Person person;
    private Coordinate terrainTarget;
    private MultiLinearMoviment moviment;    
    private final VisualMap visualMap;    

    public PersonAction(Person person, VisualMap visualMap, Coordinate terrainTarget) {
        this.person = person;
        this.visualMap = visualMap;
        this.terrainTarget = terrainTarget;        
        this.person.setCurrentGameAction(GameAction.ATTACKING);
    }
}
