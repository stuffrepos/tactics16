package net.stuffrepos.tactics16.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import net.stuffrepos.tactics16.util.Nameable;
import net.stuffrepos.tactics16.util.listeners.Listener;
import net.stuffrepos.tactics16.util.listeners.ListenerManager;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.battleengine.BattleData;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.Map.Square;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Map implements Nameable, net.stuffrepos.tactics16.battleengine.Map {

    public static final Log log = LogFactory.getLog(Map.class);
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int TERRAIN_SIZE = 32;
    public static final int MIN_SIZE = 6;
    public static final int MAX_SIZE = 32;
    private String name;
    private final PersonInitialPositions personInitialPositions = new PersonInitialPositions();
    private int width;
    private int height;
    private final EnumMap<Terrain.Layer, Layer> layers;
    private CacheableMapValue<MapCoordinate, Square> squares = new CacheableMapValue<MapCoordinate, Square>() {
        @Override
        protected Square calculate(final MapCoordinate key) {
            return new Square() {
                public boolean isMovimentBlocked() {
                    Terrain terrain = getLayer(Terrain.Layer.Base).getOccupied(key);
                    Terrain obstacle = getLayer(Terrain.Layer.Obstacle).getOccupied(key);
                    return (terrain != null && terrain.getBlock()) || (obstacle != null);
                }

                public boolean isActionBlocked() {
                    Terrain object = getLayer(Terrain.Layer.Obstacle).getOccupied(key);
                    return object == null ? false : object.getBlock();
                }
            };
        }
    };
    private ListenerManager<Map> listenerManager = new ListenerManager<Map>(this);
    private String originalName;

    public Map(String name, int width, int height) {
        this.name = name;
        this.originalName = this.name;
        this.layers = new EnumMap<Terrain.Layer, Layer>(Terrain.Layer.class);
        for (Terrain.Layer layer : Terrain.Layer.values()) {
            this.layers.put(layer, new Layer(layer));
        }
        setWidthHeight(width, height);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidthHeight(int width, int height) {
        assert width >= MIN_SIZE;
        assert width <= MAX_SIZE;
        assert height >= MIN_SIZE;
        assert height <= MAX_SIZE;

        int oldWidth = this.width;
        int oldHeight = this.height;
        this.width = width;
        this.height = height;
        for (Layer layer : layers.values()) {
            layer.resize(oldWidth, oldHeight);
        }
        this.listenerManager.fireChange();
    }

    public void addHeight(int dh) {
        this.setWidthHeight(this.getWidth(), this.getHeight() + dh);
    }

    public void addWidth(int dw) {
        this.setWidthHeight(this.getWidth() + dw, this.getHeight());
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Terrain getPositionedOn(Terrain.Layer layer, int x, int y) {
        return layers.get(layer).getPositioned(x, y);
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

    public GameImage getTerrainImage(Terrain terrain, long elapsedTime) {
        return terrain.getSpriteAnimation().getImage(elapsedTime);
    }

    public void addListener(Listener<Map> listener) {
        listenerManager.addListener(listener);
    }

    public Integer getPlayerFromPosition(Coordinate position) {
        return personInitialPositions.getPlayerFromPosition(position);
    }

    public boolean inMap(Coordinate c) {
        return c.inRectangle(0, 0, getWidth(), getHeight());
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
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (layers.get(Terrain.Layer.Base).getPositioned(x, y) == null) {
                    return false;
                }
            }
        }
        return personInitialPositions.isPlayable();
    }

    public Square getSquare(MapCoordinate coordinate) {
        return squares.getValue(coordinate);
    }

    public Layer getLayer(Terrain.Layer layer) {
        return layers.get(layer);
    }

    public void setTerrain(Coordinate position, Terrain object) {
        getLayer(object.getLayer()).putObject(position, object);
    }

    public void setTerrain(int x, int y, Terrain terrain) {
        setTerrain(new Coordinate(x, y), terrain);
    }

    public interface Iterator {

        public void check(int x, int y, Terrain terrain);
    }

    // <editor-fold defaultstate="collapsed" desc="class Layer">
    public class Layer {

        private class Occupation {

            private final MapCoordinate positioned;
            private final Terrain terrain;

            private Occupation(MapCoordinate positioned, Terrain terrain) {
                this.positioned = positioned;
                this.terrain = terrain;
            }
        }
        private final Terrain.Layer layer;
        private java.util.Map<MapCoordinate, Terrain> positioned =
                new TreeMap<MapCoordinate, Terrain>(BattleData.CoordinateComparator.getInstance());
        private CacheableMapValue<MapCoordinate, Occupation> occupied = new CacheableMapValue<MapCoordinate, Occupation>() {
            @Override
            protected Occupation calculate(MapCoordinate key) {
                if (positioned.get(key) != null) {
                    return new Occupation(key, positioned.get(key));
                }

                List<Occupation> neighboors = new ArrayList<Occupation>(2);
                if (key.getX() > 0) {
                    neighboors.add(occupied.getValue(new Coordinate(key.getX() - 1, key.getY())));
                }
                if (key.getY() > 0) {
                    neighboors.add(occupied.getValue(new Coordinate(key.getX(), key.getY() - 1)));
                }
                for (Occupation neighboor : neighboors) {
                    if (neighboor != null
                            && key.getX() >= neighboor.positioned.getX()
                            && key.getX() < neighboor.positioned.getX() + neighboor.terrain.getWidth()
                            && key.getY() >= neighboor.positioned.getY()
                            && key.getY() < neighboor.positioned.getY() + neighboor.terrain.getHeight()) {
                        return neighboor;
                    }
                }

                return null;
            }
        };

        private Layer(Terrain.Layer layer) {
            this.layer = layer;
        }

        private Terrain getPositioned(int x, int y) {
            return positioned.get(new Coordinate(x, y));
        }

        private Terrain getOccupied(MapCoordinate coord) {            
            Occupation o = occupied.getValue(coord);
            return o == null ? null : o.terrain;
        }

        private void resize(int oldWidth, int oldHeight) {
            for (int x = oldWidth; x > width; x--) {
                for (int y = oldHeight; y > height; y--) {
                    positioned.remove(new Coordinate(x, y));
                }
            }
        }

        public void putObject(Coordinate position, Terrain terrain) {
            assert terrain.getLayer() != null;
            assert terrain.getLayer().equals(this.layer);
            
            Occupation occupation = occupied.getValue(position);
            if (occupation != null) {
                positioned.remove(occupation.positioned);
            }
            for (int dX = 0; dX < terrain.getWidth(); ++dX) {
                for (int dY = 0; dY < terrain.getHeight(); ++dY) {
                    MapCoordinate c = new Coordinate(
                            position.getX() + dX,
                            position.getY() + dY);

                    if (c.getX() == position.getX() && c.getY() == position.getY()) {
                        positioned.put(c, terrain);
                    } else {
                        positioned.remove(c);
                    }

                }
            }
            occupied.clear();
            squares.clear();
        }

        public void iterate(Iterator iterator) {
            for (int x = 0; x < getWidth(); ++x) {
                for (int y = 0; y < getHeight(); ++y) {
                    iterator.check(x, y, getPositioned(x, y));
                }
            }
        }

        public Iterable<java.util.Map.Entry<MapCoordinate, Terrain>> getObjects() {
            return positioned.entrySet();
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
