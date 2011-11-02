package net.stuffrepos.tactics16.phase;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class Phase extends BasicGameState {
    private static int idCounter = 0;
    private final int id = ++idCounter;
    private boolean initialized;

    protected void initResources(GameContainer container, StateBasedGame game) throws SlickException {
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        if (!initialized) {
            initResources(container, game);
            initialized = true;
        }
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
    }

    @Override
    public int getID() {
        return id;
    }
}
