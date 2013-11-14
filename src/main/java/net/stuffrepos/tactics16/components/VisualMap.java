package net.stuffrepos.tactics16.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.Terrain;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualMap extends AbstractObject2D {

    private long elapsedTime = 0l;
    private final Map map;

    public VisualMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public Coordinate getPersonPosition(Coordinate terrainPosition) {
        return new Coordinate(
                getLeft() + terrainPosition.getX() * Map.TERRAIN_SIZE + (Map.TERRAIN_SIZE / 2),
                getTop() + terrainPosition.getY() * Map.TERRAIN_SIZE + (Map.TERRAIN_SIZE / 2));
    }

    public Coordinate getTerrainPosition(Coordinate terrainPosition) {
        return new Coordinate(
                getLeft() + terrainPosition.getX() * Map.TERRAIN_SIZE,
                getTop() + terrainPosition.getY() * Map.TERRAIN_SIZE);
    }

    public void render(final Graphics g, boolean renderObstacles, int[] playerColors) {
        map.getLayer(Terrain.Layer.Base).iterate(new Map.Iterator() {
            public void check(int x, int y, Terrain terrain) {
                if (terrain != null) {
                    map.getTerrainImage(terrain, elapsedTime).render(
                            g,
                            getLeft() + x * Map.TERRAIN_SIZE,
                            getTop() + y * Map.TERRAIN_SIZE);
                } else {
                    g.setColor(Color.white);
                    g.drawRect(
                            getLeft() + x * Map.TERRAIN_SIZE,
                            getTop() + y * Map.TERRAIN_SIZE,
                            Map.TERRAIN_SIZE - 1,
                            Map.TERRAIN_SIZE - 1);
                }
            }
        });
        if (playerColors != null) {
            for (Coordinate personPosition : map.getPersonInitialPositions().getAllPositions()) {
                g.setColor(new Color(playerColors[map.getPersonInitialPositions().getPlayerFromPosition(personPosition)]));
                g.fillRect(
                        getLeft() + personPosition.getX() * Map.TERRAIN_SIZE,
                        getTop() + personPosition.getY() * Map.TERRAIN_SIZE,
                        Map.TERRAIN_SIZE,
                        Map.TERRAIN_SIZE);

            }
        }

        if (renderObstacles) {
            map.getLayer(Terrain.Layer.Obstacle).iterate(new Map.Iterator() {
                public void check(int x, int y, Terrain terrain) {
                    if (terrain != null) {
                        map.getTerrainImage(terrain, elapsedTime).render(
                                g,
                                getLeft() + x * Map.TERRAIN_SIZE,
                                getTop() + y * Map.TERRAIN_SIZE);
                    }
                }
            });
        }

    }

    @Override
    protected int calculateWidth() {
        return map.getWidth() * Map.TERRAIN_SIZE;
    }

    @Override
    protected int calculateHeight() {
        return map.getHeight() * Map.TERRAIN_SIZE;
    }
}
