package net.stuffrepos.tactics16.scenes.battle.personaction;

import java.util.Iterator;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.MultiLinearMoviment;
import net.stuffrepos.tactics16.util.listeners.Listener;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.VisualBattleMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonMovimentAnimation {

    public static final double MOVIMENT_SPEED = Map.TERRAIN_SIZE * 4 / 1000d;
    private Person person;
    private Coordinate terrainTarget;
    private MultiLinearMoviment moviment;
    private final VisualBattleMap visualBattleMap;
    private final MapCoordinate originalPosition;

    public PersonMovimentAnimation(Person person, VisualBattleMap visualBattleMap, MapCoordinate originalPosition, MapCoordinate terrainTarget) {
        this.person = person;
        this.visualBattleMap = visualBattleMap;
        this.originalPosition = originalPosition;
        this.terrainTarget = Coordinate.fromMapCoordinate(terrainTarget);
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
        Coordinate origin = Coordinate.fromMapCoordinate(originalPosition);
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
                    grouped.add(visualBattleMap.getVisualMap().getPersonPosition(current));
                    origin = current;
                    current = next;
                }
            }

            if (!minPathIterator.hasNext()) {
                grouped.add(visualBattleMap.getVisualMap().getPersonPosition(next));
            }
        }



        return grouped;
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
            costs = visualBattleMap.getBattleGame().calculateMovimentDistances(terrainTarget,person.getPlayer());
        }

        private Coordinate getMinNeighboorCost(Coordinate c) {
            Coordinate minNeighboorCost = null;
            for (Coordinate neighboor : visualBattleMap.getBattleGame().getMovimentNeighboors(c,person.getPlayer())) {
                Integer neighboorCost = costs.get(neighboor);
                if (neighboorCost != null &&
                        (minNeighboorCost == null || neighboorCost < costs.get(minNeighboorCost))) {
                    minNeighboorCost = neighboor;
                }
            }
            return minNeighboorCost;
        }

        public List<Coordinate> getMinPath() {
            if (costs.get(originalPosition) == null) {
                return null;
            } else {
                Coordinate c = Coordinate.fromMapCoordinate(originalPosition);

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
