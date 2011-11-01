package net.stuffrepos.tactics16.animation;

import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualAnimation {

    private Coordinate position = new Coordinate();
    private long elapsedTime;
    private SpriteAnimation animation = null;

    public void setAnimation(SpriteAnimation animation) {
        if (animation != this.animation) {
            this.animation = animation;
            this.elapsedTime = 0l;
        }
    }

    public GameImage getCurrentImage() {
        if (animation.getImagesCount() > 0 && elapsedTime >= 0l) {
            return animation.getImage(elapsedTime);
        } else {
            return null;
        }
    }

    public void render(Graphics g) {
        GameImage image = getCurrentImage();
        if (image != null) {
            image.render(g, position);
        }
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public long getLoopCount() {
        return this.animation.getLoopCount(elapsedTime);
    }

    public Coordinate getPosition() {
        return position;
    }
}
