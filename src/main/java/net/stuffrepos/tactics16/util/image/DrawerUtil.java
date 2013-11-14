package net.stuffrepos.tactics16.util.image;

import java.awt.Dimension;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.game.Coordinate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DrawerUtil {

    public static final int BORDER_3D = 1;

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

        g.fillRect(x + BORDER_3D, y + BORDER_3D, width - BORDER_3D * 2, height - BORDER_3D * 2);
        g.setColor(raised ? brighter : darker);
        g.fillRect(x, y, BORDER_3D, height);
        g.fillRect(x + BORDER_3D, y, width - BORDER_3D * 2, BORDER_3D);
        g.setColor(raised ? darker : brighter);
        g.fillRect(x + BORDER_3D, y + height - BORDER_3D, width - BORDER_3D, BORDER_3D);
        g.fillRect(x + width - BORDER_3D, y, BORDER_3D, height - BORDER_3D);

        g.setColor(c);
    }

    public static void fill3dRect(Graphics g, Object2D object, boolean b) {
        fill3dRect(
                g,
                object.getLeft(),
                object.getTop(),
                object.getWidth(),
                object.getHeight(),
                b);
    }

    public static void drawImage(Graphics g, Image image, Coordinate coordinate) {
        g.drawImage(image, coordinate.getX(), coordinate.getY());
    }

    public static void fillRect(Graphics g, Dimension size) {
        g.fillRect(0, 0, size.width, size.height);
    }

    public static void objectBackground(Graphics g, Object2D object, Color color) {
        g.setColor(color);
        g.fillRect(
                object.getLeft(),
                object.getTop(),
                object.getWidth(),
                object.getHeight());
    }

    public static void objectBorder(Graphics g, Object2D object, Color color) {
        g.setColor(color);
        g.drawRect(
                object.getLeft(),
                object.getTop(),
                object.getWidth(),
                object.getHeight());
    }

    public static void objectBox(Graphics g, Object2D object, Color backgroundColor, Color borderColor) {
        objectBackground(g, object, backgroundColor);
        objectBorder(g, object, borderColor);
    }

    public static void fillScreen(Graphics g) {
        g.fillRect(0, 0, MyGame.getInstance().getWidth(), MyGame.getInstance().getHeight());
    }
}
