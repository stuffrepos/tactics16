package tactics16.scenes.mapbuilder;

import tactics16.game.Map;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import tactics16.phase.PhaseManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapBuilderScene implements Phase {

    private static final MapBuilderScene instance = new MapBuilderScene();    
    private final MenuMode menuMode = new MenuMode(this);
    private final TerrainEditMode terrainEditMode = new TerrainEditMode(this);
    private final PersonsPositionMode personsPositionMode = new PersonsPositionMode(this);
    private PhaseManager phaseManager = new PhaseManager();
    private Map map;

    public static MapBuilderScene getInstance() {
        return instance;
    }

    public void toMenuMode() {
        phaseManager.change(menuMode);
    }

    public void toPersonsPositionMode() {
        phaseManager.change(personsPositionMode);
    }

    public void toTerrainEditMode() {
        phaseManager.change(terrainEditMode);
    }

    public void onAdd() {
        toMenuMode();
    }

    public void onRemove() {
    }

    public void update(long elapsedTime) {
        phaseManager.getCurrentPhase().update(elapsedTime);
    }

    public void render(Graphics2D g) {
        phaseManager.getCurrentPhase().render(g);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {        
        this.map = map;
        this.menuMode.onEnter();
    }

    public void onExit() {
    }

    public void onEnter() {
        toMenuMode();
    }
}
