package tactics16.datamanager;

import com.golden.gamedev.util.FileUtil;
import tactics16.game.Coordinate;
import tactics16.game.Job;
import tactics16.game.Map;
import tactics16.game.Terrain;
import tactics16.util.DataGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tactics16.MyGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class DataManager {

    private MyGame game;
    private DataGroup<Job> jobs = new DataGroup<Job>();
    private DataGroup<Terrain> terrains = new DataGroup<Terrain>();
    private DataGroup<Map> maps = new DataGroup<Map>();
    private final File saveDirectory;

    public DataManager(MyGame game, File saveDirectory) {
        this.saveDirectory = saveDirectory;
        this.game = game;
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
                getMaps().add(jsonToMap(
                        loadJsonObjectFromFile(file),
                        directory));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

    }

    public JSONObject loadJsonObjectFromFile(File file) throws IOException, JSONException {
        return new JSONObject(tactics16.util.FileUtil.fileToString(file));
    }

    public Terrain loadTerrain(JSONObject jsonObject, File directory) throws JSONException {
        Terrain terrain = new Terrain(jsonObject.getString("name"));
        terrain.setAllowAction(jsonObject.getBoolean("allowAction"));
        terrain.setAllowMoviment(jsonObject.getBoolean("allowMoviment"));
        if (jsonObject.has("image")) {
            terrain.getImages().add(
                    game.getImage(
                    new File(directory, jsonObject.getString("image"))));
        } else if (jsonObject.has("images")) {
            JSONArray images = jsonObject.getJSONArray("images");

            for (int i = 0; i < images.length(); ++i) {
                terrain.getImages().add(
                        game.getImage(
                        new File(directory, images.getString(i))));
            }
        } else {
            throw new RuntimeException("Terrain has no images");
        }
        return terrain;
    }

    public Map jsonToMap(JSONObject jsonObject, File directory) throws JSONException {
        final Map map = new Map(
                jsonObject.getString("name"),
                jsonObject.getInt("width"),
                jsonObject.getInt("height"));

        JSONArray terrains = jsonObject.getJSONArray("terrains");
        for (int x = 0; x < terrains.length(); ++x) {
            for (int y = 0; y < terrains.getJSONArray(x).length(); ++y) {
                map.setTerrain(
                        x,
                        y,
                        getTerrains().getRequired(terrains.getJSONArray(x).getString(y)));
            }
        }

        JSONArray players = jsonObject.getJSONArray("personInitialPositions");

        for (int player = 0; player < players.length(); ++player) {
            for (int c = 0; c < players.getJSONArray(player).length(); ++c) {
                map.setPersonInitialPosition(
                        player,
                        new Coordinate(players.getJSONArray(player).getJSONObject(c)));
            }
        }

        return map;
    }

    public JSONObject mapToJson(Map map) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("objectType", "map");
        jsonObject.put("name", map.getName());
        jsonObject.put("width", map.getWidth());
        jsonObject.put("height", map.getHeight());

        JSONArray terrains = new JSONArray();

        for (int x = 0; x < map.getWidth(); ++x) {
            terrains.put(x, new JSONArray());

            for (int y = 0; y < map.getHeight(); ++y) {
                terrains.getJSONArray(x).put(y, map.getTerrain(x, y).getName());
            }
        }

        jsonObject.put("terrains", terrains);

        JSONArray players = new JSONArray();

        for (int player = 0; player < map.getPlayerCount(); ++player) {
            players.put(player, new JSONArray());

            for (Coordinate position : map.getPlayerInitialPosition(player)) {
                players.getJSONArray(player).put(position.toJson());
            }
        }

        jsonObject.put("personInitialPositions", players);

        return jsonObject;
    }

    public void saveMap(Map map) throws IOException {
        File mapsDirectory = new File(getSaveDirectory(), "maps");

        mapsDirectory.mkdirs();

        File file = new File(mapsDirectory, map.getName() + ".json");

        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(mapToJson(map).toString(4).getBytes());
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } finally {
            fos.close();
        }
    }

    

    public DataGroup<Job> getJobs() {
        return jobs;
    }

    public DataGroup<Terrain> getTerrains() {
        return terrains;
    }

    public DataGroup<Map> getMaps() {
        return maps;
    }

    public File getSaveDirectory() {
        return saveDirectory;
    }

    public Terrain getDefaultTerrain() {
        return terrains.getRequired("Grass");
    }
}
