package net.stuffrepos.tactics16.util.image;

import java.awt.Dimension;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DrawerUtil {

    /**
     * Copied from java.awt.Graphics2D.fill3dRect.
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     * @param raised 
     */
    public static void fill3dRect(Graphics g, int x, int y, int width, int height, boolean raised) {
        Color c = g.getColor();
        Color brighter = c.brighter();
        Color darker = c.darker();

        if (!raised) {
            g.setColor(darker);
        }

        g.fillRect(x + 1, y + 1, width - 2, height - 2);
        g.setColor(raised ? brighter : darker);
        g.fillRect(x, y, 1, height);
        g.fillRect(x + 1, y, width - 2, 1);
        g.setColor(raised ? darker : brighter);
        g.fillRect(x + 1, y + height - 1, width - 1, 1);
        g.fillRect(x + width - 1, y, 1, height - 1);

        g.setColor(c);
    }

    public static void drawImage(Graphics g, Image image, Coordinate coordinate) {
        g.drawImage(image, coordinate.getX(), coordinate.getY());
    }

    public static void fillRect(Graphics g, Dimension size) {
        g.fillRect(0, 0, size.width, size.height);
    }

    public static void fillScreen(Graphics g) {
        g.fillRect(0, 0, MyGame.getInstance().getWidth(), MyGame.getInstance().getHeight());
    }
}
