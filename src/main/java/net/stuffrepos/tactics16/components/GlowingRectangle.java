package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.game.Coordinate;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class GlowingRectangle {

    private int IMAGE_COUNT = 8;
    private int IMAGE_CHANGE = 1600/IMAGE_COUNT;
    private float MIN_TRANSPARENCY = 0.30f;
    private float MAX_TRANSPARENCY = 0.46f;
    private BufferedImage[] cursorImages = new BufferedImage[IMAGE_COUNT];
    private Coordinate position = new Coordinate();
    private long elapsedTime = 0;
    private int color;
    private Dimension size;

    public GlowingRectangle(int size) {
        this(0x0000FF, size);
    }

    public GlowingRectangle(int color, int size) {
        this(color, new Dimension(size, size));
    }

    public GlowingRectangle(int color, Dimension size) {
        this.color = color;
        this.size = size;

        float d = (MAX_TRANSPARENCY - MIN_TRANSPARENCY) / (cursorImages.length - 1);

        for (int i = 0; i < cursorImages.length; ++i) {
            cursorImages[i] = new BufferedImage(
                    this.size.width,
                    this.size.height,
                    Transparency.TRANSLUCENT);

            float transparency = d * i + MIN_TRANSPARENCY;
            int alpha = ((int) (0xFF * transparency)) << 24;
            int rgb = alpha | this.color;

            for (int x = 0; x < cursorImages[i].getWidth(); ++x) {
                for (int y = 0; y < cursorImages[i].getHeight(); ++y) {
                    cursorImages[i].setRGB(
                            x,
                            y,
                            rgb);
                }
            }
        }
    }

    public GlowingRectangle() {
        this(32);
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public void render(Graphics2D g) {
        g.drawImage(getCurrentImage(), getPosition().getX(), getPosition().getY(), null);
    }

    public BufferedImage getCurrentImage() {
        int cicleN = cursorImages.length * 2 - 1;
        int cicle = (int) (elapsedTime / IMAGE_CHANGE) % cicleN;
        int imageIndex = cicle >= cursorImages.length
                ? cicleN - cicle - 1
                : cicle;

        return cursorImages[imageIndex];
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
