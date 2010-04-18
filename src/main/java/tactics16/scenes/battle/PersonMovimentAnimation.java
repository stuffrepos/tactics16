package tactics16.scenes.battle;

import java.util.Iterator;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.game.Map;
import tactics16.util.MultiLinearMoviment;
import tactics16.util.listeners.Listener;
import java.util.LinkedList;
import java.util.List;
import tactics16.components.VisualMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonMovimentAnimation {

    public static final double MOVIMENT_SPEED = Map.TERRAIN_SIZE * 4 / 1000d;
    private Person person;
    private Coordinate terrainTarget;
    private MultiLinearMoviment moviment;
    private final VisualMap visualMap;

    public PersonMovimentAnimation(Person person, VisualMap visualMap, Coordinate terrainTarget) {
        this.person = person;
        this.visualMap = visualMap;
        this.terrainTarget = terrainTarget;
        this.person.setCurrentGameAction(GameAction.WALKING);
        this.moviment = new MultiLinearMoviment(
                this.person.getPosition(),
                getMinPathGrouped(),
                MOVIMENT_SPEED);
        this.moviment.addListener(new Listener<MultiLinearMoviment>() {

            public void onChange(MultiLinearMoviment source) {
                double speedX = 0.0d;
                if (!source.isFinalized()) {
                    speedX = source.getCurrentSpeed().getDoubleX();
                }
                if (speedX > 0.0d) {
                    PersonMovimentAnimation.this.person.setSide(1);
                } else if (speedX < 0.0d) {
                    PersonMovimentAnimation.this.person.setSide(-1);
                }
            }
        });
    }

    private List<Coordinate> getMinPathGrouped() {
        List<Coordinate> minPath = new PathSearch().getMinPath();        

        if (minPath == null) {
            System.out.flush();
            throw new RuntimeException("Min path is null");
        }

        List<Coordinate> grouped = new LinkedList<Coordinate>();
        Coordinate origin = getOriginPosition();
        Iterator<Coordinate> minPathIterator = minPath.iterator();
        Coordinate current = null;

        while (minPathIterator.hasNext()) {
            Coordinate next = minPathIterator.next();
            
            if (current == null) {
                current = next;
            } else {
                if ((current.getX() == next.getX() && current.getX() == origin.getX()) ||
                        (current.getY() == next.getY() && current.getY() == origin.getY())) {
                    current = next;
                } else {
                    grouped.add(visualMap.getPersonPosition(current));
                    origin = current;
                    current = next;
                }
            }

            if (!minPathIterator.hasNext()) {
                grouped.add(visualMap.getPersonPosition(next));
            }
        }



        return grouped;
    }

    private Coordinate getOriginPosition() {
        return person.getMapPosition();
    }

    public Coordinate getTerrainTarget() {
        return terrainTarget;
    }

    public void update(long elapsedTime) {
        moviment.update(elapsedTime);

        if (isFinalized()) {
            person.setCurrentGameAction(GameAction.STOPPED);
        }
    }

    public boolean isFinalized() {
        return moviment.isFinalized();
    }

    private class PathSearch {

        private java.util.Map<Coordinate, Integer> costs;

        public PathSearch() {
            costs = visualMap.getMap().calculateMovimentDistances(terrainTarget);
        }

        private Coordinate getMinNeighboorCost(Coordinate c) {
            Coordinate minNeighboorCost = null;
            for (Coordinate neighboor : visualMap.getMap().getMovimentNeighboors(c)) {
                Integer neighboorCost = costs.get(neighboor);
                if (neighboorCost != null &&
                        (minNeighboorCost == null || neighboorCost < costs.get(minNeighboorCost))) {
                    minNeighboorCost = neighboor;
                }
            }
            return minNeighboorCost;
        }

        public List<Coordinate> getMinPath() {
            if (costs.get(getOriginPosition()) == null) {
                return null;
            } else {
                Coordinate c = getOriginPosition();

                List<Coordinate> path = new LinkedList<Coordinate>();

                while (!c.equals(getTerrainTarget())) {
                    c = getMinNeighboorCost(c);
                    path.add(c);
                }

                return path;
            }
        }
    }
}
