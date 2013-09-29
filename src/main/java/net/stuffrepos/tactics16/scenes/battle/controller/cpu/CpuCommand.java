package net.stuffrepos.tactics16.scenes.battle.controller.cpu;

import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.game.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuCommand {

    private final Map.MapCoordinate movimentTarget;
    private final Action action;
    private final Map.MapCoordinate actionTarget;

    public CpuCommand(Map.MapCoordinate movimentTarget, Action action, Map.MapCoordinate actionTarget) {
        this.movimentTarget = movimentTarget;
        this.action = action;
        this.actionTarget = actionTarget;
    }

    public Map.MapCoordinate getActionTarget() {
        return actionTarget;
    }

    public Map.MapCoordinate getMovimentTarget() {
        return movimentTarget;
    }

    public Action getAction() {
        return action;
    }
}
