package net.stuffrepos.tactics16.util.phase;

import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualEntitiesPhase extends Phase {

    List<VisualEntity> entities = new LinkedList<VisualEntity>();

    public void addEntity(VisualEntity entity) {
        entities.add(entity);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);

        for (VisualEntity entity : entities) {
            entity.update(delta);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);

        for (VisualEntity entity : entities) {
            entity.render(g);
        }
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
    }
    
    
}
