package tactics16.components;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Object2D {

    public static class Formater {

        public static String toString(Object2D o) {
            return String.format("x/y: %d/%d, w/h: %d/%d", o.getLeft(), o.getTop(), o.getWidth(), o.getHeight());
        }
    }

    public int getTop();

    public int getLeft();

    public int getWidth();

    public int getHeight();
}
