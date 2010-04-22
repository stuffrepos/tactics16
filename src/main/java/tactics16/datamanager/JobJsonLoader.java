package tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tactics16.MyGame;
import tactics16.animation.ImageGroup;
import tactics16.animation.SpriteAnimation;
import tactics16.game.Action;
import tactics16.game.Job;
import tactics16.game.Reach;
import tactics16.scenes.battle.Player;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobJsonLoader extends AbstractJsonFileLoader<Job> {

    public JobJsonLoader(File file) throws IOException, JSONException {
        super(file);
    }

    @Override
    protected Job internalLoadObject() throws JSONException {
        final Job job = new Job(getRootJson().getString("name"));
        job.setEvasiveness(getRootJson().getInt("evasiveness"));
        job.setDefense(getRootJson().getInt("defense"));

        job.getSpriteActionGroup().addAnimationGroup(
                new AnimationGroupJsonLoader(getRootJson().getJSONObject("animationGroup"), getFile()).loadObject());

        JSONArray actions = getRootJson().getJSONArray("actions");

        for (int i = 0; i < actions.length(); ++i) {
            job.getActions().add(loadAction(actions.getJSONObject(i)));
        }

        new JsonKeyIterator(getRootJson().getJSONObject("playerColorsMapping")) {

            @Override
            public void iterate(JSONObject jsonObject, String key) throws JSONException {
                job.getSpriteActionGroup().setMapping(
                        Integer.parseInt(key, 16),
                        Player.Color.valueOf(jsonObject.getString(key)));
            }
        };

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
        reach.setMin(jsonObject.getInt("min"));
        reach.setMax(jsonObject.getInt("max"));
        reach.setRay(jsonObject.getInt("ray"));
        reach.setClearTrajetory(jsonObject.getBoolean("clearTrajectory"));
        return reach;
    }

}
