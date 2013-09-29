package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.battleengine.BattleEvent;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.util.ObjectProcessorFinder;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class EventProcessorFinder<EventProcessorType extends EventProcessor> extends ObjectProcessorFinder<BattleEvent, EventProcessorType> {

    private CacheableMapValue<Class<? extends EventProcessorType>, EventProcessorType> processors = new CacheableMapValue<Class<? extends EventProcessorType>, EventProcessorType>() {
        @Override
        protected EventProcessorType calculate(Class<? extends EventProcessorType> key) {
            return instantiateProcessor(key);
        }
    };

    public EventProcessorFinder(String packageName, Class<? extends EventProcessorType> eventProcessorSuperClass) {
        super(packageName, eventProcessorSuperClass);
    }

    protected abstract EventProcessorType instantiateProcessor(
            Class<? extends EventProcessorType> clazz);

    public EventProcessorType getEventProcessor(BattleEvent event) {
        return processors.getValue(getEventProcessorClass(event));
    }
}
