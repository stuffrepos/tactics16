package net.stuffrepos.tactics16.phase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Phase {

    public void onAdd();

    public void onRemove();

    public void update(long elapsedTime);

    public void render(Graphics g);

    public void onExit();

    public void onEnter();    
}