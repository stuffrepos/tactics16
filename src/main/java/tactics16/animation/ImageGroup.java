package tactics16.animation;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ImageGroup {

    private java.util.Map<String, BufferedImage> images = new TreeMap<String, BufferedImage>();

    public BufferedImage getImage(String name) {
        return images.get(name);
    }

    public void addImage(String name, BufferedImage image) {
        images.put(name, image);
    }

    public boolean hasImage(String name) {
        return images.get(name) != null;
    }
}
