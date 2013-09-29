package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuPlayerController;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuIa;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuControllerToBattle implements ControllerToBattle {
    
    private final CpuIa ia;

    public CpuControllerToBattle(CpuIa ia) {        
        this.ia = ia;
    }

    public String getName() {
        return "CPU - " + ia.getName();
    }

    public PlayerController newPlayerControl() {
        return new CpuPlayerController(ia);
    }
    
}
