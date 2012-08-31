package net.stuffrepos.tactics16.battlegameengine;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Map extends Cloneable {

    public Square getSquare(Coordinate coordinate);

    public int getWidth();

    public int getHeight();

    public Map clone();

    public interface Square extends Cloneable {

        public boolean isMovimentBlocked();

        public boolean isActionBlocked();

        public Square clone();
    }

    public interface Coordinate extends Cloneable {

        public int getY();

        public int getX();

        public Coordinate clone();
    }
}
