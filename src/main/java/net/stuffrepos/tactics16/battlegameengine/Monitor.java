package net.stuffrepos.tactics16.battlegameengine;

import java.util.Collection;
import java.util.List;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Monitor {

    public void notifyNewTurn(int currentTurn);

    public void notifyPersonsActOrderDefined(List<Integer> persons);

    public void notifySelectedPerson(Integer selectedPerson);

    public MapCoordinate requestMovimentTarget(Integer person, Map map,
            MapCoordinate originalPosition,
            Collection<MapCoordinate> movimentRange);

    public void notifyOutOfReachMoviment(Integer person, MapCoordinate coordinate);

    public void notifyPersonMoved(Integer person,
            MapCoordinate originalPosition,
            MapCoordinate movimentTarget);

    public Action requestPersonAction(
            Integer person,
            Map map,
            MapCoordinate personPosition,
            java.util.Map<Action, Boolean> classifyPersonActions);

    public void notifyMovimentCancelled(Integer person,
            MapCoordinate originalPosition,
            MapCoordinate movimentTarget);

    public void notifySelectedAction(Integer person, Action selectedAction);

    public void notifyNotEnabledAction(Integer person, Action selectedAction);

    public void notifyChooseActionCancelled(
            Integer person,
            Action selectedAction);

    public void notifyChoosedTarget(Integer person, MapCoordinate actionTarget);

    public void notifyOutOfReach(Integer person, Action selectedAction);

    public MapCoordinate requestActionTarget(
            Integer person,
            Map map,
            MapCoordinate get,
            Action selectedAction,
            Collection<MapCoordinate> actionRange);

    public boolean requestActConfirm(Integer person, Action selectedAction,
            MapCoordinate actionTarget,
            Collection<MapCoordinate> actRay, Collection<Integer> affectedPersons);

    public void notifyConfirmCancelled(Integer person, Action selectedAction,
            MapCoordinate actionTarget);

    public void notifyPerformedAction(
            Integer agentPerson,
            Action action,
            MapCoordinate target,
            Collection<MapCoordinate> buildActionReachRay,
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
