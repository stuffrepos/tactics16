package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class PlayerToBattle extends DataObject {

    private final PlayerConfig playerConfig;
    private List<PersonToBattle> persons = new LinkedList<PersonToBattle>();
    private final ControllerToBattle controller;

    public PlayerToBattle(PlayerConfig playerConfig, ControllerToBattle controller, String name) {
        super(name);
        this.playerConfig = playerConfig;
        this.controller = controller;
    }

    public Collection<PersonToBattle> getPersons() {
        return persons;
    }

    public void addPerson(PersonToBattle personToBattle) {
        persons.add(personToBattle);
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public ControllerToBattle getController() {
        return this.controller;
    }


}
