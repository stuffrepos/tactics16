package net.stuffrepos.tactics16.animation;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ImageGroup {

    private java.util.Map<String, GameImage> images = new TreeMap<String, GameImage>();

    public GameImage getImage(String name) {
        return images.get(name);
    }

    public void addImage(String name, GameImage image) {
        images.put(name, image);
    }

    public boolean hasImage(String name) {
        return images.get(name) != null;
    }
}
