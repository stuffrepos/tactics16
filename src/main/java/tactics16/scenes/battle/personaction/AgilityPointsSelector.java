package tactics16.scenes.battle.personaction;

import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.animation.VisualEntity;
import tactics16.components.Object2D;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class AgilityPointsSelector implements VisualEntity, Object2D {

    private static int SPHERE_SIZE = 8;
    private static int SPHERE_GAP = SPHERE_SIZE / 4;
    private static int HEIGHT = SPHERE_SIZE + SPHERE_GAP * 2;
    private static Color SELECTED_COLOR = new Color(0x00FF00);
    private static Color UNSELECTED_COLOR = new Color(0x3300FF00, true);
    private static Color BACKGROUND_COLOR = new Color(0xAA000000, true);
    private static Color BORDER_COLOR = Color.WHITE;
    private final int max;
    private int count;
    private Coordinate position = new Coordinate();
    private final Spheres spheres;
    private final TextDialog text;

    public AgilityPointsSelector(int max) {
        this.max = max;
        this.spheres = new Spheres();
        this.text = new TextDialog();
        setCount(max / 2);

        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {

                text.getPosition().set(source);
                spheres.getPosition().setXY(
                        text.getLeft(),
                        Layout.getBottomGap(text));
            }
        });

    }

    private void setCount(int count) {
        this.count = count;
        if (this.count < 0) {
            this.count = 0;
        }
        if (this.count > max) {
            this.count = max;
        }
        text.setText("AP: +" + this.count + "/" + max);
    }

    public void update(long elapsedTime) {
        spheres.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        spheres.render(g);
        text.render(g);
    }

    public boolean isFinalized() {
        return false;
    }

    public int getTop() {
        return text.getTop();
    }

    public int getLeft() {
        return text.getLeft();
    }

    public int getWidth() {
        return Math.max(spheres.getWidth(), text.getWidth());
    }

    public int getHeight() {
        return spheres.getHeight() + text.getHeight() + Layout.OBJECT_GAP;
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getAgilityPoints() {
        return this.count;
    }

    private class Spheres implements Object2D {

        private Coordinate position = new Coordinate();

        public void update(long elapsedTime) {

            if (MyGame.getInstance().isKeyPressed(GameKey.LEFT)) {
                setCount(count - 1);
            } else if (MyGame.getInstance().isKeyPressed(GameKey.RIGHT)) {
                setCount(count + 1);
            }
        }

        public void render(Graphics2D g) {

            g.setColor(BACKGROUND_COLOR);
            g.fillRect(
                    getPosition().getX(),
                    getPosition().getY(),
                    getWidth(),
                    getHeight());
            g.setColor(BORDER_COLOR);
            g.drawRect(
                    getPosition().getX(),
                    getPosition().getY(),
                    getWidth(),
                    getHeight());

            for (int i = 0; i < max; ++i) {
                if (i < count) {
                    g.setColor(SELECTED_COLOR);
                } else {
                    g.setColor(UNSELECTED_COLOR);
                }
                g.fillOval(
                        getPosition().getX() + i * (SPHERE_SIZE + SPHERE_GAP) + SPHERE_GAP,
                        getPosition().getY() + SPHERE_GAP,
                        SPHERE_SIZE,
                        SPHERE_SIZE);
                g.setColor(BORDER_COLOR);
                g.drawOval(
                        getPosition().getX() + i * (SPHERE_SIZE + SPHERE_GAP) + SPHERE_GAP,
                        getPosition().getY() + SPHERE_GAP,
                        SPHERE_SIZE,
                        SPHERE_SIZE);
            }
        }

        public Coordinate getPosition() {
            return position;
        }

        public int getTop() {
            return getPosition().getY();
        }

        public int getLeft() {
            return getPosition().getX();
        }

        public int getWidth() {
            return (SPHERE_SIZE + SPHERE_GAP) * max + SPHERE_GAP;
        }

        public int getHeight() {
            return HEIGHT;
        }
    }
}
