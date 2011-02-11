package net.stuffrepos.tactics16.animation;

import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface VisualEntity {

    public void update(long elapsedTime);
    public void render(Graphics2D g);
    public boolean isFinalized();    
}
