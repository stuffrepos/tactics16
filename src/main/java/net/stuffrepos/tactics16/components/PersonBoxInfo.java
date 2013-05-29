package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.scenes.battle.Person;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonBoxInfo extends PersonOrJobBoxInfo {

    private final Person person;

    public PersonBoxInfo(Person person) {
        super(
                person.getJob(),
                person.getPlayer().getPlayerConfig(),
                "HP\nSP\nSpP",
                String.format(
                "%d/%d\n%d/%d\n%.1f/%.1f",
                person.getEnginePerson().getHealthPoints(),
                person.getEnginePerson().getMaximumHealthPoints(),
                person.getEnginePerson().getSpecialPoints(),
                person.getEnginePerson().getMaximumSpecialPoints(),
                person.getEnginePerson().getSpeedPoints(),        
                person.getEnginePerson().getSpeed()));        
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}