package net.stuffrepos.tactics16.animation;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.scenes.battle.Person;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EntitiesLayer implements VisualEntity {

    private List<VisualEntity> children = new LinkedList<VisualEntity>();

    public void update(long elapsedTime) {
        List<VisualEntity> toRemove = new LinkedList<VisualEntity>();
        for (VisualEntity animation : children) {
            animation.update(elapsedTime);
            if (animation.isFinalized()) {
                toRemove.add(animation);
            }
        }

        children.removeAll(toRemove);
    }

    public void render(Graphics2D g) {
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
