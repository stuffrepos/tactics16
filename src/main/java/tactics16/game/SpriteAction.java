package tactics16.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SpriteAction {

    public static final int DEFAULT_CHANGE_FRAME_INTERVAL = 250;
    private int changeFrameInterval = DEFAULT_CHANGE_FRAME_INTERVAL;
    private List<BufferedImage> images = new ArrayList<BufferedImage>();

    public int getChangeFrameInterval() {
        return changeFrameInterval;
    }

    public void setChangeFrameInterval(int changeFrameInterval) {
        this.changeFrameInterval = changeFrameInterval;
    }

    public int getImagesCount() {
        return images.size();
    }

    public BufferedImage getImage(int index) {
        return images.get(index);
    }

    public void addImage(BufferedImage image) {
        images.add(image);
    }
}
