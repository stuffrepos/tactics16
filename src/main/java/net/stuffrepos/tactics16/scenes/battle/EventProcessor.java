package net.stuffrepos.tactics16.scenes.battle;

import net.stuffrepos.tactics16.battlegameengine.BattleEvent;
import net.stuffrepos.tactics16.phase.Phase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class EventProcessor<EventType extends BattleEvent> {

    private final BattleScene battleScene;

    public EventProcessor(BattleScene battleScene) {
        this.battleScene = battleScene;
    }

    public BattleScene getScene() {
        return battleScene;
    }

    public abstract Phase init(EventType event);

    public abstract Class getEventClass();
}
