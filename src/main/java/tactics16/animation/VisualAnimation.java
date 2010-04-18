package tactics16.animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import tactics16.game.Coordinate;

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

    public BufferedImage getCurrentImage() {
        if (animation.getImagesCount() > 0 && elapsedTime >= 0l) {
            return animation.getImage(elapsedTime);
        } else {
            return null;
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(getCurrentImage(), position.getX(), position.getY(), null);
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
