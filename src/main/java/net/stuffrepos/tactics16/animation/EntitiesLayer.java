package net.stuffrepos.tactics16.animation;

import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.components.MapCursor;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EntitiesLayer<EntityType extends VisualEntity> implements VisualEntity {

    private List<EntityType> children = new LinkedList<EntityType>();

    public void update(int delta) {
        List<VisualEntity> toRemove = new LinkedList<VisualEntity>();
        for (VisualEntity animation : children) {
            animation.update(delta);
            if (animation.isFinalized()) {
                toRemove.add(animation);
            }
        }

        children.removeAll(toRemove);
    }

    public void render(Graphics g) {
        for (VisualEntity child : children) {
            child.render(g);
        }
    }

    public boolean isFinalized() {
        return children.isEmpty();
    }

    public void addEntity(EntityType entity) {
        children.add(entity);
    }

    public Iterable<EntityType> getChildren() {
        return children;
    }
}
