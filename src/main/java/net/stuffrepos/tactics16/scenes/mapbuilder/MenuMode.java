package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battleconfig.SelectMapScene;
import org.newdawn.slick.Graphics;
import java.io.IOException;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MenuMode extends Phase {

    private SelectMapScene selectMapScene;
    private Menu menu = new Menu(new CommonMenuOption[]{});
    private final MapBuilderScene scene;
    private TextBox status = new TextBox();

    public MenuMode(final MapBuilderScene scene) {
        this.scene = scene;
        menu.addOptions(new CommonMenuOption[]{
            new CommonMenuOption("New Map") {
                @Override
                public void executeAction() {
                    scene.setMap(null);
                }
            }, new CommonMenuOption("Open Map") {
                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(selectMapScene);
                }
            }, new CommonMenuOption("Rename Map") {
                @Override
                public void executeAction() {
                    scene.toRenameMode();
                }
            }, new CommonMenuOption("Save Map") {
                @Override
                public void executeAction() {
                    scene.saveMap();
                }
            }, new CommonMenuOption("Width-") {
                @Override
                public void executeAction() {
                    scene.getMap().addWidth(-1);
                }

                @Override
                public boolean isEnabled() {
                    return scene.getMap().getWidth() > Map.MIN_SIZE;
                }
            }, new CommonMenuOption("Width+") {
                @Override
                public void executeAction() {
                    scene.getMap().addWidth(1);
                }

                @Override
                public boolean isEnabled() {
                    return scene.getMap().getWidth() < Map.MAX_SIZE;
                }
            }, new CommonMenuOption("Height-") {
                @Override
                public void executeAction() {
                    scene.getMap().addHeight(-1);
                }

                @Override
                public boolean isEnabled() {
                    return scene.getMap().getHeight() > Map.MIN_SIZE;
                }
            }, new CommonMenuOption("Height+") {
                @Override
                public void executeAction() {
                    scene.getMap().addHeight(1);
                }

                @Override
                public boolean isEnabled() {
                    return scene.getMap().getHeight() < Map.MAX_SIZE;
                }
            }
        });
        for (final Terrain.Layer layer : Terrain.Layer.values()) {
            menu.addOption(new CommonMenuOption("Edit Map Terrain (" + layer.name() + ")") {
                @Override
                public void executeAction() {
                    scene.toTerrainEditMode(layer);
                }
            });
        }

        menu.addOptions(new CommonMenuOption[]{
            new CommonMenuOption("Person's Positions") {
                @Override
                public void executeAction() {
                    scene.toPersonsPositionMode();
                }
            }, new CommonMenuOption("Quit", GameKey.CANCEL) {
                @Override
                public void executeAction() {
                    scene.quit();
                }
            }
        });
        menu.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
        status.setMinWidth(menu.getWidth());
        status.setMaxWidth(menu.getWidth());
        status.getPosition().setXY(
                Layout.getRightInnerGap(Layout.getScreenObject2D(), status),
                Layout.OBJECT_GAP);


        selectMapScene = new SelectMapScene(new Runnable() {
            public void run() {
                if (selectMapScene.getSelectedMap() != null) {
                    MenuMode.this.scene.setMap(selectMapScene.getSelectedMap());
                }
                MyGame.getInstance().getPhaseManager().back();
            }
        },false);
    }

    private void updateStatusText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Map name: ");

        if (scene.getMap() == null) {
            builder.append("-");
        } else {
            builder.append("\n " + scene.getMap().getName());
            builder.append("\nMap size:");
            builder.append(String.format("\n %dx%d",
                    scene.getMap().getWidth(), scene.getMap().getHeight()));
            builder.append("\nPlayable: " + (scene.getMap().isPlayable() ? "Yes" : "No"));
        }

        status.setText(builder.toString());
    }

    // <editor-fold defaultstate="collapsed" desc="Phase implementation">
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        menu.update(delta);
        updateStatusText();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        menu.render(g);
        status.render(g);
    }
}
