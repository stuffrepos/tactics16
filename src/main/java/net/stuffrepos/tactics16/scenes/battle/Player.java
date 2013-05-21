package net.stuffrepos.tactics16.scenes.battle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Player {

    private final PlayerConfig playerConfig;
    private final List<Person> persons = new LinkedList<Person>();

    Player(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public Collection<Person> getPersons() {
        return persons;
    }
    
    public Person getPerson(int index) {
        return persons.get(index);
    }
}
