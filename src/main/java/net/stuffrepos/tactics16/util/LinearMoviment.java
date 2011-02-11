package net.stuffrepos.tactics16.util;

import net.stuffrepos.tactics16.game.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class LinearMoviment {

    private Coordinate objectPosition;
    private Coordinate target;
    private Coordinate speed;
    private boolean finalized;

    public LinearMoviment(Coordinate objectPosition, Coordinate target, double speed) {
        this.objectPosition = objectPosition;
        this.target = target;
        double dx = target.getDoubleX() - objectPosition.getDoubleX();
        double dy = target.getDoubleY() - objectPosition.getDoubleY();
        double d = Math.sqrt(dx * dx + dy * dy);
        this.speed = new Coordinate((dx / d) * speed, (dy / d) * speed);
    }

    public void update(long elapsedTime) {
        if (!finalized) {
            finalized = calculateFinalized();

            if (!finalized) {
                this.objectPosition.addXY(
                        elapsedTime * speed.getDoubleX(),
                        elapsedTime * speed.getDoubleY());
            }

            finalized = calculateFinalized();

            if (finalized) {
                this.objectPosition.set(target);
            }
        }
    }

    public boolean isFinalized() {
        return finalized;
    }

    public boolean calculateFinalized() {
        boolean finalizedX;

        if (speed.getDoubleX() >= 0.0d) {
            finalizedX = objectPosition.getDoubleX() >= target.getDoubleX();
        } else {
            finalizedX = objectPosition.getDoubleX() < target.getDoubleX();
        }

        boolean finalizedY;

        if (speed.getDoubleY() >= 0.0d) {
            finalizedY = objectPosition.getDoubleY() >= target.getDoubleY();
        } else {
            finalizedY = objectPosition.getDoubleY() < target.getDoubleY();
        }

        return finalizedX && finalizedY;
    }

    public Coordinate getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return String.format("%s => %s * %s", this.objectPosition, this.target, this.speed);
    }
}
