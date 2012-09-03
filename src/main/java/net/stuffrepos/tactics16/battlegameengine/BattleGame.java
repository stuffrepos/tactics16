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
import net.stuffrepos.tactics16.battlegameengine.Map.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleGame {

    private final Map map;
    private Monitor monitor;
    private final PersonSet personSet;
    private final Definitions definitions = new Definitions();
    private final Finders finders = new Finders();

    public BattleGame(
            Map map,
            java.util.Map<Integer, Person> persons,
            java.util.Map<Integer, Integer> personsPlayers,
            java.util.Map<Integer, Coordinate> personsPositions,
            Monitor monitor) {
        this.map = map.clone();
        this.monitor = monitor;
        this.personSet = new PersonSet();

        for (Entry<Integer, Person> e : persons.entrySet()) {
            this.personSet.addPerson(
                    e.getKey(),
                    e.getValue().clone(),
                    personsPositions.get(e.getKey()).clone(),
                    personsPlayers.get(e.getKey()));
        }
    }

    public void run() {
        int turn = 0;
        turnLoop:
        while (true) {
            monitor.notifyNewTurn(++turn);
            List<Integer> persons = finders.orderPersonsToAct();
            monitor.notifyPersonsActOrderDefined(persons);
            for (Integer selectedPerson : persons) {
                if (definitions.isPersonAlive(selectedPerson)) {
                    monitor.notifySelectedPerson(selectedPerson);
                    personActs(selectedPerson);
                    if (finders.getAlivePlayers().size() < 2) {
                        break turnLoop;
                    }
                }
            }
        }
        monitor.notifyWiningPlayer(finders.getWinnerPlayer());
    }

    private void personActs(Integer personId) {
        PersonActPhase currentPhase = PersonActPhase.Moviment;
        Action selectedAction = null;
        Coordinate originalPosition = personSet.getPerson(personId).getPosition();
        Coordinate movimentTarget = null;
        Coordinate actionTarget = null;

        phaseLoop:
        while (true) {
            switch (currentPhase) {
                case Moviment:
                    Collection<Coordinate> movimentRange = buildMovimentRange(personId);
                    movimentTarget = monitor.requestMovimentTarget(
                            personId,
                            map,
                            personSet.getPerson(personId).getPosition(),
                            movimentRange);

                    if (movimentTarget == null) {
                        continue;
                    } else if (movimentRange.contains(movimentTarget)) {
                        personSet.getPerson(personId).setPosition(movimentTarget);
                        monitor.notifyPersonMoved(personId, originalPosition, movimentTarget);
                        currentPhase = PersonActPhase.ChooseAction;
                    } else {
                        monitor.notifyOutOfReachMoviment(personId, movimentTarget);
                    }
                    continue;

                case ChooseAction:
                    selectedAction = monitor.requestPersonAction(
                            personId,
                            map,
                            personSet.getPerson(personId).getPosition(),
                            classifyPersonActions(personId));

                    if (selectedAction == null) {
                        personSet.getPerson(personId).setPosition(movimentTarget);
                        monitor.notifyMovimentCancelled(personId, originalPosition, movimentTarget);
                        movimentTarget = null;
                        currentPhase = PersonActPhase.Moviment;
                    } else if (definitions.isActionEnabled(personId, selectedAction)) {
                        monitor.notifySelectedAction(personId, selectedAction);
                        currentPhase = PersonActPhase.ChooseTarget;
                    } else {
                        monitor.notifyNotEnabledAction(personId, selectedAction);
                    }
                    continue;

                case ChooseTarget:
                    Collection<Coordinate> actionRange = buildActionRange(personId, selectedAction);
                    actionTarget = monitor.requestActionTarget(
                            personId,
                            map,
                            personSet.getPerson(personId).getPosition(),
                            selectedAction,
                            actionRange);

                    if (actionTarget == null) {
                        monitor.notifyChooseActionCancelled(personId, selectedAction);
                        currentPhase = PersonActPhase.ChooseAction;
                        selectedAction = null;
                    } else if (actionRange.contains(actionTarget)) {
                        monitor.notifyChoosedTarget(personId, actionTarget);
                        currentPhase = PersonActPhase.Confirm;
                    } else {
                        monitor.notifyOutOfReach(personId, selectedAction);
                    }
                    continue;

                case Confirm:
                    Collection<Coordinate> actRay = buildActionReachRay(
                            selectedAction,
                            personSet.getPerson(personId).getPosition());
                    boolean confirm = monitor.requestActConfirm(
                            personId,
                            selectedAction,
                            actionTarget,
                            actRay,
                            findAffectedActionPersons(selectedAction, actionTarget));

                    if (confirm) {
                        performAction(personId, selectedAction, actionTarget);
                        break phaseLoop;
                    } else {
                        monitor.notifyConfirmCancelled(personId,
                                selectedAction,
                                actionTarget);
                        currentPhase = PersonActPhase.ChooseTarget;
                        continue;
                    }

            }

        }
    }

    private void performAction(Integer agentPerson, Action action, Coordinate target) {
        Collection<Integer> affectedPersons = findAffectedActionPersons(action, target);

        int lostSpecialPoints = definitions.actionLostSpecialPoints(agentPerson, action);
        int lostHealthPoints = definitions.actionLostHealthPoints(agentPerson, action);

        personSet.getPerson(agentPerson).subtractHealthPoints(lostHealthPoints);
        personSet.getPerson(agentPerson).subtractSpecialPoints(lostSpecialPoints);

        monitor.notifyPerformedAction(
                agentPerson,
                action,
                target,
                buildActionReachRay(action, target),
                affectedPersons,
                lostSpecialPoints,
                lostHealthPoints);

        for (Integer affectedPerson : findAffectedActionPersons(action, target)) {
            boolean hits = definitions.hits(action, affectedPerson);
            int damage = 0;
            if (hits) {
                damage = definitions.damage(action, affectedPerson);
                personSet.getPerson(affectedPerson).subtractHealthPoints(damage);
            }

            monitor.notifyActionAffectedPerson(
                    affectedPerson,
                    hits,
                    damage,
                    definitions.isPersonAlive(affectedPerson));

        }
    }

    private java.util.Map<Action, Boolean> classifyPersonActions(Integer person) {
        java.util.Map<Action, Boolean> classifiedActions = new HashMap<Action, Boolean>();
        for (Action action : personSet.getPerson(person).getActions()) {
            classifiedActions.put(action, definitions.isActionEnabled(person, action));
        }
        return classifiedActions;
    }

    private Collection<Coordinate> buildMovimentRange(Integer person) {
        return buildMovimentRange(
                person,
                personSet.getPerson(person).getPosition(),
                personSet.getPerson(person).getMoviment());
    }

    private Set<Coordinate> buildMovimentRange(Integer person, Coordinate current, int movimentLeft) {
        Set<Coordinate> range = new TreeSet<Coordinate>(CoordinateComparator.getInstance());

        if (map.getSquare(current).isMovimentBlocked()) {
            return range;
        }

        Integer personOnPosition = personSet.getPersonOnPosition(current);

        if (personOnPosition != null && !person.equals(personOnPosition)) {
            if (definitions.isEnemy(person, personOnPosition)) {
                return range;
            }
        } else {
            range.add(current);
        }

        if (movimentLeft > 0) {
            for (Coordinate neighbor : findMapNeighbors(current, 1, 1)) {
                range.addAll(buildMovimentRange(person, neighbor, movimentLeft - 1));
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
    private Set<Coordinate> buildActionRange(Integer person, Action action) {
        Set<Coordinate> range = new TreeSet<Coordinate>(CoordinateComparator.getInstance());

        for (int distance = action.getReach().getMinimum();
                distance <= action.getReach().getMaximum();
                ++distance) {
            for (int angle = 0; angle < distance * 4; angle++) {
                int x = (angle / (distance * 2)) % 2 == 0
                        ? -distance + angle % (distance * 2)
                        : distance - angle % (distance * 2);
                int y = ((angle + distance) / (distance * 2)) % 2 == 0
                        ? -distance + (angle + distance) % (distance * 2)
                        : distance - (angle + distance) % (distance * 2);

                if (inMap(x, y)) {
                    range.add(new CoordinateImpl(x, y));
                }
            }
        }

        if (action.getReach().getDirect()) {
            Set<Coordinate> onSight = new TreeSet<Coordinate>(CoordinateComparator.getInstance());

            for (Coordinate coordinate : range) {
                if (onSight(personSet.getPerson(person).getPosition(), coordinate)) {
                    onSight.add(coordinate);
                }
            }

            return onSight;
        } else {
            return range;
        }

    }

    private Collection<Integer> findAffectedActionPersons(Action action, Coordinate target) {
        Set<Integer> affectedPersons = new HashSet<Integer>();
        for (Coordinate coordinate : buildActionReachRay(action, target)) {
            Integer person = personSet.getPersonOnPosition(coordinate);
            if (person != null) {
                affectedPersons.add(person);
            }
        }

        return affectedPersons;
    }

    private Collection<Coordinate> buildActionReachRay(Action action, Coordinate target) {
        return findMapNeighbors(target, 0, action.getReach().getRay());
    }

    private Collection<Coordinate> findMapNeighbors(Coordinate current, int minimumRay, Integer maximumRay) {
        Set<Coordinate> neighbors = new TreeSet<Coordinate>(CoordinateComparator.getInstance());

        for (int ray = minimumRay;
                ray <= maximumRay;
                ++ray) {
            for (int angle = 0; angle < ray * 4; angle++) {
                int x = Math.integerCosine(ray, angle);
                int y = Math.integerSine(ray, angle);

                if (inMap(x, y)) {
                    neighbors.add(new CoordinateImpl(x, y));
                }
            }
        }
        return neighbors;
    }

    private boolean inMap(Coordinate coordinate) {
        return inMap(coordinate.getX(), coordinate.getY());
    }

    private boolean inMap(int x, int y) {
        return x >= 0
                && x < map.getWidth()
                && y >= 0
                && y < map.getHeight();
    }

    private boolean onSight(Coordinate c1, Coordinate c2) {
        for (Coordinate coordinate : Math.betweenCoordinates(c1, c2)) {
            if (map.getSquare(coordinate).isActionBlocked()
                    || personSet.getPersonOnPosition(coordinate) != null) {
                return false;
            }
        }

        return true;
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

        private Collection<Integer> getPlayerAlivePersons(Integer playerId) {
            List<Integer> alivePersons = new LinkedList<Integer>();
            for (Integer personId : personSet.getPersonsByPlayer(playerId)) {
                if (definitions.isPersonAlive(personId)) {
                    alivePersons.add(personId);
                }
            }
            return alivePersons;
        }

        private List<Integer> orderPersonsToAct() {
            List<Integer> orderedObjects = orderReverseObjectsByPersonsCount(
                    getAlivePlayers());
            List<Integer> orderedPersons = new LinkedList<Integer>();

            for (Integer playerId : orderedObjects) {
                List<Integer> playerPersons = new LinkedList<Integer>(getPlayerAlivePersons(playerId));
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

        private List<Integer> orderReverseObjectsByPersonsCount(Collection<Integer> players) {
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
    }

    private class Definitions {

        private boolean isPersonAlive(Integer person) {
            return personSet.getPerson(person).getHealthPoints() <= 0;
        }

        private boolean isPlayerAlive(Integer player) {
            return !finders.getPlayerAlivePersons(player).isEmpty();
        }

        private boolean isActionEnabled(Integer person, Action action) {
            return personSet.getPerson(person).getSpecialPoints() >= action.costSpecialPoints();
        }

        private boolean isEnemy(Integer person, Integer otherPerson) {
            return !personSet.getPerson(person).getPlayer().equals(
                    personSet.getPerson(otherPerson).getPlayer());
        }

        private boolean hits(Action action, Integer affectedPerson) {
            return dice(action.getAccuracy()) >= dice(personSet.getPerson(affectedPerson).getEvasiveness());
        }

        private int damage(Action action, Integer affectedPerson) {
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

        private int actionLostSpecialPoints(Integer agentPerson, Action action) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private int actionLostHealthPoints(Integer agentPerson, Action action) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private enum PersonActPhase {

        Moviment,
        ChooseAction,
        ChooseTarget,
        Confirm
    }

    private static class CoordinateImpl implements Map.Coordinate {

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
            return y;
        }

        @Override
        public Coordinate clone() {
            return new CoordinateImpl(x, y);
        }
    }

    private static class CoordinateComparator implements Comparator<Coordinate> {

        private static final CoordinateComparator instance = new CoordinateComparator();

        public static CoordinateComparator getInstance() {
            return instance;
        }

        public int compare(Coordinate c1, Coordinate c2) {
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
        private java.util.Map<Coordinate, Integer> mapPersons =
                new TreeMap<Coordinate, Integer>(CoordinateComparator.getInstance());
        private java.util.Map<Integer, Collection<Integer>> playerPersons =
                new HashMap<Integer, Collection<Integer>>();

        private void addPerson(Integer personId, Person person, Coordinate position, Integer playerId) {
            persons.put(personId, new PersonData(person));
            persons.get(personId).setPlayer(playerId);
            persons.get(personId).setPosition(position);
        }

        public Integer getPersonOnPosition(Coordinate position) {
            return mapPersons.get(position);
        }

        private Iterable<Integer> getPlayers() {
            return playerPersons.keySet();
        }

        private Iterable<Integer> getPersonsByPlayer(Integer playerId) {
            return playerPersons.get(playerId);
        }

        private PersonData getPerson(Integer personId) {
            return persons.get(personId);
        }

        public class PersonData {

            private Integer id;
            private Integer playerId;
            private int healthPoints;
            private int specialPoints;
            private Person person;
            private Coordinate position;

            public PersonData(Person person) {
                this.person = person.clone();
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

            private void setPosition(Coordinate position) {
                if (this.position != null) {
                    mapPersons.remove(this.position);
                }

                this.position = position;
                mapPersons.put(position, id);
            }

            private void setPlayer(Integer playerId) {
                if (this.playerId != null) {
                    playerPersons.get(this.playerId).remove(this.id);
                }

                if (playerPersons.get(playerId) == null) {
                    playerPersons.put(playerId, new HashSet<Integer>());
                }

                this.playerId = playerId;
                playerPersons.get(playerId).add(this.id);
            }

            private Coordinate getPosition() {
                return position;
            }

            private int getEvasiveness() {
                return person.getEvasiveness();
            }

            private int getResistence() {
                return person.getResistence();
            }

            private void subtractHealthPoints(int lostHealthPoints) {
                throw new UnsupportedOperationException("Not yet implemented");
            }

            private void subtractSpecialPoints(int lostSpecialPoints) {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }

    private static class Math {

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

        private static Iterable<Coordinate> betweenCoordinates(Coordinate c0, Coordinate c1) {
            Set<Coordinate> coordinates = new TreeSet<Coordinate>(CoordinateComparator.getInstance());

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
}
