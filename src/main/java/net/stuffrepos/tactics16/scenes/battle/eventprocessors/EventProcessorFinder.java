package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import net.stuffrepos.tactics16.battleengine.BattleEvent;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import org.reflections.Reflections;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class EventProcessorFinder<EventProcessorType extends EventProcessor> {

    private final Map<Class, EventProcessorType> processors;

    public EventProcessorFinder(String packageName, Class<? extends EventProcessor> eventProcessorSuperClass) {
        processors = new HashMap<Class, EventProcessorType>();

        for (Class<? extends EventProcessorType> eventProcessorClass : new Class[]{eventProcessorSuperClass}) {
            Reflections reflections = new Reflections(packageName);
            for (Class<? extends EventProcessorType> clazz : reflections.getSubTypesOf(eventProcessorClass)) {
                System.out.println(clazz.getName());
                try {
                    EventProcessorType processor = instantiateProcessor(clazz);
                    processors.put(processor.getEventClass(), processor);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    protected abstract EventProcessorType instantiateProcessor(Class<? extends EventProcessorType> clazz)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    public EventProcessorType getEventProcessor(BattleEvent event) {
        EventProcessorType processor = processors.get(event.getClass());

        if (processor == null) {
            throw new RuntimeException("Processor not found for " + event.getClass());
        } else {
            return processor;
        }
    }
}
