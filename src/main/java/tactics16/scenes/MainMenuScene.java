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

    private static final MainMenuScene instance = new MainMenuScene();
    private boolean positioned = false;
    private Text title;
    private TextDialog keysHelp;
    private ObjectCursor1D<Font> fontsCursor;
    private TextDialog fontName;
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
    },true);

    private MainMenuScene() {
    }

    public static final MainMenuScene getInstance() {
        return instance;
    }

    @Override
    public void onAdd() {

        title = new Text();
        title.setFont(Text.MAIN_TITLE_FONT);
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
        fontName = new TextDialog();
        fontName.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);

        final String initialFontName = MyGame.getInstance().getFont().getName();

        List<Font> fonts = new ArrayList<Font>();

        for (String fontName :
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            fonts.add(new Font(fontName, Font.PLAIN, 12));
        }

        fontsCursor = new ObjectCursor1D<Font>(fonts);
        fontsCursor.getCursor().setKeys(GameKey.PREVIOUS, GameKey.NEXT);
        fontsCursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {

                MyGame.getInstance().setFont(fontsCursor.getSelected());

                fontName.setText(fontsCursor.getSelected().getName());
            }
        });

        while (true) {
            if (initialFontName.equals(fontsCursor.getSelected().getName())) {
                break;
            }
            if (fontsCursor.getCursor().getCurrent() == fontsCursor.getCursor().getLength()) {
                break;
            }
            fontsCursor.getCursor().moveNext();
        }
    }

    @Override
    public void update(long elapsedTime) {
        fontsCursor.update(elapsedTime);
        mainMenu.update(elapsedTime);
    }

    @Override
    public void render(Graphics2D g) {
        mainMenu.render(g);
        fontName.render(g);
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
