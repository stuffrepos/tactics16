package net.stuffrepos.tactics16.util.image;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class PixelImageCopyIterator {

    private final Image source;

    public PixelImageCopyIterator(Image source) {
        this.source = source;
    }

    protected abstract Color iterate(int x, int y, Color color);

    public Image build() {
        final ImageBuffer buffer = new ImageBuffer(source.getWidth(), source.getHeight());
        new PixelImageIterator(source) {

            @Override
            public void iterate(int x, int y, Color color) {
                ImageUtil.setColor(buffer, x, y, PixelImageCopyIterator.this.iterate(x, y, color));
            }
        };

        return new Image(buffer);
    }
}
