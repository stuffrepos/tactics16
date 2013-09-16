package net.stuffrepos.tactics16.datamanager;

import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.util.DataGroup;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.javabasic.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.ImageBuffer;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataManager {

    private static enum ObjectType {

        Job,
        Map,
        Terrain
    }
    private static final Log log = LogFactory.getLog(DataManager.class);
    private DataGroup<Job> jobs = new DataGroup<Job>();
    private DataGroup<Terrain> terrains = new DataGroup<Terrain>();
    private MapManager maps;
    private final File dataDirectory;
    private CacheableValue<Terrain> defaultTerrain = new CacheableValue<Terrain>() {
        @Override
        protected Terrain calculate() {
            Terrain terrain = new Terrain("Default");
            terrain.getImages().add(new GameImage(buildDefaultImage()));
            return terrain;
        }

        private ImageBuffer buildDefaultImage() {
            ImageBuffer ib = new ImageBuffer(Map.TERRAIN_SIZE, Map.TERRAIN_SIZE);
            for (int x = 0; x < ib.getWidth(); ++x) {
                for (int y = 0; y < ib.getHeight(); ++y) {
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if (x == 0 || x == ib.getWidth() - 1 || y == 0 || y == ib.getHeight() - 1) {
                        r = 255;
                        g = 255;
                        b = 255;
                    }

                    ib.setRGBA(x, y, r, g, b, 255);
                }
            }
            return ib;
        }
    };

    public DataManager(File dataDirectory) {
        this.dataDirectory = dataDirectory;
        maps = new MapManager(new File(dataDirectory, "maps"));
    }

    public void loadDirectory(File directory) {
        EnumMap<ObjectType, List<File>> objects = _loadDirectory(directory);

        for (File file : objects.get(ObjectType.Job)) {
            try {
                getJobs().add(new JobJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

        for (File file : objects.get(ObjectType.Terrain)) {
            try {
                getTerrains().add(new TerrainJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

        for (File file : objects.get(ObjectType.Map)) {
            try {
                getMaps().add(new MapJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

    }

    private EnumMap<ObjectType, List<File>> _loadDirectory(File directory) {
        EnumMap<ObjectType, List<File>> objects = new EnumMap<ObjectType, List<File>>(ObjectType.class);
        for (ObjectType objectType : ObjectType.values()) {
            objects.put(objectType, new LinkedList<File>());
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                for (EnumMap.Entry<ObjectType, List<File>> e : _loadDirectory(file).entrySet()) {
                    objects.get(e.getKey()).addAll(e.getValue());
                }
            } else {
                try {
                    if ("json".equals(FileUtil.getExtension(file))) {
                        JSONObject jsonObject = loadJsonObjectFromFile(file);

                        ObjectType foundObjectType = null;

                        for (ObjectType objectType : ObjectType.values()) {
                            if (objectType.name().toLowerCase().equals(jsonObject.getString("objectType"))) {
                                foundObjectType = objectType;
                                break;
                            }
                        }


                        if (foundObjectType == null) {
                            throw new RuntimeException(
                                    "Unknown objectType \"" + jsonObject.getString("objectType") + "\"");
                        } else {
                            objects.get(foundObjectType).add(file);
                        }

                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
                }
            }
        }
        return objects;
    }

    public JSONObject loadJsonObjectFromFile(File file) throws IOException, JSONException {
        return new JSONObject(net.stuffrepos.tactics16.util.javabasic.FileUtil.fileToString(file));
    }

    public DataGroup<Job> getJobs() {
        return jobs;
    }

    public DataGroup<Terrain> getTerrains() {
        return terrains;
    }

    public MapManager getMaps() {
        return maps;
    }
    
    public Terrain getDefaultTerrain() {
        return defaultTerrain.getValue();
    }

    public File getDataDirectory() {
        return dataDirectory;
    }
}
