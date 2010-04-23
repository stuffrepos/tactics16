package tactics16.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import tactics16.Layout;
import tactics16.animation.VisualEntity;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PhaseTitle implements VisualEntity, Object2D {

    public static final Font PHASE_TITLE_FONT = new Font("purisa", Font.BOLD, 24);
    public static final Color DEFAULT_COLOR = new Color(0x00FF00);
    private Text text;

    public PhaseTitle(String text) {
        this.text = new Text();
        this.text.setColor(DEFAULT_COLOR);
        this.text.setText(text);
        this.text.setFont(PHASE_TITLE_FONT);
        this.text.getPosition().setXY(Layout.getCentralizedLeft(this.text), Layout.OBJECT_GAP);
    }

    public void update(long elapsedTime) {
    }

    public void render(Graphics2D g) {
        text.render(g);
    }

    public boolean isFinalized() {
        return false;
    }

    public int getTop() {
        return text.getTop();
    }

    public int getLeft() {
        return text.getLeft();
    }

    public int getWidth() {
        return text.getWidth();
    }

    public int getHeight() {
        return text.getHeight();
    }
}
