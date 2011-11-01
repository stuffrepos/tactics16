package net.stuffrepos.tactics16.animation;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.image.ImageUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class GameImage implements Cloneable {

    private Image image;
    private Coordinate center;
    private double scale = 1.0;
    private CacheableValue<GameImage> scaledImage = new CacheableValue<GameImage>() {

        @Override
        protected GameImage calculate() {
            GameImage scaledImage = new GameImage(ImageUtil.scaleImage(ImageUtil.slickToAwt(image), scale));
            scaledImage.getCenter().setXY(
                    (int) (center.getX() * scale),
                    (int) (center.getY() * scale));
            return scaledImage;
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
        this.image = image;
        this.center = new Coordinate();
    }

    public GameImage(BufferedImage image) {
        this(ImageUtil.awtToSlick(image));
    }

    public void render(Graphics g, Coordinate position) {
        render(g, position, false, false);
    }

    public void render(Graphics g, int x, int y) {
        render(g, x, y, false, false);
    }

    public void render(Graphics g, int x, int y, boolean invertX, boolean invertY) {

        assert invertY == false : "Not yet implemented Y invert";                

        if (invertX) {            
            flippedX.getValue().render(g,x,y,false,false);
        } else {
            g.drawImage(
                    image,
                    x - center.getX(),
                    y - center.getY(),
                    null);
        }
    }

    public BufferedImage getImage() {
        return ImageUtil.slickToAwt(image);
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
