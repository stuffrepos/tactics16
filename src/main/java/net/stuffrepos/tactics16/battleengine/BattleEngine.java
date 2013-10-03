package net.stuffrepos.tactics16.battleengine;

import net.stuffrepos.tactics16.battleengine.events.ActConfirmRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.events.ChooseActionCancelled;
import net.stuffrepos.tactics16.battleengine.events.ChoosedTarget;
import net.stuffrepos.tactics16.battleengine.events.ConfirmCancelledNotify;
import net.stuffrepos.tactics16.battleengine.events.MovimentCancelledNotify;
import net.stuffrepos.tactics16.battleengine.events.NewTurn;
import net.stuffrepos.tactics16.battleengine.events.NotEnabledActionNotify;
import net.stuffrepos.tactics16.battleengine.events.OutOfReachMoviment;
import net.stuffrepos.tactics16.battleengine.events.OutOfReachNotify;
import net.stuffrepos.tactics16.battleengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.battleengine.events.PersonMoved;
import net.stuffrepos.tactics16.battleengine.events.SelectedActionNotify;
import net.stuffrepos.tactics16.battleengine.events.SelectedPersonNotify;
import net.stuffrepos.tactics16.battleengine.events.WinningPlayerNotify;
import net.stuffrepos.tactics16.battleengine.events.ActionTargetRequest;
import net.stuffrepos.tactics16.battleengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.battleengine.events.PersonActionRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concentrates the battle logic.
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleEngine {

    private static final Log log = LogFactory.getLog(BattleEngine.class);
    private boolean running;
    /**
     * Speed cost for any action.
     */
    public static final float ACT_SPEED_POINTS_COST = 1.0f;
    public static final int SPECIAL_POINTS_INCREMENT_BY_END_TURN = 1;
    private State state = State.NotStarted;
    private BattleData data;

    public BattleEngine(BattleData battleData) {
        this.data = battleData;
    }

    public State getState() {
        return state;
    }

    public void run(Monitor monitor) {
        if (State.NotStarted.equals(state)) {
            state = State.Started;
            int turn = 0;
            turnLoop:
            while (true) {
                if (log.isDebugEnabled()) {
                    log.debug("New turn: " + (turn + 1));
                }

                monitor.notify(new NewTurn(++turn, newTurnRenewPersonsSpeeds()));

                Integer selectedPerson;

                while ((selectedPerson = data.getFinders().nextPersonToAct()) != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Selected person: " + selectedPerson);
                    }

                    monitor.notify(new SelectedPersonNotify(selectedPerson));
                    personActs(monitor, selectedPerson);
                    if (data.getFinders().getAlivePlayers().size() < 2) {
                        break turnLoop;
                    }
                }

                for (int person : data.getPersonSet().getPersons()) {
                    if (data.getPersonSet().getPerson(person).isAlive()) {
                        data.getPersonSet().getPerson(person).increaseSpecialPoints(SPECIAL_POINTS_INCREMENT_BY_END_TURN);
                    }
                }

            }
            monitor.notify(new WinningPlayerNotify(data.getFinders().getWinnerPlayer()));
            state = State.Finalized;
        }
    }

    public void waitRequest() {
        this.running = false;
        while (!this.running) {
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void resumeRequest() {
        this.running = true;
    }

    private Collection<Entry<Integer, ValueChanged<Float>>> newTurnRenewPersonsSpeeds() {
        java.util.Map<Integer, ValueChanged<Float>> personsSpeeds = new HashMap<Integer, ValueChanged<Float>>();

        for (int person : data.getFinders().getAlivePersons()) {
            float oldSpeedPoints = data.getPersonSet().getPerson(person).getSpeedPoints();
            data.getPersonSet().getPerson(person).renewSpeedPoints();
            personsSpeeds.put(
                    person,
                    new ValueChanged<Float>(
                    oldSpeedPoints,
                    data.getPersonSet().getPerson(person).getSpeedPoints()));
        }

        return personsSpeeds.entrySet();
    }

    private void personActs(Monitor monitor, Integer personId) {
        PersonActPhase currentPhase = PersonActPhase.Moviment;
        Action selectedAction = null;
        MapCoordinate originalPosition = data.getPersonSet().getPerson(personId).getPosition();
        MapCoordinate movimentTarget = null;
        MapCoordinate actionTarget = null;

        phaseLoop:
        while (true) {
            switch (currentPhase) {
                case Moviment:
                    Collection<MapCoordinate> movimentRange = buildMovimentRange(personId);
                    if (log.isDebugEnabled()) {
                        log.debug("Moviment range: " + mapCoordinateListToString(movimentRange));
                    }
                    movimentTarget = monitor.request(new MovimentTargetRequest(
                            data.getPersonSet().getPerson(personId),
                            data.getMap(),
                            data.getPersonSet().getPerson(personId).getPosition(),
                            movimentRange));

                    if (log.isDebugEnabled()) {
                        log.debug("Moviment position requested: " + mapCoordinateToString(movimentTarget));
                    }

                    if (movimentTarget == null) {
                        continue;
                    } else if (movimentRange.contains(movimentTarget)) {
                        data.getPersonSet().getPerson(personId).setPosition(movimentTarget);
                        monitor.notify(new PersonMoved(personId, originalPosition, movimentTarget));
                        currentPhase = PersonActPhase.ChooseAction;
                    } else {
                        monitor.notify(new OutOfReachMoviment(personId, movimentTarget));
                    }
                    continue;

                case ChooseAction:
                    SelectedAction selectedActionResult = monitor.request(new PersonActionRequest(
                            data.getPersonSet().getPerson(personId),
                            data.getMap(),
                            data.getPersonSet().getPerson(personId).getPosition(),
                            classifyPersonActions(personId)));

                    if (selectedActionResult == null) {
                        data.getPersonSet().getPerson(personId).setPosition(originalPosition);
                        monitor.notify(new MovimentCancelledNotify(personId, originalPosition, movimentTarget));
                        movimentTarget = null;
                        currentPhase = PersonActPhase.Moviment;
                    } else if (selectedActionResult.getAction() == null) {
                        monitor.notify(new SelectedActionNotify(personId, null));
                        currentPhase = PersonActPhase.Confirm;
                    } else if (data.getDefinitions().isActionEnabled(personId, selectedActionResult.getAction())) {
                        selectedAction = selectedActionResult.getAction();
                        monitor.notify(new SelectedActionNotify(personId, selectedAction));
                        currentPhase = PersonActPhase.ChooseTarget;
                    } else {
                        monitor.notify(new NotEnabledActionNotify(personId, selectedActionResult.getAction()));
                    }
                    continue;

                case ChooseTarget:
                    Collection<MapCoordinate> actionRange = buildActionRange(personId, selectedAction);
                    actionTarget = monitor.request(new ActionTargetRequest(
                            data.getPersonSet().getPerson(personId),
                            data.getMap(),
                            data.getPersonSet().getPerson(personId).getPosition(),
                            selectedAction,
                            actionRange));

                    if (actionTarget == null) {
                        monitor.notify(new ChooseActionCancelled(personId, selectedAction));
                        currentPhase = PersonActPhase.ChooseAction;
                        selectedAction = null;
                    } else if (actionRange.contains(actionTarget)) {
                        monitor.notify(new ChoosedTarget(personId, actionTarget));
                        currentPhase = PersonActPhase.Confirm;
                    } else {
                        monitor.notify(new OutOfReachNotify(
                                data.getPersonSet().getPerson(personId),
                                selectedAction,
                                actionRange,
                                actionTarget));
                    }
                    continue;

                case Confirm:
                    Collection<MapCoordinate> actRay = selectedAction == null ? null : data.getFinders().buildActionReachRay(
                            selectedAction,
                            actionTarget);
                    boolean confirm = monitor.request(new ActConfirmRequest(
                            data.getPersonSet().getPerson(personId),
                            selectedAction,
                            actionTarget,
                            actRay,
                            selectedAction == null ? null : data.getFinders().findAffectedActionPersons(selectedAction, actionTarget)));

                    if (confirm) {
                        performAction(monitor, personId, selectedAction, actionTarget);
                        break phaseLoop;
                    } else {
                        monitor.notify(new ConfirmCancelledNotify(personId,
                                selectedAction,
                                actionTarget));
                        currentPhase = selectedAction == null
                                ? PersonActPhase.ChooseAction
                                : PersonActPhase.ChooseTarget;
                        continue;
                    }

            }

        }
    }

    private void performAction(Monitor monitor, Integer agentPerson, Action action, MapCoordinate target) {
        int lostSpecialPoints = data.getDefinitions().actionLostSpecialPoints(agentPerson, action);
        int lostHealthPoints = data.getDefinitions().actionLostHealthPoints(agentPerson, action);
        float lostSpeedPoints = data.getDefinitions().actionLostSpeedPoints(agentPerson, action);

        data.getPersonSet().getPerson(agentPerson).subtractHealthPoints(lostHealthPoints);
        data.getPersonSet().getPerson(agentPerson).subtractSpecialPoints(lostSpecialPoints);
        data.getPersonSet().getPerson(agentPerson).subtractSpeedPoints(lostSpeedPoints);

        java.util.Map<Integer, AffectedPersonResult> affectedPersonResults = new HashMap<Integer, AffectedPersonResult>();

        for (Integer affectedPerson : data.getFinders().findAffectedActionPersons(action, target)) {
            boolean hits = data.getDefinitions().hits(action, affectedPerson);
            int damage = 0;
            if (hits) {
                damage = data.getDefinitions().damage(action, affectedPerson);
                data.getPersonSet().getPerson(affectedPerson).subtractHealthPoints(damage);
                data.getPersonSet().getPerson(affectedPerson).increaseSpecialPoints(damage);
            }

            affectedPersonResults.put(
                    affectedPerson,
                    new AffectedPersonResult(
                    hits,
                    damage,
                    data.getDefinitions().isPersonAlive(affectedPerson)));
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Action performed (agentPerson: %d, action: %s, target: %s, affected person: %d)",
                    agentPerson,
                    action == null ? "null" : action.getName(),
                    mapCoordinateToString(target),
                    affectedPersonResults.size()));

            for (Entry<Integer, AffectedPersonResult> e : affectedPersonResults.entrySet()) {
                log.debug(String.format("Affected person (Person: %d, Hit: %b, Damage: %d, Alive: %b)",
                        e.getKey(),
                        e.getValue().isHits(),
                        e.getValue().getDamage(),
                        e.getValue().isPersonAlive()));
            }
        }

        monitor.notify(new PerformedActionNotify(
                agentPerson,
                action,
                target,
                action == null ? null : data.getFinders().buildActionReachRay(action, target),
                affectedPersonResults,
                lostSpecialPoints,
                lostHealthPoints));
    }

    public SortedMap<Action, Boolean> classifyPersonActions(Integer person) {
        SortedMap<Action, Boolean> classifiedActions = new TreeMap<Action, Boolean>();
        for (Action action : data.getPersonSet().getPerson(person).getActions()) {
            classifiedActions.put(action, data.getDefinitions().isActionEnabled(person, action));
        }
        return classifiedActions;
    }

    public Collection<MapCoordinate> buildMovimentRange(int person) {
        return buildMovimentRange(
                person,
                data.getPersonSet().getPerson(person).getPosition(),
                data.getPersonSet().getPerson(person).getMoviment(),
                null);
    }

    private Set<MapCoordinate> buildMovimentRange(int person, MapCoordinate current, int movimentLeft, MapCoordinate previous) {
        Set<MapCoordinate> range = new TreeSet<MapCoordinate>(BattleData.CoordinateComparator.getInstance());

        if (data.getMap().getSquare(current).isMovimentBlocked()) {
            return range;
        }

        Integer personOnPosition = data.getPersonSet().getPersonOnPosition(current);

        if (personOnPosition != null && person != personOnPosition) {
            if (data.getDefinitions().isEnemy(person, personOnPosition)) {
                return range;
            }
        } else {
            range.add(current);
        }

        if (movimentLeft > 0) {
            for (MapCoordinate neighbor : data.getFinders().findMapNeighbors(current, 1, 1)) {
                if (previous == null || !neighbor.equals(previous)) {
                    range.addAll(buildMovimentRange(person, neighbor, movimentLeft - 1, current));
                }
            }
        }

        return range;
    }

    public Set<MapCoordinate> buildActionRange(Integer person, Action action) {
        return buildActionRange(person, action, null);
    }

    /**
     *
     * @param person
     * @param action
     * @return
     */
    public Set<MapCoordinate> buildActionRange(Integer person, Action action, MapCoordinate personPosition) {
        if (personPosition == null) {
            personPosition = data.getPersonSet().getPerson(person).getPosition();
        }
        
        Set<MapCoordinate> range = data.getFinders().findMapNeighbors(
                personPosition, action.getReach().getDistanceMin(), action.getReach().getDistanceMax());

        if (action.getReach().getDirect()) {
            Set<MapCoordinate> onSight = new TreeSet<MapCoordinate>(BattleData.CoordinateComparator.getInstance());

            for (MapCoordinate coordinate : range) {
                if (onSight(personPosition, coordinate)) {
                    onSight.add(coordinate);
                }
            }

            return onSight;
        } else {
            return range;
        }

    }

    private boolean onSight(MapCoordinate c1, MapCoordinate c2) {
        for (MapCoordinate coordinate : BattleData.Math.betweenCoordinates(c1, c2)) {
            if ((data.getMap().getSquare(coordinate).isActionBlocked()
                    || data.getPersonSet().getPersonOnPosition(coordinate) != null)
                    && BattleData.CoordinateComparator.getInstance().compare(c1, coordinate) != 0
                    && BattleData.CoordinateComparator.getInstance().compare(c2, coordinate) != 0) {
                return false;
            }
        }

        return true;
    }

    private String mapCoordinateToString(MapCoordinate c) {
        return c == null ? "null" : "(" + c.getX() + "," + c.getY() + ")";
    }

    private String mapCoordinateListToString(Collection<MapCoordinate> coordinateList) {
        StringBuilder b = new StringBuilder();

        boolean first = true;

        for (MapCoordinate c : coordinateList) {
            if (first) {
                first = false;
            } else {
                b.append(",");
            }
            b.append(mapCoordinateToString(c));
        }

        return b.toString();
    }

    public EnginePerson getPerson(int id) {
        return data.getPersonSet().getPerson(id);
    }

    public Integer getPersonOnPosition(MapCoordinate mapPosition) {
        return data.getPersonSet().getPersonOnPosition(mapPosition);
    }

    public int calculateDamage(Action action, EnginePerson targetPerson) {
        return data.getDefinitions().damage(action, targetPerson.getId());
    }

    /**
     * Probability is between 0 and 100
     *
     * @param action
     * @param targetPerson
     * @return
     */
    public int hitProbability(Action action, EnginePerson targetPerson) {
        return (int) (BattleData.Math.hitsProbability(action.getAccuracy(), targetPerson.getEvasiveness()) * 100.0);
    }

    public BattleData cloneData() {
        try {
            return (BattleData) data.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private enum PersonActPhase {

        Moviment,
        ChooseAction,
        ChooseTarget,
        Confirm
    }

    public static class AffectedPersonResult {

        private final boolean isAlive;
        private final int damage;
        private final boolean hits;

        private AffectedPersonResult(boolean hits, int damage, boolean isAlive) {
            this.hits = hits;
            this.damage = damage;
            this.isAlive = isAlive;
        }

        public boolean isPersonAlive() {
            return isAlive;
        }

        public int getDamage() {
            return damage;
        }

        public boolean isHits() {
            return hits;
        }
    }

    public static enum State {

        NotStarted,
        Started,
        Finalized
    }

    public static class ValueChanged<T> {

        private final T oldValue;
        private final T newValue;

        public ValueChanged(T oldValue, T newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public T getOldValue() {
            return oldValue;
        }

        public T getNewValue() {
            return newValue;
        }
    }

    public static class SelectedAction {

        private final Action action;

        public SelectedAction(Action action) {
            this.action = action;
        }

        public Action getAction() {
            return action;
        }
    }
}
