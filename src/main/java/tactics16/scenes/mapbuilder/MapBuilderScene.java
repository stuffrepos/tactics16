package tactics16.scenes.mapbuilder;

import tactics16.game.Map;
import tactics16.phase.Phase;
import tactics16.util.listeners.Listener;
import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapBuilderScene implements Phase {

    private static final MapBuilderScene instance = new MapBuilderScene();
    private Mode currentMode;
    private final MenuMode menuMode = new MenuMode(this);
    private final TerrainEditMode terrainEditMode = new TerrainEditMode(this);
    private final PersonsPositionMode personsPositionMode = new PersonsPositionMode(this);
    private Map map;

    public static MapBuilderScene getInstance() {
        return instance;
    }

    public void toMenuMode() {
        this.currentMode = menuMode;
    }

    public void toPersonsPositionMode() {
        this.currentMode = personsPositionMode;
    }

    public void toTerrainEditMode() {
        this.currentMode = terrainEditMode;
    }

    public void onAdd() {
        toMenuMode();
    }

    public void onRemove() {
    }

    public void update(long elapsedTime) {
        currentMode.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        currentMode.render(g);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        Map oldMap = this.map;
        this.map = map;

        if (oldMap != this.map && this.map != null) {
            map.addListener(new Listener<Map>() {

                public void onChange(Map source) {
                    terrainEditMode.getTerrainCursor().getCursor().setDimension(
                            source.getWidth(), source.getHeight());
                    personsPositionMode.getTerrainCursor().getCursor().setDimension(
                            source.getWidth(), source.getHeight());

                }
            });
        }
    }

    public void onExit() {
    }

    public void onEnter() {
    }
}
