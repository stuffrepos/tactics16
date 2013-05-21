package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import net.stuffrepos.tactics16.game.Job;
import org.newdawn.slick.Color;

/**
 *
 * @author eduardo
 */
public class PlayerToBattle extends DataObject {

    private final PlayerConfig playerConfig;
    private List<PersonToBattle> persons = new LinkedList<PersonToBattle>();

    public PlayerToBattle(PlayerConfig playerConfig, String name) {
        super(name);
        this.playerConfig = playerConfig;
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


}
