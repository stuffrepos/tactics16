package net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine.SelectedAction;

import net.stuffrepos.tactics16.battleengine.events.PersonActionRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuRequestProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionRequestProcessor extends CpuRequestProcessor<PersonActionRequest, SelectedAction> {

    public PersonActionRequestProcessor(final BattleScene battleScene, CpuCommand cpuCommand) {
        super(battleScene, cpuCommand);
    }

    @Override
    public Class getEventClass() {
        return PersonActionRequest.class;
    }

    @Override
    protected SelectedAction buildAnswer(PersonActionRequest event) {        
        return new SelectedAction(getCpuCommand().getAction());
    }
}
