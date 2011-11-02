package net.stuffrepos.tactics16.animation;

import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface VisualEntity {

    public void update(int delta);
    public void render(Graphics g);
    public boolean isFinalized();    
}
