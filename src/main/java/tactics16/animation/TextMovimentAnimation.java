package tactics16.animation;

import java.awt.Graphics2D;
import tactics16.components.TextBox;
import tactics16.game.Coordinate;
import tactics16.util.Align;
import tactics16.util.LinearMoviment;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TextMovimentAnimation implements VisualEntity {

    private final LinearMoviment moviment;
    private final TextBox textDialog;
    private long delayElapsedTime = 0l;
    private final long delay;

    public TextMovimentAnimation(Coordinate source, Coordinate target, long delay, double speed) {
        this.textDialog = new TextBox();
        this.textDialog.getPosition().set(source);
        this.textDialog.setHorizontalPosition(Align.NULL);
        this.textDialog.setVerticalPosition(Align.NULL);
        this.moviment = new LinearMoviment(this.textDialog.getPosition(), target, speed);
        this.delay = delay;
    }

    public void update(long elapsedTime) {
        if (moviment.isFinalized()) {
            delayElapsedTime += elapsedTime;
        } else {
            moviment.update(elapsedTime);
        }
    }

    public void render(Graphics2D g) {
        textDialog.render(g);
    }

    public boolean isFinalized() {
        return moviment.isFinalized() && delayElapsedTime >= delay;
    }

    public TextBox getDialog() {
        return textDialog;
    }
}
