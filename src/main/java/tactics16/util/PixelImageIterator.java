package tactics16.util;

import java.awt.image.BufferedImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class PixelImageIterator {    

    public PixelImageIterator(BufferedImage image) {        
        for(int x=0; x < image.getWidth(); ++x) {
            for(int y=0; y < image.getHeight(); ++y) {
                iterate(x,y,image.getRGB(x, y));
            }
        }
    }

    public abstract void iterate(int x,int y,int rgb);
}
