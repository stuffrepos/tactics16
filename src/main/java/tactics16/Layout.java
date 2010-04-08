package tactics16;

import tactics16.components.Object2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Layout {

    public static final int OBJECT_GAP = 5;

    public static String toString(Object2D o) {
        return String.format(
                "l=%d,t=%d,w=%d,h=%d,r=%d,b=%d",
                o.getLeft(),
                o.getTop(),
                o.getWidth(),
                o.getHeight(),
                Layout.getRight(o),
                Layout.getBottom(o));
    }

    public static int getBottom(Object2D o) {
        return o.getTop() + o.getHeight();
    }

    public static int getRight(Object2D o) {
        return o.getLeft() + o.getWidth();
    }

    public static int getCentralizedLeft(Object2D o) {
        return (getScreenWidth() - o.getWidth()) / 2;
    }

    public static int getScreenHeight() {
        return MyGame.getInstance().getHeight();
    }

    public static int getScreenWidth() {
        return MyGame.getInstance().getWidth();
    }

    public static int getRightGap(Object2D o) {
        return getRight(o) + OBJECT_GAP;
    }

    public static int getBottomGap(Object2D o) {
        return getBottom(o) + OBJECT_GAP;
    }
}
