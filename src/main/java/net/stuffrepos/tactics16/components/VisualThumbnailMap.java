package net.stuffrepos.tactics16.components;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.image.Thumbnail;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualThumbnailMap {

    public static final int THUMBNAIL_TERRAIN_SIZE = Map.TERRAIN_SIZE / 2;
    private Coordinate position = new Coordinate();
    private Map map;
    private CacheableValue<Image> thumbnail = new CacheableValue<Image>() {

        @Override
        protected Image calculate() {
            try {
                Image thumb = new Image(
                        THUMBNAIL_TERRAIN_SIZE * getMap().getWidth(),
                        THUMBNAIL_TERRAIN_SIZE * getMap().getHeight());
                final Graphics g = thumb.getGraphics();
                getMap().iterate(new Map.Iterator() {

                    public void check(int x, int y, Terrain terrain) {
                        Image terrainThumbnail = Thumbnail.getThumbnail(terrain.getImages().get(0).getImage(), THUMBNAIL_TERRAIN_SIZE, THUMBNAIL_TERRAIN_SIZE, null);                        
                        g.drawImage(terrainThumbnail, THUMBNAIL_TERRAIN_SIZE * x, THUMBNAIL_TERRAIN_SIZE * y, null);
                    }
                });
                g.destroy();
                return thumb;
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
