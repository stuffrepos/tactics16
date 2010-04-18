package tactics16.scenes.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import tactics16.game.Coordinate;
import tactics16.game.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleGame {

    private Map map;
    private List<Player> players = new ArrayList<Player>();

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void clearPlayers() {
        players.clear();
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

    public Set<Coordinate> calculateTargetActionRayArea(Coordinate target, int ray) {
        Set<Coordinate> area = new TreeSet<Coordinate>();

        for (java.util.Map.Entry<Coordinate, Integer> e : map.calculateActionDistances(target).entrySet()) {
            if (e.getValue() <= ray) {
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
}
