package tactics16.game;

import tactics16.MyGame;
import tactics16.util.Nameable;
import tactics16.util.Thumbnail;
import tactics16.util.listeners.Listener;
import tactics16.util.listeners.ListenerManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Map implements Nameable {
    
    public Terrain getTerrain(Coordinate position) {
        return getTerrain(position.getX(), position.getY());
    }

    public PersonInitialPositions getPersonInitialPositions() {
        return personInitialPositions;
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
    }// </editor-fold>
    public static final int TERRAIN_SIZE = 32;
    public static final int MIN_SIZE = 6;
    public static final int MAX_SIZE = 32;

    public interface Iterator {

        public void check(int x, int y, Terrain terrain);
    }
    private final String name;
    private BufferedImage thumb;
    private long elapsedTime;
    private PersonInitialPositions personInitialPositions = new PersonInitialPositions();
    private Terrains terrains = new Terrains(0, 0);
    private ListenerManager<Map> listenerManager = new ListenerManager<Map>(this);

    public Map(String name, int width, int height) {
        this.name = name;
        setWidthHeight(width, height);
    }

    public String getName() {
        return name;
    }

    public void setWidthHeight(int w, int h) {
        if ((h >= MIN_SIZE && h <= MAX_SIZE) && (w >= MIN_SIZE && w <= MAX_SIZE) && (w != this.getWidth() || h != this.getHeight())) {
            final Terrains oldTerrains = this.terrains;
            this.terrains = new Terrains(w, h);
            thumb = null;
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

    public BufferedImage getTerrainImage(Terrain terrain) {
        return terrain.getImages().get((int) ((elapsedTime / 300L) % terrain.getImages().size()));
    }

    public void addListener(Listener<Map> listener) {
        listenerManager.addListener(listener);
    }

    public Integer getPlayerFromPosition(Coordinate position) {
        return personInitialPositions.getPlayerFromPosition(position);
    }
}
