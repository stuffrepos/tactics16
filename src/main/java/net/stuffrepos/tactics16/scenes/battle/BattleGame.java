package net.stuffrepos.tactics16.scenes.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.stuffrepos.tactics16.battleengine.BattleData;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battleconfig.PersonToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.PlayerToBattle;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleGame {

    private final Map map;
    private final List<Player> players;
    private final BattleEngine battleEngine;

    public BattleGame(Map map, List<PlayerToBattle> playersToBattle) {
        assert map != null : "map is null";
        assert playersToBattle != null;
        assert playersToBattle.size() >= 2;
        this.map = map;
        
        this.players = new ArrayList<Player>(playersToBattle.size());
        java.util.Map<Integer, net.stuffrepos.tactics16.battleengine.EnginePersonConfig> persons = new HashMap<Integer, net.stuffrepos.tactics16.battleengine.EnginePersonConfig>();
        java.util.Map<Integer, Integer> personsPlayers = new HashMap<Integer, Integer>();
        java.util.Map<Integer, net.stuffrepos.tactics16.battleengine.Map.MapCoordinate> personsPositions = new HashMap<Integer, net.stuffrepos.tactics16.battleengine.Map.MapCoordinate>();

        int playerId = 0;
        int personId = 0;
        for (PlayerToBattle playerToBattle : playersToBattle) {
            int playerPersonId = 0;
            Player player = new Player(playerToBattle.getPlayerConfig(), playerToBattle.getController().newPlayerControl());
            for (PersonToBattle personToBattle : playerToBattle.getPersons()) {
                persons.put(personId, personToBattle);
                personsPlayers.put(personId, playerId);
                personsPositions.put(personId, getPersonInitialPosition(playerId, playerPersonId));
                player.addPerson(new Person(this, player, personToBattle, personId));
                playerPersonId++;
                personId++;
            }
            players.add(player);
            playerId++;
        }

        battleEngine = new BattleEngine(
                new BattleData(
                map, persons, personsPlayers, personsPositions));
    }

    public Map getMap() {
        return map;
    }

    public Iterable<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players.get(index);
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
        Integer personId = getEngine().getPersonOnPosition(mapPosition);
        return personId != null
                ? getPerson(personId)
                : null;
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
            if (map.inMap(neighboor) && !map.getSquare(neighboor).isMovimentBlocked()) {
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

    public BattleEngine getEngine() {
        return battleEngine;
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

    public Iterable<Person> getPersons() {
        List<Person> persons = new LinkedList<Person>();
        for (Player player : players) {
            for (Person person : player.getPersons()) {
                persons.add(person);
            }
        }
        return persons;
    }
}
