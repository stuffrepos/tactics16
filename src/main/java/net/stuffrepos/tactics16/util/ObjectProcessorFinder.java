package net.stuffrepos.tactics16.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ObjectProcessorFinder<ObjectType, ProcessorType> {

    private static final Log log = LogFactory.getLog(ObjectProcessorFinder.class);
    public static final String GET_OBJECT_CLASS_METHOD_NAME = "getObjectClass";
    private final Map<Class<? extends ObjectType>, Class<? extends ProcessorType>> processors;

    public ObjectProcessorFinder(String packageName,
            Class<? extends ProcessorType> eventProcessorSuperClass) {
        processors = new HashMap<Class<? extends ObjectType>, Class<? extends ProcessorType>>();

        if (log.isDebugEnabled()) {
            log.debug("Searching in package " + packageName);
        }

        for (Class<? extends ProcessorType> eventProcessorClass : new Class[]{eventProcessorSuperClass}) {
            Reflections reflections = new Reflections(packageName);
            for (Class<? extends ProcessorType> processorClass : reflections.getSubTypesOf(eventProcessorClass)) {
                if (log.isDebugEnabled()) {
                    log.debug("Processor found: " + processorClass.getName());
                }
                try {
                    Method getEventClassMethod = processorClass.getMethod(GET_OBJECT_CLASS_METHOD_NAME);
                    Class objectClass = (Class) getEventClassMethod.invoke(null);
                    if (log.isDebugEnabled()) {
                        log.debug("Object found: " + objectClass.getName());
                    }
                    processors.put(objectClass, processorClass);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

    public Class<? extends ProcessorType> getEventProcessorClass(ObjectType object) {
        Class<? extends ProcessorType> clazz = processors.get(object.getClass());
        if (clazz == null) {
            throw new IllegalArgumentException("Processor not found for " + object.getClass().getName());
        }
        return clazz;
    }
}
