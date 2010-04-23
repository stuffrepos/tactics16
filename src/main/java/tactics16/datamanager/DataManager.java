package tactics16.datamanager;

import com.golden.gamedev.util.FileUtil;
import tactics16.game.Job;
import tactics16.game.Terrain;
import tactics16.util.DataGroup;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataManager {

    private DataGroup<Job> jobs = new DataGroup<Job>();
    private DataGroup<Terrain> terrains = new DataGroup<Terrain>();
    private MapManager maps;
    private final File dataDirectory;

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
        return new JSONObject(tactics16.util.javabasic.FileUtil.fileToString(file));
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
        return terrains.getRequired("Grass");
    }

    public File getDataDirectory() {
        return dataDirectory;
    }
}
