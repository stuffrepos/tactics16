package net.stuffrepos.tactics16.components;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualThumbnailMap {

    private static final float THUMBNAIL_SCALE = 0.5f;
    private Coordinate position = new Coordinate();
    private Map map;
    private CacheableValue<Image> thumbnail = new CacheableValue<Image>() {

        @Override
        protected Image calculate() {
            try {
                VisualMap visualMap = new VisualMap(map); 
                Image original = new Image(
                        visualMap.getWidth(),
                        visualMap.getHeight());
                final Graphics g = original.getGraphics();
                visualMap.render(g, true, null);
                g.destroy();
                return original.getScaledCopy(THUMBNAIL_SCALE);
            } catch (SlickException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public void setMap(Map map) {
        this.map = map;
        this.thumbnail.clear();
    }

    public void render(Graphics g) {
        if (getMap() != null) {
            g.drawImage(thumbnail.getValue(), getPosition().getX(), getPosition().getY(), null);
        }
    }

    public Coordinate getPosition() {
        return position;
    }

    public Map getMap() {
        return map;
    }
}
