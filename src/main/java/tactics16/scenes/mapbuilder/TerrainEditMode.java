package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class TerrainEditMode implements Mode {

    private TerrainCursor terrainCursor;
    private TerrainPallete terrainPallete;
    private Coordinate mapPosition;
    private final MapBuilderScene scene;
    private TextDialog status;

    public TerrainEditMode(MapBuilderScene scene) {
        this.scene = scene;
        this.terrainPallete = new TerrainPallete();
        this.terrainPallete.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.OBJECT_GAP);

        this.mapPosition = new Coordinate(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(terrainPallete));

        this.terrainCursor = new TerrainCursor(this.mapPosition);
        this.status = new TextDialog();
        this.status.setWidth(200);
        this.status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - this.status.getWidth(),
                Layout.OBJECT_GAP);
    }

    public void update(long elapsedTime) {
        terrainPallete.update(elapsedTime);
        terrainCursor.update(elapsedTime);

        if (MyGame.getInstance().keyPressed(KeyEvent.VK_ESCAPE)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
            scene.getMap().setTerrain(
                    terrainCursor.getCursor().getPosition(),
                    terrainPallete.getCursor().getSelected());
        }

        updateStatusText();
    }

    public void render(Graphics2D g) {

        terrainPallete.render(g);
        scene.getMap().render(g, mapPosition);
        status.render(g);
        terrainCursor.render(g);
    }

    public Coordinate getMapPosition() {
        return this.mapPosition;
    }

    private void updateStatusText() {
        StringBuilder builder = new StringBuilder();

        if (scene.getMap() == null) {
            builder.append("-");
        } else {
            builder.append("Map size:");
            builder.append(String.format("\n %dx%d",
                    scene.getMap().getWidth(), scene.getMap().getHeight()));

            builder.append("\nCursor: " + terrainCursor.getCursor().toString());

        }

        status.setText(builder.toString());
    }

    public TerrainCursor getTerrainCursor() {
        return terrainCursor;
    }
}
