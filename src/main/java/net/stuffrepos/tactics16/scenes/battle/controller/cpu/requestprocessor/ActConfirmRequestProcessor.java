package net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor;

import net.stuffrepos.tactics16.battleengine.events.ActConfirmRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuRequestProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActConfirmRequestProcessor extends CpuRequestProcessor<ActConfirmRequest, Boolean> {

    public ActConfirmRequestProcessor(final BattleScene battleScene, CpuCommand cpuCommand) {
        super(battleScene, cpuCommand);
    }

    @Override
    public Class getEventClass() {
        return ActConfirmRequest.class;
    }

    @Override
    protected Boolean buildAnswer(ActConfirmRequest event) {
        return true;
    }
}
