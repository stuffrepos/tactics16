package net.stuffrepos.tactics16.util;

import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MultiLinearMoviment {

    private final Coordinate objectPosition;
    private final double speed;
    private final Queue<Coordinate> targets;
    private LinearMoviment currentMoviment;
    private Coordinate absoluteSpeed;
    private ListenerManager<MultiLinearMoviment> listenerManager = new ListenerManager<MultiLinearMoviment>(this);

    public MultiLinearMoviment(Coordinate objectPosition, List<Coordinate> targets, double speed) {
        this.objectPosition = objectPosition;
        this.targets = new LinkedList<Coordinate>(targets);
        this.speed = speed;

        if (targets.isEmpty()) {
            this.absoluteSpeed = new Coordinate(0, 0);
        } else {
            double dx = targets.get(targets.size() - 1).getDoubleX() - objectPosition.getDoubleX();
            double dy = targets.get(targets.size() - 1).getDoubleY() - objectPosition.getDoubleY();
            double d = Math.sqrt(dx * dx + dy * dy);
            this.absoluteSpeed = new Coordinate((dx / d) * speed, (dy / d) * speed);
        }


        nextMoviment();
    }

    private void nextMoviment() {
        this.currentMoviment = this.targets.isEmpty()
                ? null
                : new LinearMoviment(objectPosition, this.targets.poll(), speed);
        listenerManager.fireChange();
    }

    public void addListener(Listener<MultiLinearMoviment> listener) {
        listenerManager.addListener(listener);
    }

    private LinearMoviment getCurrentMoviment() {
        return this.currentMoviment;
    }

    public void update(long elapsedTime) {
        if (!isFinalized()) {
            this.getCurrentMoviment().update(elapsedTime);
            if (this.getCurrentMoviment().isFinalized()) {
                nextMoviment();
            }
        }
    }

    public boolean isFinalized() {
        return this.getCurrentMoviment() == null;
    }

    public double getSpeed() {
        return speed;
    }

    public Coordinate getCurrentSpeed() {
        if (isFinalized()) {
            throw new RuntimeException("No current speed with moviments is finalized");
        } else {
            return this.currentMoviment.getSpeed();
        }
    }

    public Coordinate getAbsoluteSpeed() {
        return absoluteSpeed;
    }
}
