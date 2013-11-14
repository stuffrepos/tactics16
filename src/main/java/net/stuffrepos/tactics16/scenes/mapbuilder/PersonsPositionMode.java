package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.GlowingRectangle;
import net.stuffrepos.tactics16.components.TextBox;
import org.newdawn.slick.Graphics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonsPositionMode extends Phase {

    private PlayerPallete playerPallete = new PlayerPallete();
    private final MapBuilderScene scene;
    private TextBox status;
    private MapCursor mapCursor;
    private Map<Integer, List<GlowingRectangle>> playerPanels = new TreeMap<Integer, List<GlowingRectangle>>();

    public PersonsPositionMode(MapBuilderScene scene) {
        this.scene = scene;
        status = new TextBox();
        status.setWidth(200);
        status.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - status.getWidth(),
                playerPallete.getPosition().getY());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        playerPallete.update(delta);
        if (MyGame.getInstance().isKeyPressed(GameKey.OPTIONS)) {
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

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        playerPallete.render(g);
        status.render(g);
        mapCursor.render(g);

        for (Integer player : playerPanels.keySet()) {
            for (GlowingRectangle glowingRectangle : playerPanels.get(player)) {
                glowingRectangle.render(g);
            }
        }
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

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        mapCursor = new MapCursor(scene.getVisualMap());
        mapCursor.addScreenQuadrantListener(new MapCursor.ScreenQuadrantListener() {
            public void onChange(boolean cursorOnLeft, boolean cursorOnTop) {
                if (cursorOnLeft) {
                    playerPallete.getPosition().setXY(
                            Layout.getRightInnerGap(Layout.getScreenObject2D(), playerPallete),
                            Layout.OBJECT_GAP);
                    status.getPosition().setXY(
                            Layout.getRightInnerGap(Layout.getScreenObject2D(), status),
                            Layout.getBottomGap(playerPallete));
                } else {
                    playerPallete.getPosition().setXY(
                            Layout.OBJECT_GAP,
                            Layout.OBJECT_GAP);
                    status.getPosition().setXY(
                            Layout.OBJECT_GAP,
                            Layout.getBottomGap(playerPallete));
                }
            }
        });
        scene.setMapPlayersColors(this.playerPallete.getPlayerColors());
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game); //To change body of generated methods, choose Tools | Templates.
        scene.setMapPlayersColors(null);
    }
}
