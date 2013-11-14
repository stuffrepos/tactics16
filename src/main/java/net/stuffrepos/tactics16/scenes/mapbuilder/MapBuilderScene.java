package net.stuffrepos.tactics16.scenes.mapbuilder;

import java.io.IOException;
import java.util.EnumMap;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.VisualMap;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapBuilderScene extends Phase {

    private static final MapBuilderScene instance = new MapBuilderScene();
    private final MenuMode menuMode = new MenuMode(this);
    private final EnumMap<Terrain.Layer, TerrainEditMode> terrainEditModes =
            new EnumMap<Terrain.Layer, TerrainEditMode>(Terrain.Layer.class);
    private final PersonsPositionMode personsPositionMode = new PersonsPositionMode(this);
    private final RenameMode renameMode = new RenameMode(this);
    private VisualMap visualMap;
    private PhaseManager subPhaseManager = new PhaseManager();
    private int[] playersColors = null;

    public MapBuilderScene() {
        for (Terrain.Layer layer : Terrain.Layer.values()) {
            terrainEditModes.put(layer, new TerrainEditMode(this, layer));
        }
    }

    public static MapBuilderScene getInstance() {
        return instance;
    }

    private void resetPhase() {
        MyGame.getInstance().getPhaseManager().resetTo(this);
    }

    public void toMenuMode() {
        resetPhase();
        subPhaseManager.advance(menuMode);
    }

    public void toPersonsPositionMode() {
        resetPhase();
        subPhaseManager.advance(personsPositionMode);
    }

    public void toTerrainEditMode(Terrain.Layer layer) {
        resetPhase();
        subPhaseManager.advance(terrainEditModes.get(layer));
    }

    public void toRenameMode() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().advance(renameMode);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        toTerrainEditMode(Terrain.Layer.values()[0]);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        super.init(container, game);
        setMap(null);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        visualMap.update(delta);
        subPhaseManager.getCurrentPhase().update(container, game, delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
        visualMap.render(g, true, playersColors);
        subPhaseManager.getCurrentPhase().render(container, game, g);
    }

    public VisualMap getVisualMap() {
        return visualMap;
    }

    public Map getMap() {
        return visualMap.getMap();
    }

    public void setMap(Map map) {
        this.visualMap = new VisualMap(
                map == null
                ? new Map(getNewMapName(), Map.MIN_SIZE, Map.MIN_SIZE)
                : map.clone());
    }

    public void quit() {
        resetPhase();
        MyGame.getInstance().getPhaseManager().back();
    }

    public PhaseTitle createModeTitle(String text) {
        return new PhaseTitle("Map Builder" + (text == null ? null : " - " + text));
    }

    public void setMapPlayersColors(int[] playersColors) {
        this.playersColors = playersColors;
    }

    private String getNewMapName() {
        int i = 1;
        while (true) {
            String name = "new-" + i;

            if (MyGame.getInstance().getLoader().getMaps().get(name) == null) {
                return name;
            } else {
                i++;
            }
        }
    }

    public void saveMap() {
        try {
            MyGame.getInstance().getLoader().getMaps().save(getMap());
            new MessageBox("Map Saved", MyGame.getInstance().getScreenObject2D()).createPhase();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
