package net.stuffrepos.tactics16.datamanager;

import java.awt.image.BufferedImage;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.util.DataGroup;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.javabasic.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.EmptyImageData;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataManager {

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
        maps = new MapManager(new File(dataDirectory,"maps"));
    }

    public void loadDirectory(File directory) {
        List<File> jobFiles = new LinkedList<File>();
        List<File> terrainFiles = new LinkedList<File>();
        List<File> mapFiles = new LinkedList<File>();

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                loadDirectory(file);
            } else {
                try {
                    if ("json".equals(FileUtil.getExtension(file))) {

                        JSONObject jsonObject = loadJsonObjectFromFile(file);

                        if ("job".equals(jsonObject.getString("objectType"))) {
                            jobFiles.add(file);
                        } else if ("terrain".equals(jsonObject.getString("objectType"))) {
                            terrainFiles.add(file);
                        } else if ("map".equals(jsonObject.getString("objectType"))) {
                            mapFiles.add(file);
                        } else {
                            throw new RuntimeException(
                                    "Unknown objectType \"" + jsonObject.getString("objectType") + "\"");
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
                }
            }
        }

        for (File file : jobFiles) {
            try {
                getJobs().add(new JobJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

        for (File file : terrainFiles) {
            try {
                getTerrains().add(new TerrainJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

        for (File file : mapFiles) {
            try {
                getMaps().add(new MapJsonLoader(file).loadObject());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

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
