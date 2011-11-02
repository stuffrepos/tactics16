package net.stuffrepos.tactics16.animation;

import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.Align;
import net.stuffrepos.tactics16.util.LinearMoviment;
import org.newdawn.slick.Graphics;

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

    public void update(int delta) {
        if (moviment.isFinalized()) {
            delayElapsedTime += delta;
        } else {
            moviment.update(delta);
        }
    }

    public void render(Graphics g) {
        textDialog.render(g);
    }

    public boolean isFinalized() {
        return moviment.isFinalized() && delayElapsedTime >= delay;
    }

    public TextBox getDialog() {
        return textDialog;
    }
}
