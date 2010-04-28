package tactics16.scenes;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.animation.EntitiesBoard;
import tactics16.animation.VisualEntity;
import tactics16.components.MessageBox;
import tactics16.components.MessageBox;
import tactics16.components.PhaseTitle;
import tactics16.components.Text;
import tactics16.components.TextBox;
import tactics16.phase.AbstractPhase;
import tactics16.util.cursors.Cursor1D;
import tactics16.util.cursors.ObjectCursor1D;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class FontTesterScene extends AbstractPhase {

    private static final FontTesterScene instance = new FontTesterScene();
    private final EntitiesBoard entitiesBoard = new EntitiesBoard();
    private ObjectCursor1D<Font> fontsCursor;
    private TextBox fontName;

    private FontTesterScene() {
    }

    public static final FontTesterScene getInstance() {
        return instance;
    }

    @Override
    public void onAdd() {
        entitiesBoard.getChildren().clear();
        PhaseTitle title = new PhaseTitle("Text Font Tester");
        entitiesBoard.getChildren().add(title);

        fontName = new TextBox();
        fontName.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));
        entitiesBoard.getChildren().add(fontName);

        Text mainTitle = new Text();
        mainTitle.setColor(PhaseTitle.DEFAULT_COLOR);
        mainTitle.setFont(MainMenuScene.MAIN_TITLE_FONT);
        mainTitle.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottom(fontName));
        mainTitle.setText("Main Title");
        entitiesBoard.getChildren().add(mainTitle);

        Text phaseTitle = new Text();
        phaseTitle.setColor(PhaseTitle.DEFAULT_COLOR);
        phaseTitle.setFont(PhaseTitle.PHASE_TITLE_FONT);
        phaseTitle.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(mainTitle));
        phaseTitle.setText("Phase Title");
        entitiesBoard.getChildren().add(phaseTitle);

        TextBox commonTextDialog = new TextBox();
        commonTextDialog.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(phaseTitle));
        commonTextDialog.setText("Common Text Dialog\nwith new line\nabcdefgasdkaskd ajsdkajsdka");
        entitiesBoard.getChildren().add(commonTextDialog);

        TextBox messageBox = new TextBox();
        messageBox.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(commonTextDialog));
        messageBox.setText("Message Box");
        messageBox.setFont(MessageBox.DEFAULT_FONT);
        messageBox.setForegroundColor(MessageBox.DEFAULT_FOREGROUND_COLOR);
        messageBox.setBackgroundColor(MessageBox.DEFAULT_BACKGROUND_COLOR);
        messageBox.setFlat(true);
        entitiesBoard.getChildren().add(messageBox);

        initializeFontCursor();
    }

    private void initializeFontCursor() {
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

                updateFont(fontsCursor.getSelected().getName());

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
        entitiesBoard.update(elapsedTime);

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            MyGame.getInstance().getPhaseManager().back();
        }
    }

    @Override
    public void render(Graphics2D g) {
        entitiesBoard.render(g);
    }

    private Font createNewFont(Font originalFont, String name) {
        return new Font(name, originalFont.getStyle(), originalFont.getSize());
    }

    private void updateFont(String fontName) {
        this.fontName.setText(fontName);
        for (VisualEntity e : entitiesBoard.getChildren()) {
            if (e instanceof Text) {
                Text text = (Text) e;
                text.setFont(createNewFont(text.getFont(), fontName));
            } else if (e instanceof TextBox) {
                TextBox text = (TextBox) e;
                text.setFont(createNewFont(text.getFont(), fontName));
            }
        }
    }
}
