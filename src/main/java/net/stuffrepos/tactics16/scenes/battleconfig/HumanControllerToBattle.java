package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.HumanPlayerControl;
import net.stuffrepos.tactics16.scenes.battle.PlayerControl;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class HumanControllerToBattle implements ControllerToBattle{

    public String getName() {
        return "Human";
    }

    public PlayerControl newPlayerControl() {
        return new HumanPlayerControl();
    }
    
}
