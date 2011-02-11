package net.stuffrepos.tactics16.scenes.battle;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.GlowingRectangle;
import net.stuffrepos.tactics16.components.VisualMap;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapCheckedArea implements VisualEntity {

    private List<GlowingRectangle> rectangles = new LinkedList<GlowingRectangle>();
    private Set<Coordinate> area = new TreeSet<Coordinate>();
    private List<Coordinate> terrainPositions;
    private boolean finalized = false;

    public MapCheckedArea(VisualMap visualMap, List<Coordinate> terrainPositions,int color) {
        this.terrainPositions = terrainPositions;
        for (Coordinate terrainPosition : terrainPositions) {
            GlowingRectangle rectangle = new GlowingRectangle(color,Map.TERRAIN_SIZE);
            rectangle.getPosition().set(visualMap.getTerrainPosition(terrainPosition));
            rectangles.add(rectangle);
            area.add(terrainPosition);
        }
    }

    public void update(long elapsedTime) {
        for (GlowingRectangle rectangle : rectangles) {
            rectangle.update(elapsedTime);
        }
    }

    public void render(Graphics2D g) {
        for (GlowingRectangle rectangle : rectangles) {
            rectangle.render(g);
        }
    }

    public boolean inArea(Coordinate c) {
        return area.contains(c);
    }

    public List<Coordinate> getTerrainPositions() {
        return terrainPositions;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void finalizeEntity() {
        finalized = true;
    }
}
