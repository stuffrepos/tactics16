package net.stuffrepos.tactics16.util.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ImageUtil {

    public static Image scaleImage(Image source, double scale) {
        return source.getScaledCopy((float) scale);
    }

    public static Image copyBitmask(Image input) {
        return new PixelImageCopyIterator(input) {

            @Override
            protected Color iterate(int x, int y, Color color) {
                return ColorUtil.getColorBitmask(color);
            }
        }.build();
    }

    public static void setColor(ImageBuffer image, int x, int y, Color color) {
        image.setRGBA(x, y, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static org.newdawn.slick.Image grayScale(org.newdawn.slick.Image source) {
        return new PixelImageCopyIterator(source) {

            @Override
            protected Color iterate(int x, int y, Color color) {
                if (color.getAlpha() == 0) {
                    return Color.transparent;
                } else {
                    return ColorUtil.grayScale(color);
                }
            }
        }.build();
    }

    public static Image newImage(Dimension size) throws SlickException {
        return new Image(size.width, size.height);
    }

    public static ImageBuffer newImageBuffer(Dimension size) {
        return new ImageBuffer(size.width, size.height);
    }
}
