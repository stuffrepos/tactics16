package net.stuffrepos.tactics16.animation;

import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.ImageData;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class GameImage implements Cloneable {

    private Image image;
    private Coordinate center;
    private static final double NO_SCALE = 1.0;
    private double scale = NO_SCALE;
    private CacheableValue<GameImage> scaledImage = new CacheableValue<GameImage>() {

        @Override
        protected GameImage calculate() {
            return new GameImage(
                    image.getScaledCopy((float) scale),
                    new Coordinate(
                    center.getX() * scale,
                    center.getY() * scale),
                    NO_SCALE);

        }
    };
    private CacheableValue<GameImage> flippedX = new CacheableValue<GameImage>() {

        @Override
        protected GameImage calculate() {
            GameImage flipped = new GameImage(image.getFlippedCopy(true, false));
            flipped.getCenter().setXY(
                    flipped.image.getWidth() - center.getX(),
                    center.getY());
            return flipped;
        }
    };

    public GameImage(Image image) {
        this(image, new Coordinate(), 1.0);
    }

    public GameImage(ImageData imageData) {
        this(new Image(imageData));
    }

    public GameImage(Image image, Coordinate center, double scale) {
        this.image = image;
        this.center = center.clone();
        this.scale = scale;
    }

    public void render(Graphics g, Coordinate position) {
        render(g, position, false, false);
    }

    public void render(Graphics g, int x, int y) {
        render(g, x, y, false, false);
    }

    public void render(Graphics g, int x, int y, boolean invertX, boolean invertY) {

        assert invertY == false : "Not yet implemented Y invert";                

        if (scale == NO_SCALE) {
            if (invertX) {
                flippedX.getValue().render(g, x, y, false, false);
            } else {
                g.drawImage(
                        image,
                        x - center.getX(),
                        y - center.getY());
            }
        } else {
            scaledImage.getValue().render(g, x, y, invertX, invertY);
        }
    }

    public Image getImage() {
        return image;
    }

    public Coordinate getCenter() {
        if (scale == NO_SCALE) {
            return center;
        } else {
            return scaledImage.getValue().getCenter();
        }
    }

    @Override
    public GameImage clone() {
        return clone(this.image);
    }

    public GameImage clone(Image image) {
        return new GameImage(image, center, scale);
    }

    public void render(Graphics g, Coordinate position, boolean invertX, boolean invertY) {
        render(g, position.getX(), position.getY(), invertX, invertY);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public GameImage getScaledImage() {
        return scaledImage.getValue();
    }
}
