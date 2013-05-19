package net.stuffrepos.tactics16.battlegameengine;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battlegameengine.events.ChooseActionCancelled;
import net.stuffrepos.tactics16.battlegameengine.events.ChoosedTarget;
import net.stuffrepos.tactics16.battlegameengine.events.ConfirmCancelledNotify;
import net.stuffrepos.tactics16.battlegameengine.events.MovimentCancelledNotify;
import net.stuffrepos.tactics16.battlegameengine.events.NewTurn;
import net.stuffrepos.tactics16.battlegameengine.events.NotEnabledActionNotify;
import net.stuffrepos.tactics16.battlegameengine.events.OutOfReachMoviment;
import net.stuffrepos.tactics16.battlegameengine.events.OutOfReachNotify;
import net.stuffrepos.tactics16.battlegameengine.events.PerformedActionNotify;
import net.stuffrepos.tactics16.battlegameengine.events.PersonMoved;
import net.stuffrepos.tactics16.battlegameengine.events.SelectedActionNotify;
import net.stuffrepos.tactics16.battlegameengine.events.SelectedPersonNotify;
import net.stuffrepos.tactics16.battlegameengine.events.WinningPlayerNotify;
import net.stuffrepos.tactics16.battlegameengine.events.ActionTargetRequest;
import net.stuffrepos.tactics16.battlegameengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.battlegameengine.events.PersonActionRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
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
    public static final float ACT_SPEED_POINTS_COST = 1.0f;
    private State state = State.NotStarted;

    public BattleEngine(
            Map map,
            java.util.Map<Integer, Person> persons,
            java.util.Map<Integer, Integer> personsPlayers,
            java.util.Map<Integer, MapCoordinate> personsPositions) {

        assert map != null;
        assert persons.size() >= 2 : "persons.size(): " + persons.size();
        assert personsPlayers.size() == persons.size();
        assert personsPositions.size() == persons.size();

        this.map = map;

        for (Entry<Integer, Person> e : persons.entrySet()) {
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

                monitor.notify(new NewTurn(++turn));

                for (int person : finders.getAlivePersons()) {
                    personSet.getPerson(person).renewSpeedPoints();
                }

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
                            personId,
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
                    selectedAction = monitor.request(new PersonActionRequest(
                            personId,
                            map,
                            personSet.getPerson(personId).getPosition(),
                            classifyPersonActions(personId)));

                    if (selectedAction == null) {
                        personSet.getPerson(personId).setPosition(originalPosition);
                        monitor.notify(new MovimentCancelledNotify(personId, originalPosition, movimentTarget));
                        movimentTarget = null;
                        currentPhase = PersonActPhase.Moviment;
                    } else if (definitions.isActionEnabled(personId, selectedAction)) {
                        monitor.notify(new SelectedActionNotify(personId, selectedAction));
                        currentPhase = PersonActPhase.ChooseTarget;
                    } else {
                        monitor.notify(new NotEnabledActionNotify(personId, selectedAction));
                    }
                    continue;

                case ChooseTarget:
                    Collection<MapCoordinate> actionRange = buildActionRange(personId, selectedAction);
                    actionTarget = monitor.request(new ActionTargetRequest(
                            personId,
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
                    Collection<MapCoordinate> actRay = buildActionReachRay(
                            selectedAction,
                            actionTarget);
                    boolean confirm = monitor.request(new ActConfirmRequest(
                            personId,
                            selectedAction,
                            actionTarget,
                            actRay,
                            findAffectedActionPersons(selectedAction, actionTarget)));

                    if (confirm) {
                        performAction(monitor, personId, selectedAction, actionTarget);
                        break phaseLoop;
                    } else {
                        monitor.notify(new ConfirmCancelledNotify(personId,
                                selectedAction,
                                actionTarget));
                        currentPhase = PersonActPhase.ChooseTarget;
                        continue;
                    }

            }

        }
    }

    private void performAction(Monitor monitor, Integer agentPerson, Action action, MapCoordinate target) {
        int lostSpecialPoints = definitions.actionLostSpecialPoints(agentPerson, action);
        int lostHealthPoints = definitions.actionLostHealthPoints(agentPerson, action);

        personSet.getPerson(agentPerson).subtractHealthPoints(lostHealthPoints);
        personSet.getPerson(agentPerson).subtractSpecialPoints(lostSpecialPoints);
        personSet.getPerson(agentPerson).subtractSpeedPoints(ACT_SPEED_POINTS_COST);

        java.util.Map<Integer, AffectedPersonResult> affectedPersonResults = new HashMap<Integer, AffectedPersonResult>();

        for (Integer affectedPerson : findAffectedActionPersons(action, target)) {
            boolean hits = definitions.hits(action, affectedPerson);
            int damage = 0;
            if (hits) {
                damage = definitions.damage(action, affectedPerson);
                personSet.getPerson(affectedPerson).subtractHealthPoints(damage);
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
                    action.getName(),
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
                buildActionReachRay(action, target),
                affectedPersonResults,
                lostSpecialPoints,
                lostHealthPoints));
    }

    private java.util.Map<Action, Boolean> classifyPersonActions(Integer person) {
        java.util.Map<Action, Boolean> classifiedActions = new HashMap<Action, Boolean>();
        for (Action action : personSet.getPerson(person).getActions()) {
            classifiedActions.put(action, definitions.isActionEnabled(person, action));
        }
        return classifiedActions;
    }

    private Collection<MapCoordinate> buildMovimentRange(int person) {
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

    /**
     *
     * @param person
     * @param action
     * @return
     */
    private Set<MapCoordinate> buildActionRange(Integer person, Action action) {
        Set<MapCoordinate> range = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

        for (int distance = action.getReach().getMinimum();
                distance <= action.getReach().getMaximum();
                ++distance) {
            for (int angle = 0; angle < distance * 4; angle++) {
                int x = personSet.getPerson(person).getPosition().getX()
                        + Math.integerCosine(distance, angle);
                int y = personSet.getPerson(person).getPosition().getY()
                        + Math.integerSine(distance, angle);

                if (inMap(x, y)) {
                    range.add(new CoordinateImpl(x, y));
                }
            }
        }

        if (action.getReach().getDirect()) {
            Set<MapCoordinate> onSight = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

            for (MapCoordinate coordinate : range) {
                if (onSight(personSet.getPerson(person).getPosition(), coordinate)) {
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
        for (MapCoordinate coordinate : buildActionReachRay(action, target)) {
            Integer person = personSet.getPersonOnPosition(coordinate);
            if (person != null) {
                affectedPersons.add(person);
            }
        }

        return affectedPersons;
    }

    private Collection<MapCoordinate> buildActionReachRay(Action action, MapCoordinate target) {
        Set<MapCoordinate> coords = findMapNeighbors(target, 0, action.getReach().getRay());
        coords.add(target);
        return coords;
    }

    private Set<MapCoordinate> findMapNeighbors(MapCoordinate current, int minimumRay, Integer maximumRay) {
        Set<MapCoordinate> neighbors = new TreeSet<MapCoordinate>(CoordinateComparator.getInstance());

        for (int ray = minimumRay;
                ray <= maximumRay;
                ++ray) {

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
                    throw new IllegalStateException("There is no one player alive.");
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
            return personSet.getPerson(person).getSpecialPoints() >= action.costSpecialPoints();
        }

        private boolean isEnemy(int person, int otherPerson) {
            return !personSet.getPerson(person).getPlayer().equals(
                    personSet.getPerson(otherPerson).getPlayer());
        }

        private boolean hits(Action action, int affectedPerson) {
            int accuracyDices = dice(action.getAccuracy());
            int evasivenessDices = dice(personSet.getPerson(affectedPerson).getEvasiveness());
            if (log.isDebugEnabled()) {
                log.debug(String.format("Action accuracy: %d/%d - Affected evasiveness: %d/%d",
                        accuracyDices,
                        action.getAccuracy(),
                        evasivenessDices,
                        personSet.getPerson(affectedPerson).getEvasiveness()));
            }
            return accuracyDices >= evasivenessDices;
        }

        private int damage(Action action, int affectedPerson) {
            return java.lang.Math.max(action.getPower() - personSet.getPerson(affectedPerson).getResistence(), 0);
        }

        private int dice(int quantity) {
            int result = 0;
            for (int i = 0; i < quantity; ++i) {
                if (java.lang.Math.random() < 0.5) {
                    result++;
                }
            }

            return result;
        }

        private int actionLostSpecialPoints(int agentPerson, Action action) {
            return action.costSpecialPoints();
        }

        private int actionLostHealthPoints(int agentPerson, Action action) {
            return 0; //TO-DO
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

        private void addPerson(int personId, Person person, MapCoordinate position, int playerId) {
            persons.put(personId, new PersonData(person, personId));
            persons.get(personId).setPlayer(playerId);
            persons.get(personId).setPosition(position);
        }

        public Integer getPersonOnPosition(MapCoordinate position) {
            return mapPersons.get(position);
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

        public class PersonData {

            private final int id;
            private Integer playerId;
            private int healthPoints;
            private int specialPoints;
            private float speedPoints;
            private final Person person;
            private MapCoordinate position;

            public PersonData(Person person, int id) {
                this.person = person;
                this.id = id;
                this.healthPoints = person.getMaximumHealthPoints();
                this.specialPoints = person.getMaximumSpecialPoints();
            }

            private int getHealthPoints() {
                return healthPoints;
            }

            private int getSpecialPoints() {
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

            private MapCoordinate getPosition() {
                return position;
            }

            private int getEvasiveness() {
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

            private float getSpeedPoints() {
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
}
