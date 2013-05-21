package net.stuffrepos.tactics16.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import org.newdawn.slick.Graphics;

/**
 *
 * @author eduardo
 */
public class EntitiesSequence implements VisualEntity {

    private final Iterator<EntityBuilder> entitiesIterator;
    private VisualEntity current;

    public EntitiesSequence(EntityBuilder... entities) {
        entitiesIterator = Arrays.asList(entities).iterator();
        current = entitiesIterator.hasNext()
                ? entitiesIterator.next().buildEntity()
                : null;
    }

    public void update(int delta) {
        if (current == null || current.isFinalized()) {
            current = entitiesIterator.hasNext()
                    ? entitiesIterator.next().buildEntity()
                    : null;
        }

        if (current != null) {
            current.update(delta);
        }
    }

    public void render(Graphics g) {
        if (current != null && !current.isFinalized()) {
            current.render(g);
        }
    }

    public boolean isFinalized() {
        return current == null;
    }
    
    public static interface EntityBuilder {
        public VisualEntity buildEntity();
    }
}
