package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.battleengine.BattleEvent;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class RequestProcessor<EventType extends BattleEvent, ReturnType> extends EventProcessor<EventType> {

    private boolean isAnswered = false;
    private ReturnType answer;

    public RequestProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public void answer(ReturnType answer) {
        isAnswered = true;
        this.answer = answer;
        getScene().stopCurrentEventPhase();
    }

    public ReturnType getAnswer() {
        return answer;
    }
}
