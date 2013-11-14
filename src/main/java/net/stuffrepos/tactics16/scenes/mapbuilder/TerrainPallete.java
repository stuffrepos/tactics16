package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.BorderRectangle;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.util.image.DrawerUtil;
import org.newdawn.slick.Color;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TerrainPallete implements Object2D {

    private final ObjectCursor1D<Terrain> cursor = new ObjectCursor1D<Terrain>();
    private final BorderRectangle spriteCursor = new BorderRectangle(Map.TERRAIN_SIZE);
    private final Coordinate position = new Coordinate();
    private final int height;
    private final Terrain.Layer layer;

    public TerrainPallete(Terrain.Layer layer, final int height) {
        this.height = height;
        this.layer = layer;
        cursor.getCursor().setKeys(GameKey.PREVIOUS, GameKey.NEXT);
        for (Terrain t : MyGame.getInstance().getLoader().getTerrains()) {
            if (t.getLayer().equals(layer)) {
                cursor.getList().add(t);
            }

        }
        cursor.getCursor().addListener(new Listener<Cursor1D>() {
            public void onChange(Cursor1D cursor) {
                updateSpriteCursor();
            }
        });
        position.addListener(new Listener<Coordinate>() {
            public void onChange(Coordinate source) {
                updateSpriteCursor();
            }
        });
    }

    private void updateSpriteCursor() {
        GameImage centralImage = getTerrainImage(cursor.getSelected());
        int centralX = position.getX() + getWidth() / 2;
        int centralY = position.getY() + getHeight() / 2;
        spriteCursor.getPosition().setXY(
                centralX - centralImage.getImage().getWidth() / 2,
                centralY - centralImage.getImage().getHeight() / 2);
        spriteCursor.getSize().setSize(
                centralImage.getImage().getWidth(),
                centralImage.getImage().getHeight());
    }

    public void update(int elapsedTime) {
        cursor.update(elapsedTime);
        spriteCursor.update(elapsedTime);
    }

    public void render(Graphics g) {
        int c = 0xDD;
        g.setColor(new Color(c, c, c));
        DrawerUtil.fill3dRect(g, this, true);

        int downTop = (position.getY() + getHeight() / 2)
                - getTerrainImage(cursor.getSelected()).getImage().getHeight() / 2;
        int upBottom = downTop - Layout.OBJECT_GAP;
        int index = cursor.getCursor().getCurrent();
        do {
            Terrain terrain = getCursorTerrain(index);
            int downBottom = downTop + getTerrainImage(terrain).getImage().getHeight() + Layout.OBJECT_GAP;
            if (downBottom >= Layout.getBottom(this)) {
                break;
            }
            renderTerrain(g, downTop, terrain);
            index++;
            downTop += getTerrainImage(terrain).getImage().getHeight() + Layout.OBJECT_GAP;
        } while (true);

        index = cursor.getCursor().getCurrent() - 1;
        do {
            Terrain terrain = getCursorTerrain(index);
            int upTop = upBottom - getTerrainImage(terrain).getImage().getHeight();
            if (upTop < this.getTop()) {
                break;
            }
            renderTerrain(g, upTop, terrain);
            index--;
            upBottom = upTop - Layout.OBJECT_GAP;
        } while (true);



        spriteCursor.render(g);
    }

    private Terrain getCursorTerrain(int position) {
        return cursor.getList().get(
                position >= 0
                ? position % cursor.getList().size()
                : (cursor.getList().size() - 1) + ((position + 1) % cursor.getList().size()));
    }

    private void renderTerrain(Graphics g, int top, Terrain terrain) {
        getTerrainImage(terrain).render(
                g,
                (position.getX() + getWidth() / 2) + getTerrainImage(terrain).getCenter().getX() - getTerrainImage(terrain).getImage().getWidth() / 2,
                top + getTerrainImage(terrain).getCenter().getY());
    }

    public Coordinate getPosition() {
        return position;
    }

    public ObjectCursor1D<Terrain> getCursor() {
        return cursor;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        int max = Map.TERRAIN_SIZE;
        for (Terrain terrain : cursor.getList()) {
            if (getTerrainImage(terrain).getImage().getWidth() > max) {
                max = getTerrainImage(terrain).getImage().getWidth();
            }
        }
        return max + Layout.OBJECT_GAP * 2 + DrawerUtil.BORDER_3D * 2;
    }

    public int getHeight() {
        return height;
    }
    
    public Terrain.Layer getLayer() {
        return layer;
    }

    private GameImage getTerrainImage(Terrain terrain) {
        return terrain.getSpriteAnimation().getImageByIndex(0);
    }

    
}
