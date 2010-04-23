package tactics16.components;

import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.animation.VisualEntity;
import tactics16.game.Coordinate;
import tactics16.phase.AbstractPhase;
import tactics16.phase.Phase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MessageBox implements VisualEntity, Object2D {

    private boolean finalized;
    private TextDialog textDialog;

    public MessageBox(String text, Object2D centralizeOn) {
        textDialog = new TextDialog();
        textDialog.setText(text);
        if (centralizeOn != null) {
            getPosition().set(Layout.getCentralizedOnObject2D(centralizeOn, this));
        }
    }

    public void update(long elapsedTime) {
        if (!isFinalized()) {
            for (GameKey gameKey : GameKey.values()) {
                if (MyGame.getInstance().isKeyPressed(gameKey)) {
                    finalized = true;
                    break;
                }
            }
        }
    }

    public void render(Graphics2D g) {
        textDialog.render(g);
    }

    public boolean isFinalized() {
        return finalized;
    }

    public int getTop() {
        return getPosition().getX();
    }

    public int getLeft() {
        return getPosition().getY();
    }

    public int getWidth() {
        return textDialog.getWidth();
    }

    public int getHeight() {
        return textDialog.getHeight();
    }

    private Coordinate getPosition() {
        return textDialog.getPosition();
    }

    public Phase createPhase() {
        return new EmbeddedPhase();
    }

    private class EmbeddedPhase extends AbstractPhase {

        @Override
        public void update(long elapsedTime) {
            MessageBox.this.update(elapsedTime);

            if (MessageBox.this.isFinalized()) {
                MyGame.getInstance().getPhaseManager().back();
            }
        }

        @Override
        public void render(Graphics2D g) {
            MessageBox.this.render(g);
        }        
    }
}
