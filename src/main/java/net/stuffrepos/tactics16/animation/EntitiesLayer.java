package net.stuffrepos.tactics16.animation;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EntitiesLayer implements VisualEntity {

    private List<VisualEntity> children = new LinkedList<VisualEntity>();

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

    public void addEntity(VisualEntity entity) {
        children.add(entity);
    }
}
