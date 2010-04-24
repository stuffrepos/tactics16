package tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.Layout;
import tactics16.animation.GameImage;
import tactics16.animation.SpriteAnimation;
import tactics16.animation.VisualEntity;
import tactics16.game.Coordinate;
import tactics16.util.SizeConfig;
import tactics16.util.cache.CacheableValue;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class AnimationBox implements VisualEntity, Object2D {

    private Color backgroundColor = Color.WHITE;
    private final SpriteAnimation animation;
    private long elapsedTime = 0l;
    private Coordinate position = new Coordinate();
    private SizeConfig width = new SizeConfig() {

        @Override
        protected int calculate() {
            int max = 0;
            for (GameImage image : animation.getImages()) {
                int width = image.getImage().getWidth();
                if (width > max) {
                    max = width;
                }
            }
            return max + 2 * Layout.OBJECT_GAP;
        }
    };
    private SizeConfig height = new SizeConfig() {

        @Override
        protected int calculate() {
            int max = 0;
            for (GameImage image : animation.getImages()) {
                int height = image.getImage().getHeight();
                if (height > max) {
                    max = height;
                }
            }
            return max + 2 * Layout.OBJECT_GAP;
        }
    };
    private CacheableValue<Coordinate> spritePosition = new CacheableValue<Coordinate>() {

        @Override
        protected Coordinate calculate() {
            int maxLeftMargin = 0;
            int maxTopMargin = 0;

            for (GameImage image : animation.getImages()) {
                if (image.getCenter().getX() > maxLeftMargin) {
                    maxLeftMargin = image.getCenter().getX();
                }
                if (image.getCenter().getY() > maxTopMargin) {
                    maxTopMargin = image.getCenter().getY();
                }
            }

            return new Coordinate(
                    getPosition(),
                    Layout.OBJECT_GAP + maxLeftMargin,
                    Layout.OBJECT_GAP + maxTopMargin);
        }
    };

    public AnimationBox(SpriteAnimation animation) {
        this.animation = animation;
        this.position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                spritePosition.clear();
            }
        });
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public void render(Graphics2D g) {
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fill3DRect(getLeft(), getTop(), getWidth(), getHeight(), false);
        }
        animation.getImage(elapsedTime).render(g, spritePosition.getValue());
    }

    public boolean isFinalized() {
        return false;
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return width.getValue();
    }

    public int getHeight() {
        return height.getValue();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public SizeConfig getWidthConfig() {
        return width;
    }

    public SizeConfig getHeightConfig() {
        return height;
    }
}
