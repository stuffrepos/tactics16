package tactics16.scenes;

import java.awt.Font;
import tactics16.Layout;
import tactics16.scenes.mapbuilder.MapBuilderScene;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import tactics16.GameKey;
import tactics16.components.PhaseTitle;
import tactics16.components.Text;
import tactics16.components.menu.CommonMenuOption;
import tactics16.components.menu.Menu;
import tactics16.phase.AbstractPhase;
import tactics16.util.cursors.Cursor1D;
import tactics16.util.cursors.ObjectCursor1D;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MainMenuScene extends AbstractPhase {

    public static final Font MAIN_TITLE_FONT = new Font("purisa", Font.BOLD, 48);
    private static final MainMenuScene instance = new MainMenuScene();
    private boolean positioned = false;
    private Text title;
    private TextDialog keysHelp;
    
    private final Menu mainMenu = new Menu(
            new CommonMenuOption("Play") {

                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(selectMapScene);

                }
            },
            new CommonMenuOption("Map Creator") {

                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(MapBuilderScene.getInstance());

                }
            },
            new CommonMenuOption("Text Font Tester") {

                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(FontTesterScene.getInstance());

                }
            },
            new CommonMenuOption("Quit", GameKey.CANCEL) {

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
    }, true);

    private MainMenuScene() {
    }

    public static final MainMenuScene getInstance() {
        return instance;
    }

    @Override
    public void onAdd() {

        title = new Text();
        title.setFont(MAIN_TITLE_FONT);
        title.setColor(PhaseTitle.DEFAULT_COLOR);
        title.setText("Tactics16");

        keysHelp = new TextDialog();
        StringBuilder b = new StringBuilder();
        b.append("Keys Help\n");
        for (GameKey gameKey : GameKey.values()) {
            b.append(gameKey.toString());
            b.append(": ");
            boolean first = true;
            for (int key : MyGame.getInstance().getKeyMapping().getKeys(gameKey)) {
                if (first) {
                    first = false;
                } else {
                    b.append(", ");
                }
                b.append(KeyEvent.getKeyText(key));
            }
            b.append('\n');
        }
        keysHelp.setText(b.toString());
    }

    @Override
    public void update(long elapsedTime) {
        mainMenu.update(elapsedTime);
    }

    @Override
    public void render(Graphics2D g) {
        mainMenu.render(g);
        
        title.render(g);
        keysHelp.render(g);

        if (!positioned) {
            title.getPosition().setX(
                    Layout.getCentralizedLeft(title));

            mainMenu.getPosition().setX(
                    Layout.getCentralizedLeft(mainMenu));

            keysHelp.getPosition().setX(
                    Layout.getCentralizedLeft(keysHelp));

            title.getPosition().setY(
                    (Layout.getScreenHeight() -
                    (title.getHeight() + mainMenu.getHeight() +
                    keysHelp.getHeight() + Layout.OBJECT_GAP * 2)) / 2);

            mainMenu.getPosition().setY(
                    Layout.getBottom(title) + Layout.OBJECT_GAP);

            keysHelp.getPosition().setY(
                    Layout.getBottom(mainMenu) + Layout.OBJECT_GAP);

            positioned = true;
        }
    }
}
