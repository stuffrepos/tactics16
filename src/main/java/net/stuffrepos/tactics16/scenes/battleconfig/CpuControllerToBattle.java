package net.stuffrepos.tactics16.scenes.battleconfig;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
