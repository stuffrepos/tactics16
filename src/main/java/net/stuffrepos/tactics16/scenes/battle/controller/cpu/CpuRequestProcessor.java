package net.stuffrepos.tactics16.scenes.battle.controller.cpu;

import net.stuffrepos.tactics16.battleengine.BattleEvent;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.RequestProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CpuRequestProcessor<EventType extends BattleEvent, ReturnType> extends RequestProcessor<EventType, ReturnType> {

    private final CpuCommand cpuCommand;

    public CpuRequestProcessor(BattleScene battleScene, CpuCommand cpuCommand) {
        super(battleScene);
        this.cpuCommand = cpuCommand;
    }

    public Phase init(final EventType event) {
        return new Phase() {
            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                answer(buildAnswer(event));
            }
        };
    }
    
    protected CpuCommand getCpuCommand() {
        return cpuCommand;
    }

    protected abstract ReturnType buildAnswer(EventType event);
}
