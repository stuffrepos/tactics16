package net.stuffrepos.tactics16.util.image;

import java.awt.RenderingHints;
import org.newdawn.slick.Image;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Thumbnail {

    public static Image getThumbnail(Image image,
            int maxThumbWidth,
            int maxThumbHeight,
            RenderingHints hints) {
        return image.getScaledCopy(maxThumbWidth, maxThumbHeight);
    }
}
