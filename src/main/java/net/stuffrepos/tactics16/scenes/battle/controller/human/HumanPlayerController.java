package net.stuffrepos.tactics16.scenes.battle.controller.human;

import java.lang.reflect.InvocationTargetException;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.EventProcessorFinder;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.RequestProcessor;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class HumanPlayerController implements PlayerController {

    private CacheableMapValue<BattleScene, EventProcessorFinder> eventProcessorFinders = new CacheableMapValue<BattleScene, EventProcessorFinder>() {
        @Override
        protected EventProcessorFinder calculate(final BattleScene battleScene) {
            return new EventProcessorFinder<RequestProcessor>("net.stuffrepos.tactics16.scenes.battle.controller.human.requestprocessor", RequestProcessor.class) {
                @Override
                protected RequestProcessor instantiateProcessor(Class<? extends RequestProcessor> clazz) {
                    try {
                        return clazz.getConstructor(BattleScene.class).newInstance(battleScene);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    } catch (SecurityException ex) {
                        throw new RuntimeException(ex);
                    } catch (InstantiationException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };
        }
    };

    public EventProcessor getEventProcessor(BattleScene battleScene, BattleRequest request) {
        return eventProcessorFinders.getValue(battleScene).getEventProcessor(request);
    }
}
