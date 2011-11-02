package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.GlowingRectangle;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Graphics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.VisualMap;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonsPositionMode extends Phase {

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

    public void update(GameContainer container, StateBasedGame game, int delta) {
        playerPallete.update(delta);
        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            scene.toMenuMode();
        } else if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            tooglePersonPosition();
        }

        updateStatusDialog();
        mapCursor.update(delta);
        for (Integer player : playerPanels.keySet()) {
            for (GlowingRectangle glowingRectangle : playerPanels.get(player)) {
                glowingRectangle.update(delta);
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

    public void render(GameContainer container, StateBasedGame game, Graphics g) {
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

    public void enter(GameContainer container, StateBasedGame game) {
        visualMap = new VisualMap(scene.getMap());
        visualMap.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(playerPallete));
        mapCursor = new MapCursor(visualMap);
    }
}
