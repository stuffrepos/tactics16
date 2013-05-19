package net.stuffrepos.tactics16.scenes.battle;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.stuffrepos.tactics16.battleengine.BattleEvent;
import org.reflections.Reflections;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EventProcessorFinder {

    private final Map<Class, EventProcessor> processors;

    public EventProcessorFinder(BattleScene battleScene) {
        processors = new HashMap<Class, EventProcessor>();

        for (Class<? extends EventProcessor> eventProcessorClass : new Class[]{EventProcessor.class, RequestProcessor.class}) {
            Reflections reflections = new Reflections("net.stuffrepos.tactics16.scenes.battle.eventprocessor");
            for (Class<? extends EventProcessor> clazz : reflections.getSubTypesOf(eventProcessorClass)) {
                System.out.println(clazz.getName());
                try {
                    EventProcessor processor = (EventProcessor) clazz.getConstructor(BattleScene.class).newInstance(battleScene);
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

    public EventProcessor getEventProcessor(BattleEvent event) {
        EventProcessor processor = processors.get(event.getClass());

        if (processor == null) {
            throw new RuntimeException("Processor not found for " + event.getClass());
        } else {
            return processor;
        }




    }
}
