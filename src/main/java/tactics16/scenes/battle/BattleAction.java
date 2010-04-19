package tactics16.scenes.battle;

import java.util.List;
import java.util.Set;
import tactics16.game.Action;
import tactics16.game.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleAction {

    private Person agent;
    private Action action;
    private Coordinate target;
    private int agentAgilityPoints;
    private BattleGame battleGame;

    public BattleAction(
            BattleGame battleGame, Person agent, Action action,
            Coordinate target, int agentAgilityPoints) {
        this.agent = agent;
        this.action = action;
        this.target = target;
        this.agentAgilityPoints = agentAgilityPoints;
        this.battleGame = battleGame;
    }

    public Person getAgent() {
        return agent;
    }

    public Action getAction() {
        return action;
    }

    public Coordinate getTarget() {
        return target;
    }

    public int getAgentAgilityPoints() {
        return agentAgilityPoints;
    }

    public Set<Person> getPersonsTargets() {
        return battleGame.getPersonsOnMapPositions(
                battleGame.calculateTargetActionRayArea(
                target,
                action.getReach().getRay()));
    }

    public Set<Coordinate> getRayTargets() {
        return battleGame.calculateTargetActionRayArea(target, action.getReach().getRay());
    }

    public int agilityPointsNeededToEvade(Person target) {
        return Math.max(
                0,
                getAgentAgilityPoints() + getAction().getAgility() - target.getEvasiveness());
    }

    public int calculateDamage(Person person) {
        return Math.max(0, getAction().getPower() - person.getDefense());
    }
}
