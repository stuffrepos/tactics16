package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.menu.Menu;
import tactics16.components.menu.CommonMenuOption;
import tactics16.components.TextBox;
import tactics16.game.Map;
import tactics16.game.Terrain;
import tactics16.scenes.SelectMapScene;
import java.awt.Graphics2D;
import java.io.IOException;
import tactics16.GameKey;
import tactics16.components.PhaseTitle;
import tactics16.components.VisualMap;
import tactics16.phase.AbstractPhase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MenuMode extends AbstractPhase {

    private SelectMapScene selectMapScene;
    private Menu menu = new Menu(new CommonMenuOption[]{
                // <editor-fold defaultstate="collapsed" desc="menu options">
                new CommonMenuOption("New Map") {

            @Override
            public void executeAction() {
                final Map map = new Map(getNewMapName(), Map.MIN_SIZE, Map.MIN_SIZE);
                map.iterate(new Map.Iterator() {

                    public void check(int x, int y, Terrain terrain) {
                        map.setTerrain(x, y,
                                MyGame.getInstance().getLoader().getTerrains().getRequired("Grass"));
                    }
                });

                scene.setMap(map);

            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() == null;
            }
        }, new CommonMenuOption("Open Map") {

            @Override
            public void executeAction() {
                MyGame.getInstance().getPhaseManager().advance(selectMapScene);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() == null;
            }
        }, new CommonMenuOption("Rename Map") {

            @Override
            public void executeAction() {
                scene.toRenameMode();
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("Edit Map Terrain") {

            @Override
            public void executeAction() {
                scene.toTerrainEditMode();
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("Person's Positions") {

            @Override
            public void executeAction() {
                scene.toPersonsPositionMode();
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("Save Map") {

            @Override
            public void executeAction() {

                try {
                    MyGame.getInstance().getLoader().getMaps().save(scene.getMap());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("Close Map") {

            @Override
            public void executeAction() {
                scene.setMap(null);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("-W") {

            @Override
            public void executeAction() {
                scene.getMap().addWidth(-1);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("+W") {

            @Override
            public void executeAction() {
                scene.getMap().addWidth(1);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("-H") {

            @Override
            public void executeAction() {
                scene.getMap().addHeight(-1);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("+H") {

            @Override
            public void executeAction() {
                scene.getMap().addHeight(1);
            }

            @Override
            public boolean isEnabled() {
                return scene.getMap() != null;
            }
        }, new CommonMenuOption("Quit", GameKey.CANCEL) {

            @Override
            public void executeAction() {
                scene.quit();
            }
        }// </editor-fold>
            });
    private final MapBuilderScene scene;
    private VisualMap visualMap;
    private TextBox status = new TextBox();
    private PhaseTitle title;

    public MenuMode(MapBuilderScene scene) {
        this.scene = scene;
        title = scene.createModeTitle("Main Menu");
        menu.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));

        status.getPosition().setXY(
                menu.getLeft(),
                Layout.getBottom(menu) + Layout.OBJECT_GAP);
        status.setMinWidth(menu.getWidth());
        status.setMaxWidth(menu.getWidth());

        selectMapScene = new SelectMapScene(new Runnable() {

            public void run() {
                if (selectMapScene.getSelectedMap() != null) {
                    MenuMode.this.scene.setMap(selectMapScene.getSelectedMap());
                }
                MyGame.getInstance().getPhaseManager().back();
            }
        },false);
    }

    

    private String getNewMapName() {
        int i = 1;
        while (true) {
            String name = "new-" + i;

            if (MyGame.getInstance().getLoader().getMaps().get(name) == null) {
                return name;
            } else {
                i++;
            }
        }
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
    public void update(long elapsedTime) {
        menu.update(elapsedTime);
        updateStatusText();
    }

    @Override
    public void render(Graphics2D g) {
        menu.render(g);
        if (visualMap != null) {
            visualMap.render(g);
        }
        status.render(g);
        title.render(g);
    }

    @Override
    public void onEnter() {
        if (scene.getMap() == null) {
            visualMap = null;
        } else {
            visualMap = new VisualMap(scene.getMap());
            visualMap.getPosition().setXY(Layout.getRightGap(menu), menu.getTop());
        }
    }// </editor-fold>
}
