package net.stuffrepos.tactics16.scenes.battle;

import java.util.Set;
import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.math.Interval;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleAction {

    private Person agent;
    private Action action;
    private Coordinate target;
    private Integer agentAgilityPoints;
    private BattleGame battleGame;

    public BattleAction(
            BattleGame battleGame, Person agent, Action action,
            Coordinate target) {
        this.agent = agent;
        this.action = action;
        this.target = target;
        this.agentAgilityPoints = 0;
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

    public Integer getAgentAgilityPoints() {
        return agentAgilityPoints;
    }

    public Set<Person> getPersonsTargets() {
        return battleGame.getPersonsOnMapPositions(
                battleGame.calculateTargetActionRayArea(
                target,
                new Interval(0,action.getReach().getRay())));
    }

    public Set<Coordinate> getRayTargets() {
        return battleGame.calculateTargetActionRayArea(target, new Interval(0, action.getReach().getRay()));
    }

    public Integer agilityPointsNeededToEvade(Person target) {
        return null;
    }

    public int calculateDamage(Person person) {
        return Math.max(0, getAction().getPower() - person.getDefense());
    }
}
