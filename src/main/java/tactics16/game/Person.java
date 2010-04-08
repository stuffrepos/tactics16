package tactics16.game;

import tactics16.util.TransformUtil;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Person extends DataObject {

    private Job.GameAction currentGameAction;
    private Job job;
    private final Coordinate position = new Coordinate();
    private final Coordinate mapPosition = new Coordinate();
    private long elapsedTime;
    private int side = 1;

    public Person(String name, Job job) {
        super(name);
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
        SpriteAction spriteAction = job.getSpriteActions().get(currentGameAction);
        if (spriteAction.getImagesCount() > 0) {
            return spriteAction.getImage(
                    (int) ((elapsedTime / spriteAction.getChangeFrameInterval()) % spriteAction.getImagesCount()));
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
        this.side = side;
    }
}
