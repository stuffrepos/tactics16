package tactics16.phase;

import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Phase {

    public void onAdd();

    public void onRemove();

    public void update(long elapsedTime);

    public void render(Graphics2D g);

    public void onExit();

    public void onEnter();
}
