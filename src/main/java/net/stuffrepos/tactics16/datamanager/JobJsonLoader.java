package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.ImageGroup;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import net.stuffrepos.tactics16.game.Action;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.game.Reach;
import net.stuffrepos.tactics16.scenes.battle.Player;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobJsonLoader extends AbstractJsonFileLoader<Job> {

    public JobJsonLoader(File file) throws IOException, JSONException {
        super(file);
    }

    @Override
    protected Job fromJson() throws JSONException {
        final Job job = new Job(getRootJson().getString("name"));
        job.setEvasiveness(getRootJson().getInt("evasiveness"));
        job.setDefense(getRootJson().getInt("defense"));

        job.getSpriteActionGroup().addAnimationGroup(
                new AnimationGroupJsonLoader(getRootJson().getJSONObject("animationGroup"), getFile()).loadObject());

        JSONArray actions = getRootJson().getJSONArray("actions");

        for (int i = 0; i < actions.length(); ++i) {
            job.getActions().add(loadAction(actions.getJSONObject(i)));
        }

        loadPlayersColorsMapping(getRootJson().getJSONObject("playerColorsMapping"), job.getSpriteActionGroup());


        return job;
    }

    private Action loadAction(JSONObject jsonObject) throws JSONException {
        Action action = new Action(jsonObject.getString("name"));
        action.setAgility(jsonObject.getInt("agility"));
        action.setPower(jsonObject.getInt("power"));
        action.setReach(loadReach(jsonObject.getJSONObject("reach")));
        return action;
    }

    private Reach loadReach(JSONObject jsonObject) throws JSONException {
        Reach reach = new Reach();
        if (jsonObject.has("distance")) {
            reach.getDistance().setMinMax(jsonObject.getInt("distance"));
        }
        if (jsonObject.has("distanceMin")) {
            reach.getDistance().setMin(jsonObject.getInt("distanceMin"));
        }
        if (jsonObject.has("distanceMax")) {
            reach.getDistance().setMax(jsonObject.getInt("distanceMax"));
        }
        if (jsonObject.has("ray")) {
            reach.getRay().setMinMax(jsonObject.getInt("ray"));
        }
        if (jsonObject.has("rayMin")) {
            reach.getRay().setMin(jsonObject.getInt("rayMin"));
        }
        if (jsonObject.has("rayMax")) {
            reach.getRay().setMax(jsonObject.getInt("rayMax"));
        }
        if (jsonObject.has("clearTrajectory")) {
            reach.setClearTrajetory(jsonObject.getBoolean("clearTrajectory"));
        }

        return reach;
    }

    private static void loadPlayersColorsMapping(JSONObject jsonObject, final JobSpriteActionGroup jobSpriteActionGroup) throws JSONException {


        new JsonKeyIterator(jsonObject.getJSONObject("colors")) {

            @Override
            public void iterate(JSONObject jsonObject, String key) throws JSONException {
                jobSpriteActionGroup.setMapping(
                        Integer.parseInt(key, 16),
                        Player.Color.valueOf(jsonObject.getString(key)));
            }
        };

        new JsonKeyIterator(jsonObject.optJSONObject("limits")) {

            @Override
            public void iterate(JSONObject jsonObject, String key) throws JSONException {
                Player.Color playerColor = Player.Color.valueOf(key.toUpperCase());

                new JsonKeyIterator(jsonObject.getJSONObject(key)) {

                    @Override
                    public void iterate(JSONObject jsonObject, String key) throws JSONException {
                        if ("min".equals(key)) {
                            jobSpriteActionGroup.setColorMappingMin(Float.parseFloat(jsonObject.getString(key)));
                        } else if ("max".equals(key)) {
                            jobSpriteActionGroup.setColorMappingMax(Float.parseFloat(jsonObject.getString(key)));
                        } else {
                            throw new RuntimeException("Illegal option: \"" + key + "\"");
                        }
                    }
                };
            }
        };
    }
}
