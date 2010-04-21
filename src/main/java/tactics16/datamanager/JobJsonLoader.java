package tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
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
public class JobJsonLoader extends AbstractObjectJsonLoader<Job> {

    public JobJsonLoader(File file) throws IOException, JSONException {
        super(file);
    }
    
    @Override
    protected Job internalLoadObject() throws JSONException {
        Job job = new Job(getRootJson().getString("name"));
        job.setEvasiveness(getRootJson().getInt("evasiveness"));
        job.setDefense(getRootJson().getInt("defense"));

        JSONObject spriteActions = getRootJson().getJSONObject("spriteActions");

        Iterator<String> spriteActionIterator = spriteActions.keys();

        while (spriteActionIterator.hasNext()) {
            String key = spriteActionIterator.next();
            Job.GameAction gameAction = Job.GameAction.valueOf(key.toUpperCase());
            job.getSpriteActionGroup().addAction(
                    gameAction,
                    loadSpriteAction(
                    spriteActions.getJSONObject(key),
                    job.getSpriteActionGroup().getImages(),
                    getDirectory()));
        }

        JSONArray actions = getRootJson().getJSONArray("actions");

        for (int i = 0; i < actions.length(); ++i) {
            job.getActions().add(loadAction(actions.getJSONObject(i)));
        }

        JSONObject playerColorsMapping = getRootJson().getJSONObject("playerColorsMapping");

        Iterator<String> playerColorMapping = playerColorsMapping.keys();

        while (playerColorMapping.hasNext()) {
            String key = playerColorMapping.next();
            job.getSpriteActionGroup().setMapping(
                    Integer.parseInt(key, 16),
                    Player.Color.valueOf(playerColorsMapping.getString(key)));
        }

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

    public SpriteAnimation loadSpriteAction(JSONObject jsonObject, ImageGroup imageGroup, File directory) throws JSONException {
        SpriteAnimation spriteAction = new SpriteAnimation();
        spriteAction.setChangeFrameInterval(jsonObject.getInt("changeFrameInterval"));

        JSONArray images = jsonObject.getJSONArray("images");
        for (int i = 0; i < images.length(); ++i) {
            File imageFile = new File(directory, images.getString(i));
            if (!imageGroup.hasImage(imageFile.getName())) {
                imageGroup.addImage(imageFile.getName(), MyGame.getInstance().getImage(imageFile));
            }

            try {
                spriteAction.addImage(MyGame.getInstance().getImage(imageFile));
            } catch (RuntimeException ex) {
                throw new RuntimeException(String.format(
                        "Fail to open image file \"%s\"", imageFile.getAbsolutePath()), ex);
            }
        }

        return spriteAction;
    }
}
