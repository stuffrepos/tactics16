package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.Reach;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import net.stuffrepos.tactics16.util.math.Interval;

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
        final JobImpl job = new JobImpl(getRootJson().getString("name"));
        job.setEvasiveness(getRootJson().getInt("evasiveness"));
        job.setResistence(getRootJson().getInt("resistence"));
        job.setMoviment(getRootJson().getInt("moviment"));
        job.setMaximumSpecialPoints(getRootJson().getInt("maximumSpecialPoints"));
        job.setMaximumSpecialPoints(getRootJson().getInt("maximumHealthPoints"));       
        // Only one decimal points
        job.setSpeed((float) Math.floor(getRootJson().getDouble("speed") * 10.0) / 10.0f);

        job.getSpriteActionGroup().addAnimationGroup(
                new AnimationGroupJsonLoader(getRootJson().getJSONObject("animationGroup"), getFile()).loadObject());

        JSONArray actions = getRootJson().getJSONArray("actions");

        for (int i = 0; i < actions.length(); ++i) {
            job.getActions().add(loadAction(actions.getJSONObject(i), i));
        }

        loadPlayersColorsMapping(getRootJson().getJSONObject("playerColorsMapping"), job.getSpriteActionGroup());


        return job;
    }

    private Action loadAction(JSONObject jsonObject, int jobOrder) throws JSONException {
        ActionImpl action = new ActionImpl(jsonObject.getString("name"), jobOrder);
        action.setAccuracy(jsonObject.getInt("accuracy"));
        action.setPower(jsonObject.getInt("power"));
        action.setReach(loadReach(jsonObject.getJSONObject("reach")));

        if (jsonObject.has("healthPointsCost")) {
            action.setHealthPointsCost(jsonObject.getInt("healthPointsCost"));
        }

        if (jsonObject.has("specialPointsCost")) {
            action.setSpecialPointsCost(jsonObject.getInt("specialPointsCost"));
        }

        return action;
    }

    private Reach loadReach(JSONObject jsonObject) throws JSONException {
        ReachImpl reach = new ReachImpl();
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
            reach.setRay(jsonObject.getInt("ray"));
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
                        ColorUtil.byRgba(Integer.parseInt(key, 16)),
                        PlayerConfig.Color.valueOf(jsonObject.getString(key)));
            }
        };

        new JsonKeyIterator(jsonObject.optJSONObject("limits")) {
            @Override
            public void iterate(JSONObject jsonObject, String key) throws JSONException {
                PlayerConfig.Color playerColor = PlayerConfig.Color.valueOf(key.toUpperCase());

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

    private static class JobImpl implements Job {

        public static final int MAX_HEALTH_POINTS = 10;
        public static final int MAX_SPECIAL_POINTS = 10;
        private String name;
        private Integer resistence;
        private Integer evasiveness;
        private Set<Action> actions = new HashSet<Action>();
        private JobSpriteActionGroup spriteActionGroup = new JobSpriteActionGroup();
        private int moviment;
        private float speed = 2.0f;
        private int maximumSpecialPoints = MAX_SPECIAL_POINTS;
        private int maximumHealthPoints = MAX_HEALTH_POINTS;

        public JobImpl(String name) {
            this.name = name;
        }

        public int getEvasiveness() {
            return evasiveness;
        }

        public void setEvasiveness(Integer evasiveness) {
            this.evasiveness = evasiveness;
        }

        public Set<Action> getActions() {
            return actions;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResistence() {
            return resistence;
        }

        public void setResistence(int resistence) {
            this.resistence = resistence;
        }

        public JobSpriteActionGroup getSpriteActionGroup() {
            return spriteActionGroup;
        }

        public void setMoviment(int moviment) {
            this.moviment = moviment;
        }

        public int getMoviment() {
            return this.moviment;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }

        public int getMaximumSpecialPoints() {
            return maximumSpecialPoints;
        }

        public int getMaximumHealthPoints() {
            return maximumHealthPoints;
        }

        public void setMaximumSpecialPoints(int maximumSpecialPoints) {
            this.maximumSpecialPoints = maximumSpecialPoints;
        }

        public void setMaximumHealthPoints(int maximumHealthPoints) {
            this.maximumHealthPoints = maximumHealthPoints;
        }
    }

    private static class ActionImpl implements Action {

        private Integer power;
        private String name;
        private Reach reach;
        private int accuracy;
        private int specialPointsCost;
        private int healthPointsCost;
        private int jobOrder;

        public ActionImpl(String name, int jobOrder) {
            this.name = name;
            this.jobOrder = jobOrder;
        }

        public int getPower() {
            return power;
        }

        public void setPower(Integer power) {
            this.power = power;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public net.stuffrepos.tactics16.battleengine.Reach getReach() {
            return reach;
        }

        public void setReach(Reach reach) {
            this.reach = reach;
        }

        public void setAccuracy(Integer accuracy) {
            this.accuracy = accuracy;
        }

        public int getSpecialPointsCost() {
            return specialPointsCost;
        }

        public int getAccuracy() {
            return this.accuracy;
        }

        public void setSpecialPointsCost(int specialPointsCost) {
            this.specialPointsCost = specialPointsCost;
        }

        public int getHealthPointsCost() {
            return healthPointsCost;
        }

        public void setHealthPointsCost(int healthPointsCost) {
            this.healthPointsCost = healthPointsCost;
        }

        public int compareTo(Action o) {
            if (o instanceof ActionImpl) {
                return new Integer(jobOrder).compareTo(((ActionImpl) o).jobOrder);
            } else {
                return getName().compareTo(o.getName());
            }
        }
    }

    public class ReachImpl implements Reach {

        private final Interval distance = new Interval(1);
        private int ray = 0;
        private boolean clearTrajetory = true;

        public boolean isClearTrajetory() {
            return clearTrajetory;
        }

        public void setClearTrajetory(boolean clearTrajetory) {
            this.clearTrajetory = clearTrajetory;
        }

        public Interval getDistance() {
            return distance;
        }

        public Integer getMinimum() {
            return distance.getMin();
        }

        public Integer getMaximum() {
            return distance.getMax();
        }

        public Integer getRay() {
            return ray;
        }

        public void setRay(int ray) {
            this.ray = ray;
        }

        public boolean getDirect() {
            return clearTrajetory;
        }
    }
}
