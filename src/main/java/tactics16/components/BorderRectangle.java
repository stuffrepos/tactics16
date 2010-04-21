package tactics16.components;

import tactics16.game.Coordinate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class BorderRectangle {

    private static Color DEFAULT_COLOR = Color.WHITE;
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

    public void render(Graphics2D g) {
        g.setColor(color);
        drawRect(g,0);
        g.setColor(Color.BLACK);
        drawRect(g,1);
        g.setColor(color);
        drawRect(g,2);
    }

    private void drawRect(Graphics2D g, int level) {
        g.drawRect(
                position.getX() - level, position.getY() - level,
                size.width + level * 2, size.height + level * 2);
    }

    public Coordinate getPosition() {
        return position;
    }
}
