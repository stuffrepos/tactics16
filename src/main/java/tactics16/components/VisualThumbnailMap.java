package tactics16.components;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import tactics16.game.Coordinate;
import tactics16.game.Map;
import tactics16.game.Terrain;
import tactics16.util.cache.CacheableValue;
import tactics16.util.image.Thumbnail;

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
            BufferedImage thumb = new BufferedImage(
                    THUMBNAIL_TERRAIN_SIZE * getMap().getWidth(),
                    THUMBNAIL_TERRAIN_SIZE * getMap().getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            final Graphics2D g = thumb.createGraphics();
            getMap().iterate(new Map.Iterator() {

                public void check(int x, int y, Terrain terrain) {
                    BufferedImage terrainThumbnail = Thumbnail.getThumbnail(terrain.getImages().get(0).getImage(), THUMBNAIL_TERRAIN_SIZE, THUMBNAIL_TERRAIN_SIZE, null);
                    g.drawImage(terrainThumbnail, THUMBNAIL_TERRAIN_SIZE * x, THUMBNAIL_TERRAIN_SIZE * y, null);
                }
            });
            g.dispose();
            return thumb;
        }
    };

    public void setMap(Map map) {
        this.map = map;
        this.thumbnail.clear();
    }

    public void render(Graphics2D g) {
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
