package net.stuffrepos.tactics16.battleengine;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import static net.stuffrepos.tactics16.battleengine.BattleEngine.ACT_SPEED_POINTS_COST;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleData implements Cloneable {

    private static final Log log = LogFactory.getLog(BattleData.class);
    public static final int INITIAL_SPECIAL_POINTS = 0;
    private final Map map;
    private final Finders finders = new Finders();
    private final PersonSet personSet = new PersonSet();
    private final Definitions definitions = new Definitions();

    public BattleData(Map map, java.util.Map<Integer, EnginePersonConfig> persons, java.util.Map<Integer, Integer> personsPlayers, java.util.Map<Integer, Map.MapCoordinate> personsPositions) {
        assert map != null;
        assert persons.size() >= 2 : "persons.size(): " + persons.size();
        assert personsPlayers.size() == persons.size();
        assert personsPositions.size() == persons.size();

        this.map = map;

        for (java.util.Map.Entry<Integer, EnginePersonConfig> e : persons.entrySet()) {
            this.personSet.addPerson(
                    e.getKey(),
                    e.getValue(),
                    personsPositions.get(e.getKey()).clone(),
                    personsPlayers.get(e.getKey()));
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Finders getFinders() {
        return finders;
    }

    public PersonSet getPersonSet() {
        return personSet;
    }

    public Map getMap() {
        return map;
    }

    public Definitions getDefinitions() {
        return definitions;
    }

    public class PersonSet {

        private java.util.Map<Integer, PersonData> persons =
                new HashMap<Integer, PersonData>();
        private java.util.Map<Map.MapCoordinate, Integer> mapPersons =
                new TreeMap<Map.MapCoordinate, Integer>(CoordinateComparator.getInstance());
        private java.util.Map<Integer, Collection<Integer>> playerPersons =
                new HashMap<Integer, Collection<Integer>>();

        private void addPerson(int personId, EnginePersonConfig person, Map.MapCoordinate position, int playerId) {
            persons.put(personId, new PersonData(person, personId));
            persons.get(personId).setPlayer(playerId);
            persons.get(personId).setPosition(position);
        }

        public Integer getPersonOnPosition(Map.MapCoordinate position) {
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

        public PersonData getPerson(int personId) {
            assert persons.get(personId) != null : "Person id: " + personId;
            return persons.get(personId);
        }

        public Iterable<Integer> getPersons() {
            return persons.keySet();
        }

        public class PersonData implements EnginePerson {

            private final int id;
            private Integer playerId;
            private int healthPoints;
            private int specialPoints;
            private float speedPoints;
            private final EnginePersonConfig person;
            private Map.MapCoordinate position;

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

            public int getMoviment() {
                return person.getMoviment();
            }

            public Iterable<Action> getActions() {
                return person.getActions();
            }

            private Integer getPlayer() {
                return playerId;
            }

            public void setPosition(Map.MapCoordinate position) {
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

            public Map.MapCoordinate getPosition() {
                return position;
            }

            public int getEvasiveness() {
                return person.getEvasiveness();
            }

            private int getResistence() {
                return person.getResistence();
            }

            public void subtractHealthPoints(int lostHealthPoints) {
                this.healthPoints = java.lang.Math.max(0,
                        this.healthPoints - lostHealthPoints);
            }

            public void subtractSpecialPoints(int lostSpecialPoints) {
                this.specialPoints = java.lang.Math.max(0,
                        this.specialPoints - lostSpecialPoints);
            }

            public float getSpeedPoints() {
                return speedPoints;
            }

            public void subtractSpeedPoints(float lostSpeedPoints) {
                this.speedPoints -= lostSpeedPoints;
            }

            public float getSpeed() {
                return person.getSpeed();
            }

            public void renewSpeedPoints() {
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

            public void increaseSpecialPoints(int increment) {
                assert increment >= 0;
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

    public class Definitions {

        public boolean isPersonAlive(int person) {
            return personSet.getPerson(person).getHealthPoints() > 0;
        }

        private boolean isPlayerAlive(int player) {
            return !finders.getPlayerAlivePersons(player).isEmpty();
        }

        public boolean isActionEnabled(int person, Action action) {
            return personSet.getPerson(person).getSpecialPoints()
                    + personSet.getPerson(person).getHealthPoints() >= action.getSpecialPointsCost();
        }

        public boolean isEnemy(int person, int otherPerson) {
            return !personSet.getPerson(person).getPlayer().equals(
                    personSet.getPerson(otherPerson).getPlayer());
        }

        public boolean hits(Action action, int affectedPerson) {
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

        public int damage(Action action, int affectedPerson) {
            return java.lang.Math.max(action.getPower() - personSet.getPerson(affectedPerson).getResistence(), 1);
        }

        public int actionLostSpecialPoints(int agentPerson, Action action) {
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
        public int actionLostHealthPoints(int agentPerson, Action action) {
            if (action == null) {
                return 0;
            } else {
                return -java.lang.Math.min(0, personSet.getPerson(agentPerson).getSpecialPoints() - action.getSpecialPointsCost());
            }
        }

        public float actionLostSpeedPoints(Integer agentPerson, Action action) {
            return action == null ? ACT_SPEED_POINTS_COST / 2.0f : ACT_SPEED_POINTS_COST;
        }
    }

    public class Finders {

        public Collection<Integer> getAlivePlayers() {
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

        public Iterable<Integer> getAlivePersons() {
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

        public Integer getWinnerPlayer() {
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
        public Integer nextPersonToAct() {
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

        public Collection<Integer> findAffectedActionPersons(Action action, Map.MapCoordinate target) {
            Set<Integer> affectedPersons = new HashSet<Integer>();
            if (action != null) {
                for (Map.MapCoordinate coordinate : buildActionReachRay(action, target)) {
                    Integer person = personSet.getPersonOnPosition(coordinate);
                    if (person != null) {
                        affectedPersons.add(person);
                    }
                }
            }

            return affectedPersons;
        }

        public Collection<Map.MapCoordinate> buildActionReachRay(Action action, Map.MapCoordinate target) {
            return findMapNeighbors(
                    target, action.getReach().getRayMin(), action.getReach().getRayMax());
        }

        public Set<Map.MapCoordinate> findMapNeighbors(Map.MapCoordinate current, int minimumRay, Integer maximumRay) {
            Set<Map.MapCoordinate> neighbors = new TreeSet<Map.MapCoordinate>(BattleData.CoordinateComparator.getInstance());

            for (int ray = minimumRay;
                    ray <= maximumRay;
                    ++ray) {
                if (ray == 0) {
                    if (inMap(current)) {
                        neighbors.add(current);
                    }
                } else {
                    for (int angle = 0; angle < ray * 4; angle++) {
                        int cosine = BattleData.Math.integerCosine(ray, angle);
                        int sine = BattleData.Math.integerSine(ray, angle);
                        int x = current.getX() + cosine;
                        int y = current.getY() + sine;

                        if (inMap(x, y)) {
                            neighbors.add(new BattleData.CoordinateImpl(x, y));
                        }
                    }
                }
            }

            return neighbors;
        }

        private boolean inMap(Map.MapCoordinate coordinate) {
            return inMap(coordinate.getX(), coordinate.getY());
        }

        private boolean inMap(int x, int y) {
            return x >= 0
                    && x < map.getWidth()
                    && y >= 0
                    && y < map.getHeight();
        }
    }

    public static class CoordinateImpl implements Map.MapCoordinate {

        final int x;
        final int y;

        public CoordinateImpl(int x, int y) {
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
        public Map.MapCoordinate clone() {
            return new CoordinateImpl(x, y);
        }
    }

    public static class CoordinateComparator implements Comparator<Map.MapCoordinate> {

        private static final CoordinateComparator instance = new CoordinateComparator();

        public static CoordinateComparator getInstance() {
            return instance;
        }

        public int compare(Map.MapCoordinate c1, Map.MapCoordinate c2) {
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

        public static Iterable<Map.MapCoordinate> betweenCoordinates(Map.MapCoordinate c0, Map.MapCoordinate c1) {
            Set<Map.MapCoordinate> coordinates = new TreeSet<Map.MapCoordinate>(CoordinateComparator.getInstance());

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

        public static int coordinatesDistance(Map.MapCoordinate c1, Map.MapCoordinate c2) {
            return java.lang.Math.abs(c1.getX() - c2.getX())
                    + java.lang.Math.abs(c1.getY() - c2.getY());
        }
    }
}
