package net.stuffrepos.tactics16.scenes.battleconfig;

import java.util.Collection;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.EnginePersonConfig;
import net.stuffrepos.tactics16.game.Job;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonToBattle extends DataObject implements EnginePersonConfig {

    public static final int MAX_HEALTH_POINTS = 10;
    public static final int MAX_SPECIAL_POINTS = 10;
    private final Job job;
    private final PlayerToBattle player;

    public PersonToBattle(PlayerToBattle player, String name, Job job) {
        super(name);
        this.player = player;
        this.job = job;        
    }

    public Job getJob() {
        return job;
    }

    public PlayerToBattle getPlayer() {
        return player;
    }

    public int getEvasiveness() {
        return job.getEvasiveness();
    }

    public int getResistence() {
        return job.getResistence();
    }

    public int getMoviment() {
        return job.getMoviment();
    }

    public int getMaximumHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    public int getMaximumSpecialPoints() {
        return MAX_SPECIAL_POINTS;
    }

    public Collection<Action> getActions() {
        return job.getActions();
    }

    public float getSpeed() {
        return job.getSpeed();
    }
}
