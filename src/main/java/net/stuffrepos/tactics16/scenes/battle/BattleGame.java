package net.stuffrepos.tactics16.scenes.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.math.Interval;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleGame {

    private Map map;
    private List<Player> players = new ArrayList<Player>();
    private CacheableValue<BattleEngine> battleGameEngine = new CacheableValue<BattleEngine>() {
        @Override
        protected BattleEngine calculate() {
            java.util.Map<Integer, net.stuffrepos.tactics16.battleengine.Person> persons = new HashMap<Integer, net.stuffrepos.tactics16.battleengine.Person>();
            java.util.Map<Integer, Integer> personsPlayers = new HashMap<Integer, Integer>();
            java.util.Map<Integer, net.stuffrepos.tactics16.battleengine.Map.MapCoordinate> personsPositions = new HashMap<Integer, net.stuffrepos.tactics16.battleengine.Map.MapCoordinate>();

            int playerId = 0;
            int personId = 0;
            for (Player player : players) {
                int playerPersonId = 0;
                for (Person person : player.getPersons()) {
                    persons.put(personId, person);
                    personsPlayers.put(personId, playerId);
                    personsPositions.put(personId, getPersonInitialPosition(playerId, playerPersonId));
                    playerPersonId++;
                    personId++;
                }
                playerId++;

            }

            return new BattleEngine(map, persons, personsPlayers, personsPositions);
        }
    };

    public BattleGame(Map map) {
        assert map != null : "map is null";
        setMap(map);
        resetPlayers();

    }

    public Map getMap() {
        return map;
    }

    private void setMap(Map map) {
        this.map = map;
    }

    public Iterable<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void resetPlayers() {
        players.clear();
        for (int i = 0; i < map.getPlayerCount(); ++i) {
            players.add(Player.getPlayer(i));
        }
    }

    public Coordinate getPersonInitialPosition(int player, int person) {
        int i = 0;
        for (Coordinate personInitialPosition : map.getPlayerInitialPosition(player)) {
            if (i == person) {
                return personInitialPosition;
            } else {
                ++i;
            }
        }

        throw new RuntimeException("Posição não encontrada para jogador/personagem: " + player + "/" + person);
    }

    public Person getPersonOnMapPosition(Coordinate mapPosition) {
        for (Player player : players) {
            for (Person person : player.getPersons()) {
                if (person.getMapPosition().equals(mapPosition)) {
                    return person;
                }
            }
        }
        return null;
    }

    public Set<Coordinate> calculateTargetActionRayArea(Coordinate target, Interval ray) {
        Set<Coordinate> area = new TreeSet<Coordinate>();

        for (java.util.Map.Entry<Coordinate, Integer> e : map.calculateActionDistances(target).entrySet()) {
            if (ray.valueIn(e.getValue())) {
                area.add(e.getKey());
            }
        }

        return area;
    }

    public Set<Person> getPersonsOnMapPositions(Collection<Coordinate> positions) {
        Set<Person> persons = new HashSet<Person>();

        for (Coordinate position : positions) {
            Person person = getPersonOnMapPosition(position);
            if (person != null) {
                persons.add(person);
            }
        }

        return persons;
    }

    public java.util.Map<Coordinate, Integer> calculateMovimentDistances(Coordinate target, Player player) {
        java.util.Map<Coordinate, Integer> distances = new TreeMap<Coordinate, Integer>();
        Set<Coordinate> visited = new TreeSet<Coordinate>();
        Set<Coordinate> current = new TreeSet<Coordinate>();

        current.add(target);
        visited.add(target);
        int n = 0;

        while (!current.isEmpty()) {
            Set<Coordinate> forTest = new TreeSet<Coordinate>();

            for (Coordinate c : current) {
                distances.put(c, n);

                for (Coordinate next : getMovimentNeighboors(c, player)) {
                    if (!visited.contains(next)) {
                        visited.add(next);
                        forTest.add(next);
                    }
                }
            }

            n++;
            current = forTest;
        }

        return distances;
    }

    public Iterable<Coordinate> getMovimentNeighboors(Coordinate c, Player player) {
        Set<Coordinate> neighboors = new TreeSet<Coordinate>();

        for (Coordinate neighboor : new Coordinate[]{
                    new Coordinate(c, 0, -1),
                    new Coordinate(c, 0, 1),
                    new Coordinate(c, -1, 0),
                    new Coordinate(c, 1, 0)
                }) {
            if (map.inMap(neighboor) && map.getTerrain(neighboor).getAllowMoviment()) {
                Person person = this.getPersonOnMapPosition(neighboor);
                if (person == null || person.getPlayer().equals(player)) {
                    neighboors.add(neighboor);
                }
            }
        }

        return neighboors;
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * x = 20
     *
     * @param source
     * @param target
     * @return
     */
    public static List<Coordinate> calculateTrajetory(Coordinate source, Coordinate target) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public BattleEngine getBattleGameEngine() {
        return battleGameEngine.getValue();
    }

    public Person getPerson(int personId) {
        int personCounter = 0;
        for (Player player : players) {
            for (Person playerPerson : player.getPersons()) {
                if (personCounter == personId) {
                    return playerPerson;
                }
                personCounter++;
            }
        }

        throw new RuntimeException("Person not found with ID " + personId);
    }
}
