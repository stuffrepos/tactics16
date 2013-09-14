package net.stuffrepos.tactics16.scenes.test;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class FontTesterScene extends Phase {

    private static final FontTesterScene instance = new FontTesterScene();
    private final EntitiesBoard entitiesBoard = new EntitiesBoard();

    private FontTesterScene() {
    }

    public static final FontTesterScene getInstance() {
        return instance;
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {
        entitiesBoard.getChildren().clear();
        PhaseTitle title = new PhaseTitle("Text Font Tester");
        entitiesBoard.getChildren().add(title);

        Text mainTitle = new Text();
        mainTitle.setColor(PhaseTitle.DEFAULT_COLOR);
        mainTitle.setFont(MyGame.FontType.MainTitle);
        mainTitle.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottom(title));
        mainTitle.setText("Main Title");
        entitiesBoard.getChildren().add(mainTitle);

        Text phaseTitle = new Text();
        phaseTitle.setColor(PhaseTitle.DEFAULT_COLOR);
        phaseTitle.setFont(MyGame.FontType.PhaseTitle);
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
        messageBox.setFont(MyGame.FontType.Message);
        messageBox.setForegroundColor(MessageBox.DEFAULT_FOREGROUND_COLOR);
        messageBox.setBackgroundColor(MessageBox.DEFAULT_BACKGROUND_COLOR);
        messageBox.setFlat(true);
        entitiesBoard.getChildren().add(messageBox);
        
        Text personInfo = new Text();
        personInfo.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(messageBox));
        personInfo.setText("Person Information");
        personInfo.setFont(MyGame.FontType.Message);
        personInfo.setColor(Color.cyan);
        entitiesBoard.getChildren().add(personInfo);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        entitiesBoard.update(delta);

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            MyGame.getInstance().getPhaseManager().back();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        entitiesBoard.render(g);
    }
}
