package tactics16;

import com.golden.gamedev.Game;
import com.golden.gamedev.util.FileUtil;
import tactics16.game.Action;
import tactics16.game.ActionType;
import tactics16.game.Coordinate;
import tactics16.game.Job;
import tactics16.game.Map;
import tactics16.game.Reach;
import tactics16.game.SpriteAction;
import tactics16.game.Terrain;
import tactics16.util.DataGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JsonLoader {

    private Game game;
    private DataGroup<Job> jobs = new DataGroup<Job>();
    private DataGroup<Terrain> terrains = new DataGroup<Terrain>();
    private DataGroup<Map> maps = new DataGroup<Map>();
    private final File saveDirectory;

    public JsonLoader(Game game, File saveDirectory) {
        this.saveDirectory = saveDirectory;
        this.game = game;
    }

    public void loadDirectory(File directory) {
        List<File> jobFiles = new LinkedList<File>();
        List<File> terrainFiles = new LinkedList<File>();
        List<File> mapFiles = new LinkedList<File>();

        for (File files : directory.listFiles()) {
            if (files.isDirectory()) {
                loadDirectory(files);
            } else {                
                try {
                    if ("json".equals(FileUtil.getExtension(files))) {

                        JSONObject jsonObject = loadJsonObjectFromFile(files);

                        if ("job".equals(jsonObject.getString("objectType"))) {
                            jobFiles.add(files);
                        } else if ("terrain".equals(jsonObject.getString("objectType"))) {
                            terrainFiles.add(files);
                        } else if ("map".equals(jsonObject.getString("objectType"))) {
                            mapFiles.add(files);
                        } else {
                            throw new RuntimeException(
                                    "Unknown objectType \"" + jsonObject.getString("objectType") + "\"");
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    throw new RuntimeException("File: " + files.getAbsolutePath(), ex);
                }
            }
        }

        for (File file : jobFiles) {
            try {
                getJobs().add(loadJob(
                        loadJsonObjectFromFile(file),
                        directory));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException("File: " + file.getAbsolutePath(), ex);
            }
        }

        for (File file : terrainFiles) {
            try {
                getTerrains().add(loadTerrain(
                        loadJsonObjectFromFile(file),
                        directory));
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

    public Job loadJob(JSONObject jsonObject, File directory) throws JSONException {
        Job job = new Job(jsonObject.getString("name"));
        job.setAgility(jsonObject.getInt("agility"));
        job.setDefense(jsonObject.getInt("defense"));

        JSONObject spriteActions = jsonObject.getJSONObject("spriteActions");

        Iterator<String> spriteActionIterator = spriteActions.keys();

        while (spriteActionIterator.hasNext()) {
            String key = spriteActionIterator.next();
            Job.GameAction gameAction = Job.GameAction.valueOf(key.toUpperCase());
            job.getSpriteActions().put(
                    gameAction,
                    loadSpriteAction(spriteActions.getJSONObject(key), directory));
        }

        return job;
    }

    public SpriteAction loadSpriteAction(JSONObject jsonObject, File directory) throws JSONException {
        SpriteAction spriteAction = new SpriteAction();
        spriteAction.setChangeFrameInterval(jsonObject.getInt("changeFrameInterval"));

        JSONArray images = jsonObject.getJSONArray("images");
        for (int i = 0; i < images.length(); ++i) {
            File imageFile = new File(directory, images.getString(i));
            try {
                spriteAction.addImage(MyGame.getInstance().getImage(imageFile.getPath()));
            } catch (RuntimeException ex) {
                throw new RuntimeException(String.format(
                        "Fail to open image file \"%s\"", imageFile.getAbsolutePath()), ex);
            }
        }

        return spriteAction;
    }

    public Terrain loadTerrain(JSONObject jsonObject, File directory) throws JSONException {
        Terrain terrain = new Terrain(jsonObject.getString("name"));
        terrain.setAllowAction(jsonObject.getBoolean("allowAction"));
        terrain.setAllowMoviment(jsonObject.getBoolean("allowMoviment"));
        if (jsonObject.has("image")) {
            terrain.getImages().add(
                    game.getImage(
                    new File(directory, jsonObject.getString("image")).getAbsolutePath()));
        } else if (jsonObject.has("images")) {
            JSONArray images = jsonObject.getJSONArray("images");

            for (int i = 0; i < images.length(); ++i) {
                terrain.getImages().add(
                        game.getImage(
                        new File(directory, images.getString(i)).getAbsolutePath()));
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

    public Action loadAction(JSONObject jsonObject) throws JSONException {
        Action action = new Action(jsonObject.getString("name"));
        action.setDificulty(jsonObject.getInt("dificulty"));
        action.setPower(jsonObject.getInt("power"));
        action.setReach(loadReach(jsonObject.getJSONObject("reach")));
        action.setType(ActionType.valueOf(jsonObject.getString("type")));
        return action;
    }

    public Reach loadReach(JSONObject jsonObject) throws JSONException {
        Reach reach = new Reach();
        reach.setMin(jsonObject.getInt("min"));
        reach.setMax(jsonObject.getInt("max"));
        reach.setRay(jsonObject.getInt("ray"));
        reach.setClearTrajetory(jsonObject.getBoolean("clearTrajectory"));
        return reach;
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
