package net.stuffrepos.tactics16.util.image;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class PixelImageIterator {    

    public PixelImageIterator(Image image) {
        for(int x=0; x < image.getWidth(); ++x) {
            for(int y=0; y < image.getHeight(); ++y) {
                iterate(x,y,image.getColor(x, y));
            }
        }
    }

    public abstract void iterate(int x,int y,Color color);
}
