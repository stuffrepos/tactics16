package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.GlowingRectangle;
import tactics16.components.TextBox;
import tactics16.game.Coordinate;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import tactics16.GameKey;
import tactics16.components.MapCursor;
import tactics16.components.PhaseTitle;
import tactics16.components.VisualMap;
import tactics16.phase.AbstractPhase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonsPositionMode extends AbstractPhase {

    private PlayerPallete playerPallete = new PlayerPallete();
    private PhaseTitle title;
    private final MapBuilderScene scene;
    private VisualMap visualMap;
    private TextBox status;
    private MapCursor mapCursor;
    private Map<Integer, List<GlowingRectangle>> playerPanels = new TreeMap<Integer, List<GlowingRectangle>>();

    public PersonsPositionMode(MapBuilderScene scene) {
        this.scene = scene;
        this.title = scene.createModeTitle("Set Persons Positions");
        playerPallete.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));
        status = new TextBox();
        status.setWidth(200);
        status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - status.getWidth(),
                playerPallete.getPosition().getY());
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

        for (int player = 0; player < playerPallete.getCursor().getLength(); ++player) {
            b.append("Player: " + (player + 1));
            b.append("\n\tPositions: " + scene.getMap().getPlayerInitialPosition(player).size());
            b.append('\n');
        }

        status.setText(b.toString());
    }

    public void render(Graphics2D g) {
        title.render(g);
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

    public void onEnter() {
        visualMap = new VisualMap(scene.getMap());
        visualMap.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(playerPallete));
        mapCursor = new MapCursor(visualMap);
    }
}
