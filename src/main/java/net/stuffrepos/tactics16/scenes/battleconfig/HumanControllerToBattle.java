package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.controller.human.HumanPlayerController;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class HumanControllerToBattle implements ControllerToBattle{

    public String getName() {
        return "Human";
    }

    public PlayerController newPlayerControl() {
        return new HumanPlayerController();
    }
    
}
