package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.GlowingRectangle;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonsPositionMode implements Mode {

    private PlayerPallete playerPallete = new PlayerPallete();
    private final MapBuilderScene scene;
    private Coordinate mapPosition;
    private TextDialog status;
    private TerrainCursor terrainCursor;
    private Map<Integer, List<GlowingRectangle>> playerPanels = new TreeMap<Integer, List<GlowingRectangle>>();

    public PersonsPositionMode(MapBuilderScene scene) {

        this.scene = scene;
        playerPallete.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
        mapPosition = new Coordinate(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(playerPallete));

        status = new TextDialog();
        status.setWidth(200);
        status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - status.getWidth(),
                Layout.OBJECT_GAP);

        terrainCursor = new TerrainCursor(mapPosition);
    }

    public void update(long elapsedTime) {
        playerPallete.update(elapsedTime);
        if (MyGame.getInstance().keyPressed(KeyEvent.VK_ESCAPE)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
            tooglePersonPosition();
        }

        updateStatusDialog();
        terrainCursor.update(elapsedTime);
        for (Integer player : playerPanels.keySet()) {
            for (GlowingRectangle glowingRectangle : playerPanels.get(player)) {
                glowingRectangle.update(elapsedTime);
            }
        }
    }

    private void updateStatusDialog() {
        StringBuilder b = new StringBuilder();
        b.append("Player: " + (playerPallete.getCursor().getCurrent() + 1));
        b.append('\n');

        for (Coordinate personPosition :
                scene.getMap().getPlayerInitialPosition(playerPallete.getCursor().getCurrent())) {
            b.append(personPosition.toString());
            b.append('\n');
        }

        status.setText(b.toString());
    }

    public void render(Graphics2D g) {
        playerPallete.render(g);
        scene.getMap().render(g, mapPosition, this.playerPallete.getPlayerColors());
        status.render(g);
        terrainCursor.render(g);

        for (Integer player : playerPanels.keySet()) {
            for (GlowingRectangle glowingRectangle : playerPanels.get(player)) {
                glowingRectangle.render(g);
            }
        }
    }

    public TerrainCursor getTerrainCursor() {
        return terrainCursor;
    }

    private void tooglePersonPosition() {
        Integer player = scene.getMap().getPlayerFromPosition(terrainCursor.getCursor().getPosition());

        if (player != null && player == this.playerPallete.getCursor().getCurrent()) {
            scene.getMap().setPersonInitialPosition(null, terrainCursor.getCursor().getPosition());
        } else {
            scene.getMap().setPersonInitialPosition(
                    playerPallete.getCursor().getCurrent(),
                    terrainCursor.getCursor().getPosition());
        }
    }
}
