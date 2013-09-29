package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.PlayerControl;
import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface ControllerToBattle extends Nameable {

    public PlayerControl newPlayerControl();

}
