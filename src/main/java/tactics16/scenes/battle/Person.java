package tactics16.scenes.battle;

import tactics16.animation.SpriteAnimation;
import java.awt.Graphics2D;
import tactics16.animation.GameImage;
import tactics16.animation.VisualEntity;
import tactics16.game.Coordinate;
import tactics16.game.DataObject;
import tactics16.game.Job;
import tactics16.util.LifoQueue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Person extends DataObject implements VisualEntity {

    private static final int DEFAULT_MOVIMENT = 4;
    public static final int MAX_HEALTH_POINTS = 10;
    public static final int MAX_AGILITY_POINTS = 10;
    public static final int MAX_ACTION_USE_AGILITY_POINTS = MAX_AGILITY_POINTS / 2;
    private Job.GameAction currentGameAction;
    private Job job;
    private final Coordinate position = new Coordinate();
    private final Coordinate mapPosition = new Coordinate();
    private long elapsedTime;
    private int side = 1;
    private Player player;
    private int healthPoints = MAX_HEALTH_POINTS;
    private int agilityPoints = MAX_AGILITY_POINTS;
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

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public GameImage getCurrentImage() {
        return player.getSpriteAnimation(job, currentGameAction).getImage(elapsedTime);
    }

    public void render(Graphics2D g) {
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
        return DEFAULT_MOVIMENT;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAgilityPoints() {
        return agilityPoints;
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
        return job.getDefense();
    }

    public GameActionControl getGameActionControl() {
        return gameActionControl;
    }

    public void decreaseAgilityPoints(int ap) {
        agilityPoints -= ap;        
    }

    public void decreaseHealthPoints(int hp) {
        healthPoints -= hp;
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
