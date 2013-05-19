package net.stuffrepos.tactics16.scenes.battle;

import java.util.Collection;
import java.util.HashSet;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.DataObject;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.util.LifoQueue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Person extends DataObject implements VisualEntity, net.stuffrepos.tactics16.battlegameengine.Person {
    
    public static final int MAX_HEALTH_POINTS = 10;        
    public static final int MAX_SPECIAL_POINTS = 10;
    private Job.GameAction currentGameAction;
    private Job job;
    private final Coordinate position = new Coordinate();
    private final Coordinate mapPosition = new Coordinate();
    private long elapsedTime;
    private int side = 1;
    private Player player;
    private int healthPoints = MAX_HEALTH_POINTS;    
    private GameActionControl gameActionControl = new GameActionControl();

    public Person(Player player, String name, Job job) {
        super(name);
        this.player = player;
        this.job = job;
        this.setCurrentGameAction(Job.GameAction.STOPPED);
    }

    public Job getJob() {
        return job;
    }

    public Coordinate getPosition() {
        return position;
    }

    private void setCurrentGameAction(Job.GameAction gameAction) {
        if (gameAction != currentGameAction) {
            currentGameAction = gameAction;
            elapsedTime = 0l;
        }
    }

    public void update(int delta) {
        this.elapsedTime += delta;
    }

    public GameImage getCurrentImage() {
        return player.getSpriteAnimation(job, currentGameAction).getImage(elapsedTime);
    }

    public void render(Graphics g) {
        GameImage image = getCurrentImage();
        if (image != null) {
            image.render(
                    g,
                    position,
                    side < 0,
                    false);
        }
    }

    public Coordinate getMapPosition() {
        return mapPosition;
    }

    public void setSide(int side) {
        if (side != 0) {
            this.side = side;
        }

    }

    public Player getPlayer() {
        return player;
    }

    public int getMoviment() {
        return job.getMoviment();
    }

    public int getHealthPoints() {
        return healthPoints;
    }    

    public boolean isFinalized() {
        return false;
    }

    public long getAnimationLoopCount() {
        SpriteAnimation spriteAction = job.getSpriteActionGroup().getSpriteAction(currentGameAction);
        if (spriteAction == null) {
            return 0;
        } else {
            return spriteAction.getLoopCount(elapsedTime);
        }
    }

    public int getEvasiveness() {
        return job.getEvasiveness();
    }

    public int getDefense() {
        return job.getResistence();
    }

    public GameActionControl getGameActionControl() {
        return gameActionControl;
    }

    public void decreaseHealthPoints(int hp) {
        healthPoints -= hp;
    }

    public int getResistence() {
        return job.getResistence();
    }

    public int getMaximumHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    public int getMaximumSpecialPoints() {
        return MAX_SPECIAL_POINTS;
    }

    public Collection<Action> getActions() {
        return new HashSet<Action>(job.getActions());
    }

    public float getSpeed() {
        return job.getSpeed();
    }

    public class GameActionControl {

        private LifoQueue<Job.GameAction> stack = new LifoQueue<Job.GameAction>();

        public void set(Job.GameAction gameAction) {
            stack.clear();
            stack.add(gameAction);
            setCurrentGameAction(gameAction);
        }

        public void advance(Job.GameAction gameAction) {
            stack.add(gameAction);
            setCurrentGameAction(gameAction);
        }

        public void back() {
            if (stack.size() > 1) {
                stack.poll();
                setCurrentGameAction(stack.peek());
            }
        }
    }
}
