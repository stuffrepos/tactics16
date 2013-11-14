package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Color;
import java.awt.Dimension;
import org.newdawn.slick.Graphics;

public class BorderRectangle implements Object2D {

    private static final int BORDER_SIZE = 3;
    private static Color DEFAULT_COLOR = Color.white;
    private Coordinate position = new Coordinate();
    private Color color;
    private Dimension size;

    public BorderRectangle(int size) {
        this(size, DEFAULT_COLOR);
    }

    public BorderRectangle(int size, Color color) {
        this(new Dimension(size, size), color);
    }

    public BorderRectangle(Dimension size, Color color) {
        this.color = color;
        this.size = size;
    }

    public BorderRectangle(Dimension size) {
        this(size,DEFAULT_COLOR);
    }

    public void update(long elapsedTime) {
    }

    public void render(Graphics g) {
        for (int i = 0; i < getBorderSize(); ++i) {
            g.setColor(i % 2 == 0 ? color : Color.black);
            drawRect(g, i);
        }
    }

    private void drawRect(Graphics g, int level) {
        g.drawRect(
                position.getX() - level - 1,
                position.getY() - level - 1,
                size.width + (level + 1) * 2 - 1,
                size.height + (level + 1) * 2 - 1);
    }

    public Coordinate getPosition() {
        return position;
    }

    public Dimension getSize() {
        return size;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return size.width;
    }

    public int getHeight() {
        return size.height;
    }

    public int getBorderSize() {
        return BORDER_SIZE;
    }

    public void wrap(Object2D object) {
        getPosition().setXY(object.getLeft(), object.getTop());
        getSize().setSize(object.getWidth(), object.getHeight());
    }
}
