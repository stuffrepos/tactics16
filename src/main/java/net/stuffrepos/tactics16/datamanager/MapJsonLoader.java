package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;

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

        JSONArray terrains = getRootJson().getJSONArray("terrains");
        for (int x = 0; x < terrains.length(); ++x) {
            for (int y = 0; y < terrains.getJSONArray(x).length(); ++y) {
                map.setTerrain(
                        x,
                        y,
                        MyGame.getInstance().getLoader().getTerrains().getRequired(
                        terrains.getJSONArray(x).getString(y)));
            }
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
}
