package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuPlayerController;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuControllerToBattle implements ControllerToBattle{

    public String getName() {
        return "CPU";
    }

    public PlayerController newPlayerControl() {
        return new CpuPlayerController();
    }
    
}
