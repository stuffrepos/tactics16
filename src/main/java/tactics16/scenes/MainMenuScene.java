package tactics16.scenes;

import tactics16.Layout;
import tactics16.scenes.mapbuilder.MapBuilderScene;
import tactics16.MyGame;
import tactics16.components.Menu;
import tactics16.components.MenuOption;
import tactics16.components.TextDialog;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MainMenuScene implements Phase {

    private static final MainMenuScene instance = new MainMenuScene();
    private final Menu mainMenu = new Menu(
            new MenuOption("Play") {

                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(selectMapScene);

                }
            },
            new MenuOption("Map Creator") {

                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(MapBuilderScene.getInstance());

                }
            },
            new MenuOption("Quit", KeyEvent.VK_ESCAPE) {

                @Override
                public void executeAction() {
                    MyGame.getInstance().quit();
                }
            });
    private SelectMapScene selectMapScene = new SelectMapScene(new Runnable() {

        public void run() {
            if (selectMapScene.getSelectedMap() == null) {
                MyGame.getInstance().getPhaseManager().back();
            } else {
                MyGame.getInstance().getPhaseManager().advance(new SelectPersonsScene(selectMapScene));
            }
        }
    });

    private MainMenuScene() {
    }

    public static final MainMenuScene getInstance() {
        return instance;
    }
    private TextDialog dialog;
    private boolean positioned = false;

    public void onAdd() {

        dialog = new TextDialog();
        dialog.setText("Geo Tactics");
    }

    public void onRemove() {
    }

    public void update(long elapsedTime) {
        mainMenu.update(elapsedTime);
    }

    public void render(Graphics2D g) {

        if (!positioned) {
            final int GAP = 10;

            dialog.getPosition().setX(
                    Layout.getCentralizedLeft(dialog));

            dialog.getPosition().setY(
                    (Layout.getScreenHeight() -
                    (dialog.getHeight() + mainMenu.getOptionHeight() + Layout.OBJECT_GAP)) / 2);

            mainMenu.getPosition().setX(
                    Layout.getCentralizedLeft(mainMenu));

            mainMenu.getPosition().setY(
                    Layout.getBottom(dialog) + Layout.OBJECT_GAP);

            positioned = false;
        }

        mainMenu.render(g);
        dialog.render(g);
    }

    public void onExit() {
    }

    public void onEnter() {
    }
}