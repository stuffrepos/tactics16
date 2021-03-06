package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.cursors.Cursor2D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapCursor implements VisualEntity {

    private final VisualMap visualMap;
    private Cursor2D cursor;
    private GlowingRectangle visualCursor;
    private boolean finalized = false;

    public MapCursor(VisualMap visualMap) {
        this.visualMap = visualMap;
        this.visualCursor = new GlowingRectangle(Map.TERRAIN_SIZE);
        this.cursor = new Cursor2D();
        this.cursor.setDimension(this.visualMap.getMap().getWidth(), this.visualMap.getMap().getHeight());
        this.cursor.setKeys(
                GameKey.LEFT, GameKey.RIGHT,
                GameKey.UP, GameKey.DOWN);
        this.cursor.addListener(new Listener<Cursor2D>() {

            public void onChange(Cursor2D source) {
                MapCursor.this.visualCursor.getPosition().set(
                        MapCursor.this.visualMap.getTerrainPosition(source.getPosition()));
            }
        });
    }

    public void addScreenQuadrantListener(final ScreenQuadrantListener listener) {
        visualCursor.getPosition().addListener(new Listener<Coordinate>() {
            private Boolean oldOnLeft;
            private Boolean oldOnTop; 

            public void onChange(Coordinate source) {
                boolean onLeft = source.getX() < Layout.getScreenWidth() / 2;
                boolean onTop = source.getY() < Layout.getScreenHeight() / 2;
                if (oldOnLeft == null
                        || oldOnLeft != onLeft
                        || oldOnTop == null
                        || oldOnTop != onTop) {

                    oldOnLeft = onLeft;
                    oldOnTop = onTop;
                    listener.onChange(onLeft, onTop);
                }
            }
        });
    }

    public void update(int delta) {
        cursor.update(delta);
        visualCursor.update(delta);
    }

    public void render(Graphics g) {
        visualCursor.render(g);
    }

    public Cursor2D getCursor() {
        return cursor;
    }

    public void finalizeEntity() {
        finalized = true;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public Coordinate getScreenPosition() {
        return visualCursor.getPosition();
    }

    public static interface ScreenQuadrantListener {

        public void onChange(boolean cursorOnLeft, boolean cursorOnTop);
    }
}
