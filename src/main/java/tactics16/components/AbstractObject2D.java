package tactics16.components;

import tactics16.game.Coordinate;
import tactics16.util.cache.CacheableValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class AbstractObject2D implements Object2D {

    private final Coordinate position = new Coordinate();
    private final CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            return calculateWidth();
        }
    };
    private final CacheableValue<Integer> height = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            return calculateHeight();
        }
    };

    protected abstract int calculateWidth();

    protected abstract int calculateHeight();

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
}
