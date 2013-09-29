package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.CpuPlayerControl;
import net.stuffrepos.tactics16.scenes.battle.PlayerControl;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuControllerToBattle implements ControllerToBattle{

    public String getName() {
        return "CPU";
    }

    public PlayerControl newPlayerControl() {
        return new CpuPlayerControl();
    }
    
}
