package net.stuffrepos.tactics16.game;

import net.stuffrepos.tactics16.animation.SpriteAnimation;
import net.stuffrepos.tactics16.util.Nameable;
import org.newdawn.slick.Graphics;

/**
 *
 * @author eduardo
 */
public class Terrain implements Nameable {

    public static enum Layer {

        Base,
        Obstacle
    }
    private SpriteAnimation animation;
    private String name;
    private boolean block = false;
    private final Layer layer;
    private int width = 1;
    private int height = 1;

    public Terrain(Layer layer, String name) {
        this.layer = layer;
        this.name = name;
    }

    public Layer getLayer() {
        return layer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public SpriteAnimation getSpriteAnimation() {
        return animation;
    }
    
    public void setSpriteAnimation(SpriteAnimation animation) {
        this.animation = animation;
    }

    public void render(Graphics g, int x, int y) {        
        animation.getImageByIndex(0).render(g, x, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
