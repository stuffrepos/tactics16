package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.components.MapCursor;
import tactics16.components.PhaseTitle;
import tactics16.components.VisualMap;
import tactics16.game.Terrain;
import tactics16.phase.AbstractPhase;
import tactics16.util.javabasic.StringUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class TerrainEditMode extends AbstractPhase {

    private PhaseTitle title;
    private MapCursor mapCursor;
    private TerrainPallete terrainPallete;
    private VisualMap visualMap;
    private final MapBuilderScene scene;
    private TextDialog status;

    public TerrainEditMode(MapBuilderScene scene) {
        this.scene = scene;
        this.title = scene.createModeTitle("Terrain Editor");

        this.terrainPallete = new TerrainPallete();
        this.terrainPallete.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(title));


        this.status = new TextDialog();
        this.status.setWidth(200);
        this.status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - this.status.getWidth(),
                this.terrainPallete.getTop());
    }

    @Override
    public void update(long elapsedTime) {
        terrainPallete.update(elapsedTime);
        mapCursor.update(elapsedTime);

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
    public void render(Graphics2D g) {
        terrainPallete.render(g);
        visualMap.render(g);
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
                builder.append("\n\tName: " + terrain.getName());
                builder.append("\n\tAllow Moviment: " + StringUtil.yesNo(terrain.getAllowMoviment()));
                builder.append("\n\tAllow Action: " + StringUtil.yesNo(terrain.getAllowMoviment()));
            }

            builder.append("\nCursor: ");
            builder.append("\n\t" + mapCursor.getCursor().toString());

        }

        status.setText(builder.toString());
    }

    public MapCursor getTerrainCursor() {
        return mapCursor;
    }

    @Override
    public void onEnter() {
        visualMap = new VisualMap(scene.getMap());
        visualMap.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(terrainPallete));
        mapCursor = new MapCursor(visualMap);
    }
}
