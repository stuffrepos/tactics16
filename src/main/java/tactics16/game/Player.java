package tactics16.game;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Player extends DataObject {

    private List<Person> persons = new ArrayList<Person>();

    public Player(String name) {
        super(name);
    }

    public List<Person> getPersons() {
        return persons;
    }
}
