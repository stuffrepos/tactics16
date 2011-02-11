package net.stuffrepos.tactics16.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MessageBox implements VisualEntity, Object2D {

    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    public static final Font DEFAULT_FONT = new Font(MyGame.getInstance().getFont().getName(), Font.BOLD, 16);
    private boolean finalized;
    private TextBox textBox;
    private long elapsedTime = 0;
    private Integer timeout;

    public MessageBox(String text, Object2D centralizeOn) {
        this(text, centralizeOn, null);
    }

    public MessageBox(String text, Object2D centralizeOn, Integer timeout) {
        textBox = new TextBox();
        textBox.setText(text);
        textBox.setFlat(true);
        textBox.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        textBox.setForegroundColor(DEFAULT_FOREGROUND_COLOR);
        textBox.setFont(DEFAULT_FONT);
        if (centralizeOn != null) {
            getPosition().set(Layout.getCentralizedOnObject2D(centralizeOn, this));
        }
        this.timeout = timeout;
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
        if (!isFinalized()) {
            for (GameKey gameKey : GameKey.values()) {
                if (MyGame.getInstance().isKeyPressed(gameKey)) {
                    finalized = true;
                    break;
                }
            }
            if (timeout != null && this.elapsedTime >= timeout) {
                finalized = true;
            }
        } 
    }

    public void render(Graphics2D g) {
        textBox.render(g);
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
        return textBox.getWidth();
    }

    public int getHeight() {
        return textBox.getHeight();
    }

    private Coordinate getPosition() {
        return textBox.getPosition();
    }

    public void createPhase(PhaseManager phaseManager) {
        new EmbeddedPhase(phaseManager);
    }

    public void createPhase() {
        createPhase(MyGame.getInstance().getPhaseManager());
    }

    public MessageBox setBackgroundColor(Color color) {
        textBox.setBackgroundColor(color);
        return this;
    }

    public MessageBox setForegroundColor(Color color) {
        textBox.setForegroundColor(color);
        return this;
    }

    private class EmbeddedPhase extends AbstractPhase {

        private final PhaseManager phaseManager;
        private Phase previousPhase;

        private EmbeddedPhase(PhaseManager phaseManager) {
            this.phaseManager = phaseManager;
            previousPhase = phaseManager.getCurrentPhase();
            phaseManager.advance(this);
        }

        @Override
        public void update(long elapsedTime) {
            MessageBox.this.update(elapsedTime);

            if (MessageBox.this.isFinalized()) {
                phaseManager.back();
            }
        }

        @Override
        public void render(Graphics2D g) {
            previousPhase.render(g);
            MessageBox.this.render(g);
        }
    }
}
