package net.stuffrepos.tactics16.scenes.battle;

import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class HumanPlayerControl implements PlayerControl {

    private CacheableMapValue<BattleScene, EventProcessorFinder> eventProcessorFinders = new CacheableMapValue<BattleScene, EventProcessorFinder>() {
        @Override
        protected EventProcessorFinder calculate(BattleScene battleScene) {
            return new EventProcessorFinder(
                    battleScene,
                    "net.stuffrepos.tactics16.scenes.battle.eventprocessors.request.human");
        }
    };

    public EventProcessor getEventProcessor(BattleScene battleScene, BattleRequest request) {
        return eventProcessorFinders.getValue(battleScene).getEventProcessor(request);
    }
}
