package net.stuffrepos.tactics16.battlegameengine;

import java.util.Collection;
import java.util.List;
import net.stuffrepos.tactics16.battlegameengine.Map.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Monitor<PersonId, PlayerId> {

    public void notifyNewTurn(int currentTurn);

    public void notifyPersonsActOrderDefined(List<PersonId> persons);

    public void notifySelectedPerson(PersonId selectedPerson);

    public Coordinate requestMovimentTarget(PersonId person, Map map,
            Coordinate originalPosition,
            Collection<Coordinate> movimentRange);

    public void notifyOutOfReachMoviment(PersonId person, Coordinate coordinate);

    public void notifyPersonMoved(PersonId person,
            Coordinate originalPosition,
            Coordinate movimentTarget);

    public Action requestPersonAction(
            PersonId person,
            Map map,
            Coordinate personPosition,
            java.util.Map<Action, Boolean> classifyPersonActions);

    public void notifyMovimentCancelled(PersonId person,
            Coordinate originalPosition,
            Coordinate movimentTarget);

    public void notifySelectedAction(PersonId person, Action selectedAction);

    public void notifyNotEnabledAction(PersonId person, Action selectedAction);

    public void notifyChooseActionCancelled(
            PersonId person,
            Action selectedAction);

    public void notifyChoosedTarget(PersonId person, Coordinate actionTarget);

    public void notifyOutOfReach(PersonId person, Action selectedAction);

    public Coordinate requestActionTarget(
            PersonId person,
            Map map,
            Coordinate get,
            Action selectedAction,
            Collection<Coordinate> actionRange);

    public boolean requestActConfirm(PersonId person, Action selectedAction,
            Coordinate actionTarget,
            Collection<Coordinate> actRay, Collection<PersonId> affectedPersons);

    public void notifyConfirmCancelled(PersonId person, Action selectedAction,
            Coordinate actionTarget);

    public void notifyPerformedAction(
            PersonId agentPerson,
            Action action,
            Coordinate target,
            Collection<Coordinate> buildActionReachRay,
            Collection<PersonId> affectedPersons,
            int agentLostSpecialPoints,
            int agentLostHealthPoints);

    public void notifyActionAffectedPerson(
            PersonId affectedPerson,
            boolean hits,
            int damage,
            boolean affectedPersonIsAlive);

    public void notifyWiningPlayer(PlayerId winnerPlayer);
}
