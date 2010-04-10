package tactics16.scenes.mapbuilder;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.Menu;
import tactics16.components.MenuOption;
import tactics16.components.TextDialog;
import tactics16.game.Map;
import tactics16.game.Terrain;
import tactics16.scenes.SelectMapScene;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import tactics16.components.VisualMap;
import tactics16.phase.Phase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MenuMode implements Phase {

    private SelectMapScene selectMapScene;
    private Menu menu = new Menu(new MenuOption[]{
                // <editor-fold defaultstate="collapsed" desc="comment">
                new MenuOption("New Map") {

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
        },
                new MenuOption("Open Map") {

                    @Override
                    public void executeAction() {
                        MyGame.getInstance().getPhaseManager().advance(selectMapScene);
                    }
                },
                new MenuOption("Edit Map Terrain") {

                    @Override
                    public void executeAction() {
                        scene.toTerrainEditMode();
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("Person's Positions") {

                    @Override
                    public void executeAction() {
                        scene.toPersonsPositionMode();
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("Save Map") {

                    @Override
                    public void executeAction() {

                        try {
                            MyGame.getInstance().getLoader().saveMap(scene.getMap());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        MyGame.getInstance().getLoader().getMaps().addIgnore(scene.getMap());
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("Close Map") {

                    @Override
                    public void executeAction() {
                        scene.setMap(null);
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("-W") {

                    @Override
                    public void executeAction() {
                        scene.getMap().addWidth(-1);
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("+W") {

                    @Override
                    public void executeAction() {
                        scene.getMap().addWidth(1);
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("-H") {

                    @Override
                    public void executeAction() {
                        scene.getMap().addHeight(-1);
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("+H") {

                    @Override
                    public void executeAction() {
                        scene.getMap().addHeight(1);
                    }

                    @Override
                    public boolean isEnabled() {
                        return scene.getMap() != null;
                    }
                },
                new MenuOption("Quit", KeyEvent.VK_ESCAPE) {

                    @Override
                    public void executeAction() {
                        MyGame.getInstance().getPhaseManager().back();
                    }
                }// </editor-fold>
            });
    private final MapBuilderScene scene;
    private VisualMap visualMap;
    private TextDialog status = new TextDialog();

    public MenuMode(MapBuilderScene scene) {
        this.scene = scene;
        menu.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
        
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
        });
    }

    public void update(long elapsedTime) {
        menu.update(elapsedTime);
        updateStatusText();
    }

    public void render(Graphics2D g) {
        menu.render(g);
        if (visualMap != null) {
            visualMap.render(g);
        }
        status.render(g);
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
        }

        status.setText(builder.toString());
    }

    public void onAdd() {
    }

    public void onRemove() {
    }

    public void onExit() {
    }

    public void onEnter() {
        if (scene.getMap() == null) {
            visualMap = null;
        }
        else {
            visualMap = new VisualMap(scene.getMap());
            visualMap.getPosition().setXY(Layout.getRightGap(menu), Layout.OBJECT_GAP);
        }               
    }
}
