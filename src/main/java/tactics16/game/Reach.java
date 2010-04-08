package tactics16.game;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Reach {

    private int min;
    private int max;
    private int ray;
    private boolean clearTrajetory;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getRay() {
        return ray;
    }

    public void setRay(int ray) {
        this.ray = ray;
    }

    public boolean isClearTrajetory() {
        return clearTrajetory;
    }

    public void setClearTrajetory(boolean clearTrajetory) {
        this.clearTrajetory = clearTrajetory;
    }
}
