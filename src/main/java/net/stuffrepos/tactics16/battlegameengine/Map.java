package net.stuffrepos.tactics16.battlegameengine;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Map {

    public Square getSquare(MapCoordinate coordinate);

    public int getWidth();

    public int getHeight();

    public interface Square extends Cloneable {

        public boolean isMovimentBlocked();

        public boolean isActionBlocked();
    }

    public interface MapCoordinate extends Cloneable {

        public int getY();

        public int getX();

        public MapCoordinate clone();
    }
}
