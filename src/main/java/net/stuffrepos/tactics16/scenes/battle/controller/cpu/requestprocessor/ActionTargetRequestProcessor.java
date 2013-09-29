package net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor;

import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.events.ActionTargetRequest;


import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuRequestProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActionTargetRequestProcessor extends CpuRequestProcessor<ActionTargetRequest, MapCoordinate> {

    public ActionTargetRequestProcessor(final BattleScene battleScene, CpuCommand cpuCommand) {
        super(battleScene, cpuCommand);
    }

    @Override
    public Class getEventClass() {
        return ActionTargetRequest.class;
    }

    @Override
    protected MapCoordinate buildAnswer(ActionTargetRequest event) {
        return getCpuCommand().getActionTarget();
    }
}
