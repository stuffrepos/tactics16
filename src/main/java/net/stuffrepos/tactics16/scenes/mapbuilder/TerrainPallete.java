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

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TerrainPallete implements Object2D {

    private ObjectCursor1D<Terrain> cursor = new ObjectCursor1D<Terrain>();
    private BorderRectangle spriteCursor = new BorderRectangle(Map.TERRAIN_SIZE);
    private Coordinate position = new Coordinate();

    public TerrainPallete() {
        cursor.getCursor().setKeys(GameKey.PREVIOUS,GameKey.NEXT);
        for (Terrain t : MyGame.getInstance().getLoader().getTerrains()) {
            cursor.getList().add(t);
        }
        cursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D cursor) {
                updateSpriteCursorPosition();
            }
        });
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                updateSpriteCursorPosition();
            }
        });
    }

    private void updateSpriteCursorPosition() {
        spriteCursor.getPosition().setXY(
                position.getX() + cursor.getCursor().getCurrent() * Map.TERRAIN_SIZE,
                position.getY());

    }

    public void update(int elapsedTime) {
        cursor.update(elapsedTime);
        spriteCursor.update(elapsedTime);
    }

    public void render(Graphics g) {
        for (int i = 0; i < cursor.getList().size(); ++i) {
            cursor.getList().get(i).render(
                    g,
                    position.getX() + i * Map.TERRAIN_SIZE,
                    position.getY());
        }

        spriteCursor.render(g);
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
        return Map.TERRAIN_SIZE * cursor.getCursor().getLength();
    }

    public int getHeight() {
        return Map.TERRAIN_SIZE;
    }
}
