package tactics16.animation;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import tactics16.game.Coordinate;
import tactics16.util.cache.CacheableValue;
import tactics16.util.image.ImageUtil;
import tactics16.util.image.TransformUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class GameImage implements Cloneable {

    private BufferedImage image;
    private Coordinate center;
    private double scale = 1.0;
    private CacheableValue<GameImage> scaledImage = new CacheableValue<GameImage>() {

        @Override
        protected GameImage calculate() {
            GameImage scaledImage = new GameImage(ImageUtil.scaleImage(image, scale));
            scaledImage.getCenter().setXY(
                    (int) (center.getX() * scale),
                    (int) (center.getY() * scale));
            return scaledImage;
        }
    };

    public GameImage(BufferedImage image) {
        this.image = image;
        this.center = new Coordinate();
    }

    public void render(Graphics2D g, Coordinate position) {
        render(g, position, false, false);
    }

    public void render(Graphics2D g, int x, int y) {
        render(g, x, y, false, false);
    }

    public void render(Graphics2D g, int x, int y, boolean invertX, boolean invertY) {

        assert invertY == false : "Not yet implemented Y invert";

        if (invertX) {
            AffineTransform flipTransform = TransformUtil.getFlipHorizontalTransform(
                    image);

            flipTransform.preConcatenate(AffineTransform.getTranslateInstance(
                    x - (image.getWidth() - center.getX()),
                    y - center.getY()));

            g.drawImage(image, flipTransform, null);

        } else {
            g.drawImage(
                    image,
                    x - center.getX(),
                    y - center.getY(),
                    null);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Coordinate getCenter() {
        return center;
    }

    @Override
    public GameImage clone() {
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                Transparency.BITMASK);

        GameImage gameImage = new GameImage(bufferedImage);
        gameImage.getCenter().set(center);
        gameImage.setScale(scale);

        return gameImage;
    }

    public void render(Graphics2D g, Coordinate position, boolean invertX, boolean invertY) {
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
