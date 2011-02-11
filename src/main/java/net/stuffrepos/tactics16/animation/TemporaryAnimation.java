package net.stuffrepos.tactics16.animation;

import java.awt.Graphics2D;
import net.stuffrepos.tactics16.game.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TemporaryAnimation implements VisualEntity {

    private final VisualAnimation animation;
    private final long loopLimit;

    public TemporaryAnimation(SpriteAnimation spriteAnimation, long loopLimit) {
        animation = new VisualAnimation();
        animation.setAnimation(spriteAnimation);
        this.loopLimit = loopLimit;
    }

    public void update(long elapsedTime) {
        animation.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        animation.render(g);
    }

    public boolean isFinalized() {
        return animation.getLoopCount() >= loopLimit;
    }

    public Coordinate getPosition() {
        return animation.getPosition();
    }
}
