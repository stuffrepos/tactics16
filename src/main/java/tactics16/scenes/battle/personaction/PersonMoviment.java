package tactics16.scenes.battle.personaction;

import tactics16.scenes.battle.*;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.game.Map;
import tactics16.util.MultiLinearMoviment;
import tactics16.util.listeners.Listener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import tactics16.components.VisualMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class PersonMoviment {

    public static final double MOVIMENT_SPEED = Map.TERRAIN_SIZE * 2 / 1000d;
    private Person person;
    private Coordinate terrainTarget;
    private MultiLinearMoviment moviment;
    private Coordinate mapPosition;
    private final VisualMap visualMap;

    public PersonMoviment(Person person, VisualMap visualMap, Coordinate terrainTarget) {
        this.person = person;
        this.visualMap = visualMap;
        this.terrainTarget = terrainTarget;        
        this.person.getGameActionControl().advance(GameAction.WALKING);

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
                    PersonMoviment.this.person.setSide(1);
                } else if (speedX < 0.0d) {
                    PersonMoviment.this.person.setSide(-1);
                }
            }
        });
    }

    private List<Coordinate> getMinPathGrouped() {
        List<Coordinate> grouped = new LinkedList<Coordinate>();
        PathSearch pathSearch = new PathSearch();        
        List<Coordinate> minPath = pathSearch.getMinPath();

        if (minPath == null) {            
            System.out.flush();
            throw new RuntimeException("Min path is null");
        }

        Coordinate origin = getOriginPosition();
        Coordinate current = minPath.get(0);

        for (int i = 1; i < minPath.size(); ++i) {
            Coordinate c = minPath.get(i);

            if ((current.getX() == c.getX() && current.getX() == origin.getX()) ||
                    (current.getY() == c.getY() && current.getY() == origin.getY())) {
                current = c;
            } else {
                grouped.add(visualMap.getPersonPosition(current));
                origin = current;
                current = c;
            }
        }

        grouped.add(visualMap.getPersonPosition(minPath.get(minPath.size() - 1)));

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
            person.getGameActionControl().back();
        }
    }

    public boolean isFinalized() {
        return moviment.isFinalized();
    }

    private class PathSearch {

        private java.util.Map<Coordinate, Integer> costs;

        public PathSearch() {
            costs = new TreeMap<Coordinate, Integer>();

            Set<Coordinate> visited = new TreeSet<Coordinate>();
            int n = 0;
            Set<Coordinate> considered = new TreeSet<Coordinate>();
            considered.add(getTerrainTarget());

            while (!considered.isEmpty()) {
                Set<Coordinate> forTest = new TreeSet<Coordinate>();

                for (Coordinate c : considered) {
                    costs.put(c, n);

                    for (Coordinate neighboor : getNeighboors(c)) {
                        if (!visited.contains(neighboor)) {
                            forTest.add(neighboor);
                        }
                    }

                    visited.add(c);
                }

                considered = forTest;
                n++;
            }
        }

        private Coordinate getMinNeighboorCost(Coordinate c) {
            Coordinate minNeighboorCost = null;
            for (Coordinate neighboor : getNeighboors(c)) {
                Integer neighboorCost = costs.get(neighboor);
                if (neighboorCost != null &&
                        (minNeighboorCost == null || neighboorCost < costs.get(minNeighboorCost))) {
                    minNeighboorCost = neighboor;
                }
            }
            return minNeighboorCost;
        }

        public List<Coordinate> getMinPath() {
            Coordinate c = getOriginPosition().clone();

            if (costs.get(c) == null) {
                return null;
            } else {
                List<Coordinate> path = new LinkedList<Coordinate>();

                do {
                    c = getMinNeighboorCost(c);
                    path.add(c);
                } while (!c.equals(getTerrainTarget()));

                return path;
            }
        }

        private Iterable<Coordinate> getNeighboors(Coordinate c) {
            List<Coordinate> neighboors = new LinkedList<Coordinate>();

            neighboors.add(new Coordinate(c, -1, 0));
            neighboors.add(new Coordinate(c, 1, 0));
            neighboors.add(new Coordinate(c, 0, -1));
            neighboors.add(new Coordinate(c, 0, 1));

            List<Coordinate> inMap = new LinkedList<Coordinate>();

            for (Coordinate neighboor : neighboors) {
                if (neighboor.inRectangle(0, 0, visualMap.getMap().getWidth(), visualMap.getMap().getHeight())) {
                    if (visualMap.getMap().getTerrain(neighboor).getAllowMoviment()) {
                        inMap.add(neighboor);
                    }
                }
            }

            return inMap;
        }
    }
}
