package net.stuffrepos.tactics16.battlegameengine;

import java.util.Collection;
import java.util.List;
import net.stuffrepos.tactics16.battlegameengine.Map.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Monitor {

    public void notifyNewTurn(int currentTurn);

    public void notifyPersonsActOrderDefined(List<Integer> persons);

    public void notifySelectedPerson(Integer selectedPerson);

    public Coordinate requestMovimentTarget(Integer person, Map map,
            Coordinate originalPosition,
            Collection<Coordinate> movimentRange);

    public void notifyOutOfReachMoviment(Integer person, Coordinate coordinate);

    public void notifyPersonMoved(Integer person,
            Coordinate originalPosition,
            Coordinate movimentTarget);

    public Action requestPersonAction(
            Integer person,
            Map map,
            Coordinate personPosition,
            java.util.Map<Action, Boolean> classifyPersonActions);

    public void notifyMovimentCancelled(Integer person,
            Coordinate originalPosition,
            Coordinate movimentTarget);

    public void notifySelectedAction(Integer person, Action selectedAction);

    public void notifyNotEnabledAction(Integer person, Action selectedAction);

    public void notifyChooseActionCancelled(
            Integer person,
            Action selectedAction);

    public void notifyChoosedTarget(Integer person, Coordinate actionTarget);

    public void notifyOutOfReach(Integer person, Action selectedAction);

    public Coordinate requestActionTarget(
            Integer person,
            Map map,
            Coordinate get,
            Action selectedAction,
            Collection<Coordinate> actionRange);

    public boolean requestActConfirm(Integer person, Action selectedAction,
            Coordinate actionTarget,
            Collection<Coordinate> actRay, Collection<Integer> affectedPersons);

    public void notifyConfirmCancelled(Integer person, Action selectedAction,
            Coordinate actionTarget);

    public void notifyPerformedAction(
            Integer agentPerson,
            Action action,
            Coordinate target,
            Collection<Coordinate> buildActionReachRay,
            Collection<Integer> affectedPersons,
            int agentLostSpecialPoints,
            int agentLostHealthPoints);

    public void notifyActionAffectedPerson(
            Integer affectedPerson,
            boolean hits,
            int damage,
            boolean affectedPersonIsAlive);

    public void notifyWiningPlayer(Integer winnerPlayer);
}
