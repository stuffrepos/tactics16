package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
abstract class DataObject implements Nameable {

    private String name;

    public DataObject(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
