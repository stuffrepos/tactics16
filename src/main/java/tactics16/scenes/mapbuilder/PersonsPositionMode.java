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
import tactics16.GameKey;
import tactics16.components.MapCursor;
import tactics16.components.VisualMap;
import tactics16.phase.Phase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonsPositionMode implements Phase {

    private PlayerPallete playerPallete = new PlayerPallete();
    private final MapBuilderScene scene;
    private VisualMap visualMap;
    private TextDialog status;
    private MapCursor mapCursor;
    private Map<Integer, List<GlowingRectangle>> playerPanels = new TreeMap<Integer, List<GlowingRectangle>>();

    public PersonsPositionMode(MapBuilderScene scene) {
        this.scene = scene;
        playerPallete.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
        status = new TextDialog();
        status.setWidth(200);
        status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - status.getWidth(),
                Layout.OBJECT_GAP);        
    }

    public void update(long elapsedTime) {
        playerPallete.update(elapsedTime);
        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            tooglePersonPosition();
        }

        updateStatusDialog();
        mapCursor.update(elapsedTime);
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
        visualMap.render(g, this.playerPallete.getPlayerColors());
        status.render(g);
        mapCursor.render(g);

        for (Integer player : playerPanels.keySet()) {
            for (GlowingRectangle glowingRectangle : playerPanels.get(player)) {
                glowingRectangle.render(g);
            }
        }
    }

    public MapCursor getMapCursor() {
        return mapCursor;
    }

    private void tooglePersonPosition() {
        Integer player = scene.getMap().getPlayerFromPosition(mapCursor.getCursor().getPosition());

        if (player != null && player == this.playerPallete.getCursor().getCurrent()) {
            scene.getMap().setPersonInitialPosition(null, mapCursor.getCursor().getPosition());
        } else {
            scene.getMap().setPersonInitialPosition(
                    playerPallete.getCursor().getCurrent(),
                    mapCursor.getCursor().getPosition());
        }
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
                Layout.getBottomGap(playerPallete));
        mapCursor = new MapCursor(visualMap);
    }
}
