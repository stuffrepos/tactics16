package tactics16.game;

import tactics16.util.listeners.Listener;
import tactics16.util.listeners.ListenerManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Coordinate implements Comparable<Coordinate>, Cloneable {

    private ListenerManager<Coordinate> listenerManager = new ListenerManager<Coordinate>(this);
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.setXY(x, y);
    }

    public Coordinate() {
        this(0, 0);
    }

    public Coordinate(JSONObject jsonObject) throws JSONException {
        this(jsonObject.getInt("x"), jsonObject.getInt("y"));
    }

    public Coordinate(Coordinate coordinate) {
        this.set(coordinate);
    }

    public Coordinate(Coordinate c, double dx, double dy) {
        this.setXY(c.x + dx, c.y + dy);
    }

    public void addListener(Listener<Coordinate> listener) {
        listenerManager.addListener(listener);
    }

    public void setXY(double x, double y) {
        if (this.x != x || this.y != y) {
            this.y = y;
            this.x = x;
            listenerManager.fireChange();
        }
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.setXY(this.x, y);
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.setXY(x, this.y);
    }

    public int compareTo(Coordinate o) {
        if ((this.x == o.x) && (this.y == o.y)) {
            return 0;
        } else if (this.y == o.y) {
            return (this.x < o.x ? -1 : 1);
        } else {
            return (this.y < o.y ? -1 : 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            return this.compareTo((Coordinate) obj) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (23 * hash + this.x);
        hash = (int) (23 * hash + this.y);
        return hash;
    }

    public void set(Coordinate coordinate) {
        this.setXY(coordinate.x, coordinate.y);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%fx%f", x, y);
    }

    public String toStringInt() {
        return String.format("%dx%d", (int) x, (int) y);
    }

    @Override
    public Coordinate clone() {
        return new Coordinate(x, y);
    }

    public void addX(double dx) {
        this.setX(x + dx);
    }

    public void addY(double dy) {
        this.setY(y + dy);
    }

    public void addXY(double dx, double dy) {
        this.setXY(x + dx, y + dy);
    }

    public double getDoubleX() {
        return x;
    }

    public double getDoubleY() {
        return y;
    }

    public void add(Coordinate d) {
        this.addXY(d.x, d.y);
    }

    public boolean inRectangle(int x, int y, int width, int height) {
        return this.x >= x &&
                this.x < x + width &&
                this.y >= y &&
                this.y < y + height;
    }

    public void set(Coordinate p, int dx, int dy) {
        this.setXY(p.x + dx, p.y + dy);
    }

    public boolean inRectangle(Coordinate position, Coordinate size) {
        return inRectangle(position.getX(), position.getY(),size.getX(),size.getY());
    }
}
