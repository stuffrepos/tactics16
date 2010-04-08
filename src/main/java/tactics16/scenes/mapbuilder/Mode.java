package tactics16.scenes.mapbuilder;

import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Mode {

    public void update(long elapsedTime);

    public void render(Graphics2D g);
}
