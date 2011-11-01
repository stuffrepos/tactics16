package net.stuffrepos.tactics16.scenes;

import java.awt.Font;
import org.newdawn.slick.Graphics;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;

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
        final String initialFontName = MyGame.DEFAULT_TRUE_TYPE_FAMILY_NAME;

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
    public void render(Graphics g) {
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
                text.setFont(new Font(fontName,Font.PLAIN,12));
            } else if (e instanceof TextBox) {
                TextBox text = (TextBox) e;
                text.setFont(new Font(fontName,Font.PLAIN,12));                
            }
        }
    }
}
