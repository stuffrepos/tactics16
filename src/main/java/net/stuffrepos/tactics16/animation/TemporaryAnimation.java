package net.stuffrepos.tactics16.animation;

import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Graphics;

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

    public void update(int delta) {
        animation.update(delta);
    }

    public void render(Graphics g) {
        animation.render(g);
    }

    public boolean isFinalized() {
        return animation.getLoopCount() >= loopLimit;
    }

    public Coordinate getPosition() {
        return animation.getPosition();
    }
}
