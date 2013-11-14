package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.util.cursors.Cursor2D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class TerrainEditMode extends Phase {

    private MapCursor mapCursor;
    private TerrainPallete terrainPallete;
    private final MapBuilderScene scene;

    public TerrainEditMode(MapBuilderScene scene, Terrain.Layer layer) {
        this.scene = scene;
        this.terrainPallete = new TerrainPallete(
                layer,
                Layout.getScreenHeight() - Layout.OBJECT_GAP * 2);
        this.terrainPallete.getPosition().setY(
                Layout.OBJECT_GAP);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        terrainPallete.update(delta);
        mapCursor.update(delta);

        if (MyGame.getInstance().isKeyPressed(GameKey.OPTIONS)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            scene.getMap().getLayer(terrainPallete.getLayer()).removeTerrain(
                    mapCursor.getCursor().getPosition());
        } else if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            scene.getMap().setTerrain(
                    mapCursor.getCursor().getPosition(),
                    terrainPallete.getCursor().getSelected());
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        terrainPallete.render(g);
        mapCursor.render(g);
    }

    public MapCursor getTerrainCursor() {
        return mapCursor;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        mapCursor = new MapCursor(scene.getVisualMap());
        mapCursor.getCursor().addListener(new Listener<Cursor2D>() {
            public void onChange(Cursor2D source) {
                if (mapCursor.getScreenPosition().getX() < Layout.getScreenWidth() / 2) {
                    terrainPallete.getPosition().setX(
                            Layout.getRightInnerGap(Layout.getScreenObject2D(), terrainPallete));
                } else {
                    terrainPallete.getPosition().setX(
                            Layout.OBJECT_GAP);
                }

            }
        });
    }
}
