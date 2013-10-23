package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.Terrain;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapJsonLoader extends AbstractJsonFileLoader<Map> {

    public MapJsonLoader(File file) throws IOException, JSONException {
        super(file);
    }

    @Override
    protected Map fromJson() throws JSONException {
        final Map map = new Map(
                getRootJson().getString("name"),
                getRootJson().getInt("width"),
                getRootJson().getInt("height"));

        JSONObject layers = getRootJson().getJSONObject("layers");
        for (Terrain.Layer layer : Terrain.Layer.values()) {
            jsonToLayer(map.getLayer(layer), layers.getJSONArray(layer.name()));
        }

        JSONArray players = getRootJson().getJSONArray("personInitialPositions");

        for (int player = 0; player < players.length(); ++player) {
            for (int c = 0; c < players.getJSONArray(player).length(); ++c) {
                map.setPersonInitialPosition(
                        player,
                        new Coordinate(players.getJSONArray(player).getJSONObject(c)));
            }
        }

        return map;
    }

    public static JSONObject mapToJson(Map map) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("objectType", "map");
        jsonObject.put("name", map.getName());
        jsonObject.put("width", map.getWidth());
        jsonObject.put("height", map.getHeight());

        JSONObject layers = new JSONObject();
        jsonObject.put("layers", layers);

        for (Terrain.Layer layer : Terrain.Layer.values()) {
            layers.put(layer.name(), layerToJson(map.getLayer(layer)));
        }

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

    public static JSONArray layerToJson(Map.Layer layer) throws JSONException {
        JSONArray objects = new JSONArray();
        for (java.util.Map.Entry<MapCoordinate, Terrain> e : layer.getObjects()) {
            JSONObject obj = new JSONObject();
            obj.put("x", e.getKey().getX());
            obj.put("y", e.getKey().getY());
            obj.put("terrain", e.getValue().getName());
            objects.put(obj);
        }
        return objects;
    }

    private void jsonToLayer(Map.Layer layer, JSONArray objects) throws JSONException {
        for (int i = 0; i < objects.length(); i++) {
            JSONObject obj = objects.getJSONObject(i);
            layer.putObject(
                    new Coordinate(obj),
                    MyGame.getInstance().getLoader().getTerrains().get(obj.getString("terrain")));
        }
    }
}
