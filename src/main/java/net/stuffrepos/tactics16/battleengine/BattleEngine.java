package net.stuffrepos.tactics16.battleengine;

import net.stuffrepos.tactics16.battleengine.events.ActConfirmRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    private final Map map;
    private final PersonSet personSet = new PersonSet();
    private final Definitions definitions = new Definitions();
    private final Finders finders = new Finders();
    private boolean running;
    /**
     * Speed cost for any action.
     */
    public static final float ACT_SPEED_POINTS_COST = 1.0f;
    public static final int SPECIAL_POINTS_INCREMENT_BY_END_TURN = 1;
    public static final int INITIAL_SPECIAL_POINTS = 0;
    private State state = State.NotStarted;

    public BattleEngine(
            Map map,
            java.util.Map<Integer, EnginePersonConfig> persons,
            java.util.Map<Integer, Integer> personsPlayers,
            java.util.Map<Integer, MapCoordinate> personsPositions) {

        assert map != null;
        assert persons.size() >= 2 : "persons.size(): " + persons.size();
        assert personsPlayers.size() == persons.size();
        assert personsPositions.size() == persons.size();

        this.map = map;

        for (Entry<Integer, EnginePersonConfig> e : persons.entrySet()) {
            this.personSet.addPerson(
                    e.getKey(),
                    e.getValue(),
                    personsPositions.get(e.getKey()).clone(),
                    personsPlayers.get(e.getKey()));
        }
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

                while ((selectedPerson = finders.nextPersonToAct()) != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Selected person: " + selectedPerson);
                    }

                    monitor.notify(new SelectedPersonNotify(selectedPerson));
                    personActs(monitor, selectedPerson);
                    if (finders.getAlivePlayers().size() < 2) {
                        break turnLoop;
                    }
                }

                for (int person : personSet.getPersons()) {
                    if (personSet.getPerson(person).isAlive()) {
                        personSet.getPerson(person).increaseSpecialPoints(SPECIAL_POINTS_INCREMENT_BY_END_TURN);
                    }
                }

            }
            monitor.notify(new WinningPlayerNotify(finders.getWinnerPlayer()));
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

        for (int person : finders.getAlivePersons()) {
            float oldSpeedPoints = personSet.getPerson(person).getSpeedPoints();
            personSet.getPerson(person).renewSpeedPoints();
            personsSpeeds.put(
                    person,
                    new ValueChanged<Float>(
                    oldSpeedPoints,
                    personSet.getPerson(person).getSpeedPoints()));
        }

        return personsSpeeds.entrySet();
    }

    private void personActs(Monitor monitor, Integer personId) {
        PersonActPhase currentPhase = PersonActPhase.Moviment;
        Action selectedAction = null;
        MapCoordinate originalPosition = personSet.getPerson(personId).getPosition();
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
                            personSet.getPerson(personId),
                            map,
                            personSet.getPerson(personId).getPosition(),
                            movimentRange));

                    if (log.isDebugEnabled()) {
                        log.debug("Moviment position requested: " + mapCoordinateToString(movimentTarget));
                    }

                    if (movimentTarget == null) {
                        continue;
                    } else if (movimentRange.contains(movimentTarget)) {
                        personSet.getPerson(personId).setPosition(movimentTarget);
                        monitor.notify(new PersonMoved(personId, originalPosition, movimentTarget));
                        currentPhase = PersonActPhase.ChooseAction;
                    } else {
                        monitor.notify(new OutOfReachMoviment(personId, movimentTarget));
                    }
                    continue;

                case ChooseAction:
                    SelectedAction selectedActionResult = monitor.request(new PersonActionRequest(
                            personSet.getPerson(personId),
                            map,
                            personSet.getPerson(personId).getPosition(),
                            classifyPersonActions(personId)));

                    if (selectedActionResult == null) {
                        personSet.getPerson(personId).setPosition(originalPosition);
                        monitor.notify(new MovimentCancelledNotify(personId, originalPosition, movimentTarget));
                        movimentTarget = null;
                        currentPhase = PersonActPhase.Moviment;
                    } else if (selectedActionResult.getAction() == null) {
                        monitor.notify(new SelectedActionNotify(personId, null));
                        currentPhase = PersonActPhase.Confirm;
                    } else if (definitions.isActionEnabled(personId, selectedActionResult.getAction())) {
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
                            personSet.getPerson(personId),
                            map,
                            personSet.getPerson(personId).getPosition(),
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
                        monitor.notify(new OutOfReachNotify(personId, selectedAction));
                    }
                    continue;

                case Confirm:
                    Collection<MapCoordinate> actRay = selectedAction == null ? null : buildActionReachRay(
                            selectedAction,
                            actionTarget);
                    boolean confirm = monitor.request(new ActConfirmRequest(
                            personSet.getPerson(personId),
                            selectedAction,
                            actionTarget,
                            actRay,
                            selectedAction == null ? null : findAffectedActionPersons(selectedAction, actionTarget)));

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
        int lostSpecialPoints = definitions.actionLostSpecialPoints(agentPerson, action);
        int lostHealthPoints = definitions.actionLostHealthPoints(agentPerson, action);
        float lostSpeedPoints = definitions.actionLostSpeedPoints(agentPerson, action);

        personSet.getPerson(agentPerson).subtractHealthPoints(lostHealthPoints);
        personSet.getPerson(agentPerson).subtractSpecialPoints(lostSpecialPoints);
        personSet.getPerson(agentPerson).subtractSpeedPoints(lostSpeedPoints);

        java.util.Map<Integer, AffectedPersonResult> affectedPersonResults = new HashMap<Integer, AffectedPersonResult>();

        for (Integer affectedPerson : findAffectedActionPersons(action, target)) {
            boolean hits = definitions.hits(action, affectedPerson);
            int damage = 0;
            if (hits) {
                damage = definitions.damage(action, affectedPerson);
                personSet.getPerson(affectedPerson).subtractHealthPoints(damage);
                personSet.getPerson(affectedPerson).increaseSpecialPoints(damage);
            }

            affectedPersonResults.put(
                    affectedPerson,
                    new AffectedPersonResult(
                    hits,
                    damage,
                    definitions.isPersonAlive(affectedPerson)));
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
                action == null ? null : buildActionReachRay(action, target),
                affectedPersonResults,
                lostSpecialPoints,
                lostHealthPoints));
    }

    public SortedMap<Action, Boolean> classifyPersonActions(Integer person) {
        SortedMap<Action, Boolean> classifiedActions = new TreeMap<Action, Boolean>();
        for (Action action : personSet.getPerson(person).getActions()) {
            classifiedActions.put(action, definitions.isActionEnabled(person, action));
        }
        return classifiedActions;
    }

    public Collection<MapCoordinate> buildMovimentRange(int person) {
        return buildMovimentRange(
                person,
                personSet.getPerson(person).getPosition(),
                personSet.getPerson(person).getMoviment(),
                null);
    }

    private Set<MapCoordinate> buildMovimentRange(int person, MapCoordinate current, int movimentLeft, MapCoordinate previous) {
        Set<MapCoordinate> range = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

        if (map.getSquare(current).isMovimentBlocked()) {
            return range;
        }

        Integer personOnPosition = personSet.getPersonOnPosition(current);

        if (personOnPosition != null && person != personOnPosition) {
            if (definitions.isEnemy(person, personOnPosition)) {
                return range;
            }
        } else {
            range.add(current);
        }

        if (movimentLeft > 0) {
            for (MapCoordinate neighbor : findMapNeighbors(current, 1, 1)) {
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
            personPosition = personSet.getPerson(person).getPosition();
        }
        
        Set<MapCoordinate> range = findMapNeighbors(
                personPosition, action.getReach().getDistanceMin(), action.getReach().getDistanceMax());

        if (action.getReach().getDirect()) {
            Set<MapCoordinate> onSight = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

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

    private Collection<Integer> findAffectedActionPersons(Action action, MapCoordinate target) {
        Set<Integer> affectedPersons = new HashSet<Integer>();
        if (action != null) {
            for (MapCoordinate coordinate : buildActionReachRay(action, target)) {
                Integer person = personSet.getPersonOnPosition(coordinate);
                if (person != null) {
                    affectedPersons.add(person);
                }
            }
        }

        return affectedPersons;
    }

    private Collection<MapCoordinate> buildActionReachRay(Action action, MapCoordinate target) {
        return findMapNeighbors(
                target, action.getReach().getRayMin(), action.getReach().getRayMax());
    }

    private Set<MapCoordinate> findMapNeighbors(MapCoordinate current, int minimumRay, Integer maximumRay) {
        Set<MapCoordinate> neighbors = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

        for (int ray = minimumRay;
                ray <= maximumRay;
                ++ray) {
            if (ray == 0) {
                if (inMap(current)) {
                    neighbors.add(current);
                }
            } else {
                for (int angle = 0; angle < ray * 4; angle++) {
                    int cosine = Math.integerCosine(ray, angle);
                    int sine = Math.integerSine(ray, angle);
                    int x = current.getX() + cosine;
                    int y = current.getY() + sine;

                    if (inMap(x, y)) {
                        neighbors.add(new CoordinateImpl(x, y));
                    }
                }
            }
        }

        return neighbors;
    }

    private boolean inMap(MapCoordinate coordinate) {
        return inMap(coordinate.getX(), coordinate.getY());
    }

    private boolean inMap(int x, int y) {
        return x >= 0
                && x < map.getWidth()
                && y >= 0
                && y < map.getHeight();
    }

    private boolean onSight(MapCoordinate c1, MapCoordinate c2) {
        for (MapCoordinate coordinate : Math.betweenCoordinates(c1, c2)) {
            if ((map.getSquare(coordinate).isActionBlocked()
                    || personSet.getPersonOnPosition(coordinate) != null)
                    && CoordinateComparator.getInstance().compare(c1, coordinate) != 0
                    && CoordinateComparator.getInstance().compare(c2, coordinate) != 0) {
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
        return personSet.getPerson(id);
    }

    public Integer getPersonOnPosition(MapCoordinate mapPosition) {
        return personSet.getPersonOnPosition(mapPosition);
    }

    public int calculateDamage(Action action, EnginePerson targetPerson) {
        return definitions.damage(action, targetPerson.getId());
    }

    /**
     * Probability is between 0 and 100
     *
     * @param action
     * @param targetPerson
     * @return
     */
    public int hitProbability(Action action, EnginePerson targetPerson) {
        return (int) (Math.hitsProbability(action.getAccuracy(), targetPerson.getEvasiveness()) * 100.0);
    }

    private class Finders {

        private Collection<Integer> getAlivePlayers() {
            List<Integer> alivePlayers = new LinkedList<Integer>();
            for (Integer player : personSet.getPlayers()) {
                if (definitions.isPlayerAlive(player)) {
                    alivePlayers.add(player);
                }
            }
            return alivePlayers;
        }

        private List<Integer> getPlayerAlivePersons(int playerId) {
            List<Integer> alivePersons = new LinkedList<Integer>();
            for (Integer personId : personSet.getPersonsByPlayer(playerId)) {
                if (definitions.isPersonAlive(personId)) {
                    alivePersons.add(personId);
                }
            }
            return alivePersons;
        }

        private Iterable<Integer> getAlivePersons() {
            List<Integer> alivePersons = new LinkedList<Integer>();
            for (Integer personId : personSet.getPersons()) {
                if (definitions.isPersonAlive(personId)) {
                    alivePersons.add(personId);
                }
            }
            return alivePersons;
        }

        private List<Integer> orderPersonsToAct() {
            List<Integer> orderedPlayers = orderPlayersByDescendingAlivePersons(
                    getAlivePlayers());
            List<Integer> orderedPersons = null;

            for (Integer playerId : orderedPlayers) {
                List<Integer> playerPersons = getPlayerAlivePersons(playerId);
                Collections.shuffle(playerPersons);

                if (orderedPersons == null) {
                    orderedPersons = playerPersons;
                } else {
                    int ratio = playerPersons.size() / orderedPersons.size();
                    for (int i = 0; i < playerPersons.size(); ++i) {
                        orderedPersons.add(i * (ratio + 1), playerPersons.get(i));
                    }
                }
            }

            return orderedPersons;
        }

        private List<Integer> orderPlayersByDescendingAlivePersons(Collection<Integer> players) {
            List<Integer> result = new LinkedList<Integer>(players);
            Collections.sort(result, new Comparator<Integer>() {
                public int compare(Integer player1, Integer player2) {
                    Collection<Integer> player1AlivePersons = getPlayerAlivePersons(player1);
                    Collection<Integer> player2AlivePersons = getPlayerAlivePersons(player2);

                    if (player1AlivePersons.size() > player2AlivePersons.size()) {
                        return -1;
                    } else if (player1AlivePersons.size() < player2AlivePersons.size()) {
                        return 1;
                    } else {
                        return player1.toString().compareTo(player2.toString());
                    }
                }
            });
            return result;
        }

        private Integer getWinnerPlayer() {
            Collection<Integer> aliveObjects = getAlivePlayers();
            switch (aliveObjects.size()) {
                case 0:
                    return null;
                case 1:
                    return aliveObjects.iterator().next();
                default:
                    throw new IllegalStateException("There is more than one player alive.");
            }
        }

        /**
         * The next person to act is that have most speed points and minimum of
         * {@link #ACT_SPEED_POINTS_COST}.
         *
         * @return
         */
        private Integer nextPersonToAct() {
            Integer selected = null;
            for (int person : getAlivePersons()) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Person(%d).speedPoints: %f", person, personSet.getPerson(person).getSpeedPoints()));
                }

                if (personSet.getPerson(person).getSpeedPoints() >= ACT_SPEED_POINTS_COST) {
                    if (selected == null
                            || personSet.getPerson(person).getSpeedPoints() > personSet.getPerson(selected).getSpeedPoints()
                            || (personSet.getPerson(person).getSpeedPoints() == personSet.getPerson(selected).getSpeedPoints()
                            && personSet.getPerson(person).getHealthPoints() < personSet.getPerson(selected).getHealthPoints())) {
                        selected = person;
                    }
                }
            }
            return selected;
        }
    }

    private class Definitions {

        private boolean isPersonAlive(int person) {
            return personSet.getPerson(person).getHealthPoints() > 0;
        }

        private boolean isPlayerAlive(int player) {
            return !finders.getPlayerAlivePersons(player).isEmpty();
        }

        private boolean isActionEnabled(int person, Action action) {
            return personSet.getPerson(person).getSpecialPoints()
                    + personSet.getPerson(person).getHealthPoints() >= action.getSpecialPointsCost();
        }

        private boolean isEnemy(int person, int otherPerson) {
            return !personSet.getPerson(person).getPlayer().equals(
                    personSet.getPerson(otherPerson).getPlayer());
        }

        private boolean hits(Action action, int affectedPerson) {
            double hitProbability = Math.hitsProbability(action.getAccuracy(), personSet.getPerson(affectedPerson).getEvasiveness());
            double result = java.lang.Math.random();
            if (log.isDebugEnabled()) {
                log.debug(
                        String.format(
                        "Action accuracy: %d / Affected evasiveness: %d / Hit Probability: %f / Result: %f",
                        action.getAccuracy(),
                        personSet.getPerson(affectedPerson).getEvasiveness(),
                        hitProbability,
                        result));
            }
            return result <= hitProbability;
        }

        private int damage(Action action, int affectedPerson) {
            return java.lang.Math.max(action.getPower() - personSet.getPerson(affectedPerson).getResistence(), 0);
        }

        private int actionLostSpecialPoints(int agentPerson, Action action) {
            if (action == null) {
                return 0;
            } else if (personSet.getPerson(agentPerson).getSpecialPoints() >= action.getSpecialPointsCost()) {
                return action.getSpecialPointsCost();
            } else {
                return personSet.getPerson(agentPerson).getSpecialPoints();
            }
        }

        /**
         * The action uses HP in place of SP if it is not enough for execute
         * action.
         *
         * @param agentPerson
         * @param action
         * @return
         */
        private int actionLostHealthPoints(int agentPerson, Action action) {
            if (action == null) {
                return 0;
            } else {
                return -java.lang.Math.min(0, personSet.getPerson(agentPerson).getSpecialPoints() - action.getSpecialPointsCost());
            }
        }

        private float actionLostSpeedPoints(Integer agentPerson, Action action) {
            return action == null ? ACT_SPEED_POINTS_COST / 2.0f : ACT_SPEED_POINTS_COST;
        }
    }

    private enum PersonActPhase {

        Moviment,
        ChooseAction,
        ChooseTarget,
        Confirm
    }

    private static class CoordinateImpl implements Map.MapCoordinate {

        final int x;
        final int y;

        private CoordinateImpl(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        @Override
        public MapCoordinate clone() {
            return new CoordinateImpl(x, y);
        }
    }

    private static class CoordinateComparator implements Comparator<MapCoordinate> {

        private static final CoordinateComparator instance = new CoordinateComparator();

        public static CoordinateComparator getInstance() {
            return instance;
        }

        public int compare(MapCoordinate c1, MapCoordinate c2) {
            if (c1.getX() < c2.getX()) {
                return -1;
            } else if (c1.getX() > c2.getX()) {
                return 1;
            } else if (c1.getY() < c2.getY()) {
                return -1;
            } else if (c1.getY() > c2.getY()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class PersonSet {

        private java.util.Map<Integer, PersonData> persons =
                new HashMap<Integer, PersonData>();
        private java.util.Map<MapCoordinate, Integer> mapPersons =
                new TreeMap<MapCoordinate, Integer>(CoordinateComparator.getInstance());
        private java.util.Map<Integer, Collection<Integer>> playerPersons =
                new HashMap<Integer, Collection<Integer>>();

        private void addPerson(int personId, EnginePersonConfig person, MapCoordinate position, int playerId) {
            persons.put(personId, new PersonData(person, personId));
            persons.get(personId).setPlayer(playerId);
            persons.get(personId).setPosition(position);
        }

        public Integer getPersonOnPosition(MapCoordinate position) {
            Integer person = mapPersons.get(position);
            if (person != null && definitions.isPersonAlive(person)) {
                return person;
            } else {
                return null;
            }
        }

        private Iterable<Integer> getPlayers() {
            return playerPersons.keySet();
        }

        private Iterable<Integer> getPersonsByPlayer(int playerId) {
            return playerPersons.get(playerId);
        }

        private PersonData getPerson(int personId) {
            assert persons.get(personId) != null : "Person id: " + personId;
            return persons.get(personId);
        }

        private Iterable<Integer> getPersons() {
            return persons.keySet();
        }

        public class PersonData implements EnginePerson {

            private final int id;
            private Integer playerId;
            private int healthPoints;
            private int specialPoints;
            private float speedPoints;
            private final EnginePersonConfig person;
            private MapCoordinate position;

            public PersonData(EnginePersonConfig person, int id) {
                this.person = person;
                this.id = id;
                this.healthPoints = person.getMaximumHealthPoints();
                this.specialPoints = INITIAL_SPECIAL_POINTS;
            }

            public int getHealthPoints() {
                return healthPoints;
            }

            public int getSpecialPoints() {
                return specialPoints;
            }

            private int getMoviment() {
                return person.getMoviment();
            }

            private Iterable<Action> getActions() {
                return person.getActions();
            }

            private Integer getPlayer() {
                return playerId;
            }

            private void setPosition(MapCoordinate position) {
                if (this.position != null) {
                    mapPersons.remove(this.position);
                }

                this.position = position;
                mapPersons.put(position, id);
            }

            private void setPlayer(int playerId) {
                if (this.playerId != null) {
                    playerPersons.get(this.playerId).remove(this.id);
                }

                if (playerPersons.get(playerId) == null) {
                    playerPersons.put(playerId, new HashSet<Integer>());
                }

                this.playerId = playerId;
                playerPersons.get(playerId).add(this.id);
            }

            public MapCoordinate getPosition() {
                return position;
            }

            public int getEvasiveness() {
                return person.getEvasiveness();
            }

            private int getResistence() {
                return person.getResistence();
            }

            private void subtractHealthPoints(int lostHealthPoints) {
                this.healthPoints = java.lang.Math.max(0,
                        this.healthPoints - lostHealthPoints);
            }

            private void subtractSpecialPoints(int lostSpecialPoints) {
                this.specialPoints = java.lang.Math.max(0,
                        this.specialPoints - lostSpecialPoints);
            }

            public float getSpeedPoints() {
                return speedPoints;
            }

            private void subtractSpeedPoints(float lostSpeedPoints) {
                this.speedPoints -= lostSpeedPoints;
            }

            public float getSpeed() {
                return person.getSpeed();
            }

            private void renewSpeedPoints() {
                this.speedPoints = person.getSpeed();
            }

            public int getMaximumHealthPoints() {
                return person.getMaximumHealthPoints();
            }

            public int getMaximumSpecialPoints() {
                return person.getMaximumSpecialPoints();
            }

            public boolean isAlive() {
                return definitions.isPersonAlive(id);
            }

            private void increaseSpecialPoints(int increment) {
                assert increment > 0;
                this.specialPoints = java.lang.Math.min(
                        this.getMaximumSpecialPoints(),
                        this.specialPoints + increment);
            }
            
            public int getId() {
                return id;
            }

            public int getPlayerId() {
                return playerId;
            }
        }
    }

    public static class Math {

        public static int integerCosine(int ray, int angle) {
            return (angle / (ray * 2)) % 2 == 0
                    ? -ray + angle % (ray * 2)
                    : ray - angle % (ray * 2);
        }

        public static int integerSine(int ray, int angle) {
            return ((angle + ray) / (ray * 2)) % 2 == 0
                    ? -ray + (angle + ray) % (ray * 2)
                    : ray - (angle + ray) % (ray * 2);
        }

        private static Iterable<MapCoordinate> betweenCoordinates(MapCoordinate c0, MapCoordinate c1) {
            Set<MapCoordinate> coordinates = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

            int x0 = c0.getX();
            int y0 = c0.getY();
            int x1 = c1.getX();
            int y1 = c1.getY();
            int dx = java.lang.Math.abs(x1 - x0);
            int dy = java.lang.Math.abs(y1 - y0);
            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            while (true) {
                coordinates.add(new CoordinateImpl(x0, y0));
                if (x0 == x1 && y0 == y1) {
                    break;
                }

                int e2 = 2 * err;

                if (e2 > -dy) {
                    err -= dy;
                    x0 += sx;
                }

                if (e2 < dx) {
                    err += dx;
                    y0 += sy;
                }
            }

            return coordinates;
        }

        public static long binomial(int n, int k) {
            if (k == 0) {
                return 1;
            }
            if (n == 0) {
                return 0;
            }
            return binomial(n - 1, k) + binomial(n - 1, k - 1);
        }

        public static double hitsProbability(int agentActionAccuracy, int targetEvasiveness) {
            double p = 0.0;
            for (int a = 0; a <= agentActionAccuracy; ++a) {
                for (int t = 0; t <= a; ++t) {
                    p += Math.binomial(agentActionAccuracy, a)
                            * Math.binomial(targetEvasiveness, t)
                            * java.lang.Math.pow(2, -(agentActionAccuracy + targetEvasiveness));
                }
            }
            return java.lang.Math.round(p * 100.0) / 100.0;
        }
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
