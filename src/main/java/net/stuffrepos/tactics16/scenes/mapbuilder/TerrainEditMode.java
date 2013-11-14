package net.stuffrepos.tactics16.scenes.mapbuilder;

import java.util.HashSet;
import java.util.Set;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
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
    private Terrain previousTerrain;

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

        if (MyGame.getInstance().keys().isPressed(GameKey.OPTIONS)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().keys().isPressed(GameKey.CANCEL)) {
            scene.getMap().getLayer(terrainPallete.getLayer()).removeTerrain(
                    mapCursor.getCursor().getPosition());
        } else if (MyGame.getInstance().keys().isHolded(GameKey.CONFIRM, 1000)) {
            fillMap(mapCursor.getCursor().getPosition(),
                    terrainPallete.getCursor().getSelected());
        } else if (MyGame.getInstance().keys().isPressed(GameKey.CONFIRM)) {
            this.previousTerrain = getMapLayer().getOccupied(mapCursor.getCursor().getPosition());
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

    private void fillMap(Coordinate origin, Terrain newTerrain) {
        Terrain originalTerrain = scene.getMap().getLayer(terrainPallete.getLayer()).getOccupied(origin);
        Set<Coordinate> visited = new HashSet<Coordinate>();
        visited.add(origin);
        Set<Coordinate> neighboors = fillMapNeighboors(
                origin,
                visited);
        neighboors.add(origin);

        int x0 = origin.getX() % newTerrain.getWidth();
        int y0 = origin.getY() % newTerrain.getHeight();

        for (int x = x0; x < scene.getMap().getWidth(); x += newTerrain.getWidth()) {
            for (int y = y0; y < scene.getMap().getHeight(); y += newTerrain.getHeight()) {
                if (fillMapCanPutTerrain(x, y, newTerrain, neighboors)) {
                    scene.getMap().setTerrain(x, y, newTerrain);
                }
            }
        }
    }

    private Set<Coordinate> fillMapNeighboors(
            Coordinate target,
            Set<Coordinate> visited) {

        Set<Coordinate> targetNeighboors = new HashSet<Coordinate>();
        if (target.getX() > 0) {
            targetNeighboors.add(new Coordinate(target.getX() - 1, target.getY()));
        }
        if (target.getY() > 0) {
            targetNeighboors.add(new Coordinate(target.getX(), target.getY() - 1));
        }
        if (target.getX() < scene.getMap().getWidth() - 1) {
            targetNeighboors.add(new Coordinate(target.getX() + 1, target.getY()));
        }
        if (target.getY() < scene.getMap().getHeight() - 1) {
            targetNeighboors.add(new Coordinate(target.getX(), target.getY() + 1));
        }

        Set<Coordinate> result = new HashSet<Coordinate>();
        Set<Coordinate> added = new HashSet<Coordinate>();
        for (Coordinate c : targetNeighboors) {
            if (!visited.contains(c)) {
                visited.add(c);
                if ((previousTerrain == null && getMapLayer().getOccupied(c) == null)
                        || (previousTerrain != null && previousTerrain.equals(getMapLayer().getOccupied(c)))) {
                    result.add(c);
                    added.add(c);
                }
            }
        }

        for (Coordinate c : added) {
            result.addAll(fillMapNeighboors(c, visited));
        }

        return result;
    }

    private boolean fillMapCanPutTerrain(int x, int y, Terrain terrain, Set<Coordinate> available) {
        for (int dX = 0; dX < terrain.getWidth(); dX++) {
            for (int dY = 0; dY < terrain.getHeight(); dY++) {
                if (!available.contains(new Coordinate(x + dX, y + dY))) {
                    return false;
                }
            }
        }

        return true;
    }

    private Map.Layer getMapLayer() {
        return scene.getMap().getLayer(terrainPallete.getLayer());
    }
}
