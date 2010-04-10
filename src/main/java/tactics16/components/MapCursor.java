package tactics16.components;

import tactics16.game.Map;
import tactics16.util.Cursor2D;
import tactics16.util.listeners.Listener;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapCursor {

    private final VisualMap visualMap;    
    private Cursor2D cursor;
    private GlowingRectangle visualCursor;

    public MapCursor(VisualMap visualMap) {
        this.visualMap = visualMap;        
        this.visualCursor = new GlowingRectangle(Map.TERRAIN_SIZE);
        this.cursor = new Cursor2D();
        this.cursor.setDimension(this.visualMap.getMap().getWidth(), this.visualMap.getMap().getHeight());
        this.cursor.setKeys(
                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        this.cursor.addListener(new Listener<Cursor2D>() {

            public void onChange(Cursor2D source) {
                MapCursor.this.visualCursor.getPosition().setXY(
                        MapCursor.this.visualMap.getPosition().getX() + Map.TERRAIN_SIZE * source.getX(),
                        MapCursor.this.visualMap.getPosition().getY() + Map.TERRAIN_SIZE * source.getY());
            }
        });
    }

    public void update(long elapsedTime) {
        cursor.update(elapsedTime);
        visualCursor.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        visualCursor.render(g);
    }

    public Cursor2D getCursor() {
        return cursor;
    }
}
