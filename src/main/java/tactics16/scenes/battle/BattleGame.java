package tactics16.scenes.battle;

import java.util.ArrayList;
import java.util.List;
import tactics16.game.Coordinate;
import tactics16.game.Map;
import tactics16.game.Person;
import tactics16.game.Player;

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
}
