package tactics16.game;

import tactics16.MyGame;
import tactics16.util.Nameable;
import tactics16.util.listeners.Listener;
import tactics16.util.listeners.ListenerManager;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import tactics16.animation.GameImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Map implements Nameable {

    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int TERRAIN_SIZE = 32;
    public static final int MIN_SIZE = 6;
    public static final int MAX_SIZE = 32;
    private String name;
    private long elapsedTime;
    private PersonInitialPositions personInitialPositions = new PersonInitialPositions();
    private Terrains terrains = new Terrains(0, 0);
    private ListenerManager<Map> listenerManager = new ListenerManager<Map>(this);
    private String originalName;

    public Map(String name, int width, int height) {
        this.name = name;
        this.originalName = this.name;
        setWidthHeight(width, height);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidthHeight(int w, int h) {
        if ((h >= MIN_SIZE && h <= MAX_SIZE) && (w >= MIN_SIZE && w <= MAX_SIZE) && (w != this.getWidth() || h != this.getHeight())) {
            final Terrains oldTerrains = this.terrains;
            this.terrains = new Terrains(w, h);
            if (oldTerrains != null) {
                iterate(new Iterator() {

                    public void check(int x, int y, Terrain terrain) {
                        if (oldTerrains.inPoint(x, y)) {
                            terrains.setTerrrain(x, y, oldTerrains.get(x, y));

                        } else {
                            terrains.setTerrrain(x, y, MyGame.getInstance().getLoader().getDefaultTerrain());
                        }
                    }
                });
            }
            this.listenerManager.fireChange();
        }
    }

    public void addHeight(int dh) {
        this.setWidthHeight(this.getWidth(), this.getHeight() + dh);
    }

    public void addWidth(int dw) {
        this.setWidthHeight(this.getWidth() + dw, this.getHeight());
    }

    public void setTerrain(Coordinate position, Terrain terrain) {
        setTerrain(position.getX(), position.getY(), terrain);
    }

    public void iterate(Iterator iterator) {
        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y < getHeight(); ++y) {
                iterator.check(x, y, terrains.get(x, y));
            }
        }
    }

    public Integer getHeight() {
        return terrains.getHeight();
    }

    public Integer getWidth() {
        return terrains.getWidth();
    }

    public void setTerrain(int x, int y, Terrain terrain) {
        terrains.setTerrrain(x, y, terrain);
    }

    public Terrain getTerrain(int x, int y) {
        return terrains.get(x, y);
    }

    public void setPersonInitialPosition(Integer player, Coordinate position) {
        assert position != null;
        personInitialPositions.setPosition(player, position);
    }

    public int getPlayerCount() {
        return personInitialPositions.getPlayerCount();
    }

    public Set<Coordinate> getPlayerInitialPosition(int player) {
        return this.personInitialPositions.getPlayerInitialPositions(player);
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public GameImage getTerrainImage(Terrain terrain) {
        return terrain.getImages().get((int) ((elapsedTime / 300L) % terrain.getImages().size()));
    }

    public void addListener(Listener<Map> listener) {
        listenerManager.addListener(listener);
    }

    public Integer getPlayerFromPosition(Coordinate position) {
        return personInitialPositions.getPlayerFromPosition(position);
    }

    public java.util.Map<Coordinate, Integer> calculateActionDistances(Coordinate target) {
        java.util.Map<Coordinate, Integer> distances = new TreeMap<Coordinate, Integer>();
        Set<Coordinate> visited = new TreeSet<Coordinate>();
        Set<Coordinate> current = new TreeSet<Coordinate>();

        current.add(target);
        visited.add(target);
        int n = 0;

        while (!current.isEmpty()) {
            Set<Coordinate> forTest = new TreeSet<Coordinate>();

            for (Coordinate c : current) {
                if (getTerrain(c).getAllowAction()) {
                    distances.put(c, n);
                }

                for (Coordinate next : getActionNeighboors(c)) {
                    if (!visited.contains(next)) {
                        visited.add(next);
                        forTest.add(next);
                    }
                }
            }

            n++;
            current = forTest;
        }

        return distances;
    }

    public Iterable<Coordinate> getActionNeighboors(Coordinate c) {
        Set<Coordinate> neighboors = new TreeSet<Coordinate>();

        for (Coordinate neighboor : new Coordinate[]{
                    new Coordinate(c, 0, -1),
                    new Coordinate(c, 0, 1),
                    new Coordinate(c, -1, 0),
                    new Coordinate(c, 1, 0)
                }) {
            if (inMap(neighboor)) {
                neighboors.add(neighboor);
            }
        }

        return neighboors;
    }

    public boolean inMap(Coordinate c) {
        return c.inRectangle(0, 0, getWidth(), getHeight());
    }

    public Terrain getTerrain(Coordinate position) {
        return getTerrain(position.getX(), position.getY());
    }

    public PersonInitialPositions getPersonInitialPositions() {
        return personInitialPositions;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public boolean isPlayable() {
        return personInitialPositions.isPlayable() &&
                terrains.isPlayable();        
    }

    public interface Iterator {

        public void check(int x, int y, Terrain terrain);
    }

    // <editor-fold defaultstate="collapsed" desc="class Terrains">
    private class Terrains {

        private int width;
        private int height;
        private Terrain[][] terrains;

        public Terrains(int width, int height) {
            setWidthHeight(width, height);
        }

        public void setWidthHeight(int width, int heigth) {
            assert width >= 0;
            assert heigth >= 0;
            this.width = width;
            this.height = heigth;
            this.terrains = new Terrain[width][heigth];
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Terrain get(int x, int y) {
            return terrains[x][y];
        }

        public void setTerrrain(int x, int y, Terrain terrain) {
            terrains[x][y] = terrain;
        }

        private boolean inPoint(int x, int y) {
            return (x >= 0 && x < width) && (y >= 0 && y < height);
        }

        public boolean isPlayable() {
            for(int x=0; x< width; ++x) {
                for(int y=0; y < height; ++y) {
                    if (terrains[x][y]==null) {
                        return false;
                    }
                }
            }
            return true;
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="class PersonInitialPositions">
    public static class PersonInitialPositions {

        private java.util.Map<Coordinate, Integer> positions = new TreeMap<Coordinate, Integer>();
        private java.util.Map<Integer, Set<Coordinate>> players = new TreeMap<Integer, Set<Coordinate>>();

        public Iterable<Coordinate> getAllPositions() {
            return positions.keySet();
        }

        public Integer getPlayerFromPosition(Coordinate position) {
            return positions.get(position);
        }

        public int getPlayerCount() {
            return players.size();
        }

        public void setPosition(Integer player, Coordinate position) {
            removePosition(position);
            if (player != null) {
                addPosition(player, position);
            }
        }

        public void addPosition(int player, Coordinate position) {
            Coordinate positionClone = position.clone();
            getPlayerInitialPositions(player).add(positionClone);
            positions.put(positionClone, player);
        }

        public void removePosition(Coordinate position) {
            if (positions.get(position) != null) {
                players.get(positions.get(position)).remove(position);
                if (players.get(positions.get(position)).isEmpty()) {
                    players.remove(positions.get(position));    
                }
            }
            positions.remove(position);
        }

        public Set<Coordinate> getPlayerInitialPositions(int player) {
            Set<Coordinate> playerPositions = players.get(player);

            if (playerPositions == null) {
                playerPositions = new TreeSet<Coordinate>();
                players.put(player, playerPositions);
            }

            return playerPositions;
        }

        private boolean isPlayable() {
            if (players.size() >= 2) {
                for(Set<Coordinate> playerPositions: players.values()) {
                    if (playerPositions.size() < 1) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
    }// </editor-fold>
}
