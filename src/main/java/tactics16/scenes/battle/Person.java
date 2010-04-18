package tactics16.scenes.battle;

import tactics16.game.*;
import tactics16.animation.SpriteAnimation;
import tactics16.util.TransformUtil;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import tactics16.animation.VisualEntity;

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

    public void setCurrentGameAction(Job.GameAction gameAction) {
        if (gameAction != currentGameAction) {
            currentGameAction = gameAction;
            elapsedTime = 0l;
        }
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    public BufferedImage getCurrentImage() {

        SpriteAnimation spriteAction;
        switch(this.currentGameAction) {
            case SELECTED:
                spriteAction = player.getSelectedSpriteAnimation(job);
                break;

            default:
                spriteAction = job.getSpriteActionGroup().getSpriteAction(currentGameAction);
        }
        
        if (spriteAction.getImagesCount() > 0) {
            return player.getImage(job.getSpriteActionGroup(), spriteAction.getImage(elapsedTime));
        } else {
            return null;
        }
    }

    public void render(Graphics2D g) {
        BufferedImage image = getCurrentImage();
        if (image != null) {
            if (side >= 0) {
                g.drawImage(image, position.getX() - image.getWidth() / 2, position.getY() - image.getHeight(), null);
            } else {
                AffineTransform flipTransform = TransformUtil.getFlipHorizontalTransform(
                        image);

                flipTransform.preConcatenate(AffineTransform.getTranslateInstance(
                        position.getX() - image.getWidth() / 2,
                        position.getY() - image.getHeight()));

                g.drawImage(image, flipTransform, null);
            }

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
}
