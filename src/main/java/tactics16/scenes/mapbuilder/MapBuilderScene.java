package tactics16.scenes.mapbuilder;

import tactics16.game.Map;
import tactics16.MyGame;
import tactics16.components.PhaseTitle;
import tactics16.phase.AbstractPhase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapBuilderScene extends AbstractPhase {

    private static final MapBuilderScene instance = new MapBuilderScene();
    private final MenuMode menuMode = new MenuMode(this);
    private final TerrainEditMode terrainEditMode = new TerrainEditMode(this);
    private final PersonsPositionMode personsPositionMode = new PersonsPositionMode(this);
    private final RenameMode renameMode = new RenameMode(this);
    private Map map;

    public static MapBuilderScene getInstance() {
        return instance;
    }

    private void resetPhase() {
        MyGame.getInstance().getPhaseManager().resetTo(this);
    }

    public void toMenuMode() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().advance(menuMode);
    }

    public void toPersonsPositionMode() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().advance(personsPositionMode);
    }

    public void toTerrainEditMode() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().advance(terrainEditMode);
    }

    public void toRenameMode() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().advance(renameMode);
    }

    @Override
    public void onAdd() {
        toMenuMode();
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
        this.menuMode.onEnter();
    }

    public void quit() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().back();
    }

    public PhaseTitle createModeTitle(String text) {
        return new PhaseTitle("Map Builder" + (text == null ? null : " - " + text));
    }
}
