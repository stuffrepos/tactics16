package tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.game.Coordinate;
import tactics16.game.Map;
import tactics16.game.Terrain;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualMap {

    private Coordinate position = new Coordinate();
    private final Map map;

    public VisualMap(Map map) {
        this.map = map;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Coordinate getPersonPosition(Coordinate terrainPosition) {
        return new Coordinate(
                position.getX() + terrainPosition.getX() * Map.TERRAIN_SIZE + (Map.TERRAIN_SIZE / 2),
                position.getY() + terrainPosition.getY() * Map.TERRAIN_SIZE + (Map.TERRAIN_SIZE / 2));
    }

    public void render(final Graphics2D g) {
        map.iterate(new Map.Iterator() {

            public void check(int x, int y, Terrain terrain) {
                if (terrain != null) {
                    g.drawImage(
                            map.getTerrainImage(terrain),
                            position.getX() + x * Map.TERRAIN_SIZE,
                            position.getY() + y * Map.TERRAIN_SIZE, null);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRect(
                            position.getX() + x * Map.TERRAIN_SIZE,
                            position.getY() + y * Map.TERRAIN_SIZE,
                            Map.TERRAIN_SIZE - 1,
                            Map.TERRAIN_SIZE - 1);
                }
            }
        });
    }

    public Map getMap() {
        return map;
    }

    public void render(final Graphics2D g, int[] playerColors) {
        map.iterate(new Map.Iterator() {

            public void check(int x, int y, Terrain terrain) {
                if (terrain != null) {
                    g.drawImage(
                            map.getTerrainImage(terrain),
                            position.getX() + x * Map.TERRAIN_SIZE,
                            position.getY() + y * Map.TERRAIN_SIZE, null);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRect(
                            position.getX() + x * Map.TERRAIN_SIZE,
                            position.getY() + y * Map.TERRAIN_SIZE,
                            Map.TERRAIN_SIZE - 1,
                            Map.TERRAIN_SIZE - 1);
                }
            }
        });
        if (playerColors != null) {
            for (Coordinate personPosition : map.getPersonInitialPositions().getAllPositions()) {
                g.setColor(new Color(playerColors[map.getPersonInitialPositions().getPlayerFromPosition(personPosition)]));
                g.fillRect(
                        this.position.getX() + personPosition.getX() * Map.TERRAIN_SIZE,
                        this.position.getY() + personPosition.getY() * Map.TERRAIN_SIZE,
                        Map.TERRAIN_SIZE,
                        Map.TERRAIN_SIZE);

            }
        }

    }
}
