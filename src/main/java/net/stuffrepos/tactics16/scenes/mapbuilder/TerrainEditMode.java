package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.TextBox;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.VisualMap;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.util.javabasic.StringUtil;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class TerrainEditMode extends Phase {

    private PhaseTitle title;
    private MapCursor mapCursor;
    private TerrainPallete terrainPallete;
    private VisualMap visualMap;
    private final MapBuilderScene scene;
    private TextBox status;

    public TerrainEditMode(MapBuilderScene scene) {
        this.scene = scene;
        this.title = scene.createModeTitle("Terrain Editor");

        this.terrainPallete = new TerrainPallete();
        this.terrainPallete.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(title));


        this.status = new TextBox();
        this.status.setWidth(200);
        this.status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - this.status.getWidth(),
                this.terrainPallete.getTop());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        terrainPallete.update(delta);
        mapCursor.update(delta);

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            scene.getMap().setTerrain(
                    mapCursor.getCursor().getPosition(),
                    terrainPallete.getCursor().getSelected());
        }

        updateStatusText();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        terrainPallete.render(g);
        visualMap.render(g, true, null);
        status.render(g);
        mapCursor.render(g);
        title.render(g);
    }

    private void updateStatusText() {
        StringBuilder builder = new StringBuilder();

        if (scene.getMap() == null) {
            builder.append("-");
        } else {
            builder.append("Map size:");
            builder.append(String.format("\n\t%dx%d",
                    scene.getMap().getWidth(), scene.getMap().getHeight()));

            Terrain terrain = terrainPallete.getCursor().getSelected();

            if (terrain != null) {
                builder.append("\nTerrain:");
                builder.append("\n\tName: ").append(terrain.getName());
                builder.append("\n\tBlock: ").append(StringUtil.yesNo(terrain.getBlock()));                
            }

            builder.append("\nCursor: ");
            builder.append("\n\t").append(mapCursor.getCursor().toString());

        }

        status.setText(builder.toString());
    }

    public MapCursor getTerrainCursor() {
        return mapCursor;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        visualMap = new VisualMap(scene.getMap());
        visualMap.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(terrainPallete));
        mapCursor = new MapCursor(visualMap);
    }
}
