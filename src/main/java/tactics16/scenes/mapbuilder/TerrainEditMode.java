package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import tactics16.components.MapCursor;
import tactics16.components.VisualMap;
import tactics16.phase.Phase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class TerrainEditMode implements Phase {

    private MapCursor mapCursor;
    private TerrainPallete terrainPallete;
    private VisualMap visualMap;
    private final MapBuilderScene scene;
    private TextDialog status;

    public TerrainEditMode(MapBuilderScene scene) {
        this.scene = scene;
        this.terrainPallete = new TerrainPallete();
        this.terrainPallete.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.OBJECT_GAP);

        
        this.status = new TextDialog();
        this.status.setWidth(200);
        this.status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - this.status.getWidth(),
                Layout.OBJECT_GAP);
    }

    public void update(long elapsedTime) {
        terrainPallete.update(elapsedTime);
        mapCursor.update(elapsedTime);

        if (MyGame.getInstance().keyPressed(KeyEvent.VK_ESCAPE)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
            scene.getMap().setTerrain(
                    mapCursor.getCursor().getPosition(),
                    terrainPallete.getCursor().getSelected());
        }

        updateStatusText();
    }

    public void render(Graphics2D g) {
        terrainPallete.render(g);
        visualMap.render(g);
        status.render(g);
        mapCursor.render(g);
    }    

    private void updateStatusText() {
        StringBuilder builder = new StringBuilder();

        if (scene.getMap() == null) {
            builder.append("-");
        } else {
            builder.append("Map size:");
            builder.append(String.format("\n %dx%d",
                    scene.getMap().getWidth(), scene.getMap().getHeight()));

            builder.append("\nCursor: " + mapCursor.getCursor().toString());

        }

        status.setText(builder.toString());
    }

    public MapCursor getTerrainCursor() {
        return mapCursor;
    }

    public void onAdd() {
    }

    public void onRemove() {
    }

    public void onExit() {        
    }

    public void onEnter() {
        visualMap = new VisualMap(scene.getMap());
        visualMap.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(terrainPallete));        
        mapCursor = new MapCursor(visualMap);
    }
}
