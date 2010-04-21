package tactics16.animation;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import tactics16.game.Coordinate;
import tactics16.util.TransformUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class GameImage {

    private BufferedImage image;
    private Coordinate center;

    public GameImage(BufferedImage image) {
        this.image = image;
        this.center = new Coordinate();
    }

    public void render(Graphics2D g, Coordinate position) {
        render(g, position.getX(), position.getY());
    }

    public void render(Graphics2D g, int x, int y) {
        render(g, x, y, false, false);
    }

    public void render(Graphics2D g, int x, int y, boolean invertX, boolean invertY) {

        assert invertY = true : "Not yet implemented Y invert";

        if (invertX) {
            AffineTransform flipTransform = TransformUtil.getFlipHorizontalTransform(
                    image);

            flipTransform.preConcatenate(AffineTransform.getTranslateInstance(
                    x - center.getX(),
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

    public GameImage sameSize() {
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                Transparency.BITMASK);

        GameImage gameImage = new GameImage(bufferedImage);
        gameImage.getCenter().set(center);

        return gameImage;
    }
}
