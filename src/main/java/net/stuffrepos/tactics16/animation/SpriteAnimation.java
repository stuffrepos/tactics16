package net.stuffrepos.tactics16.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteAnimation {

    public static final int DEFAULT_CHANGE_FRAME_INTERVAL = 250;
    private int changeFrameInterval = DEFAULT_CHANGE_FRAME_INTERVAL;
    private ArrayList<GameImage> images = new ArrayList<GameImage>();

    public int getChangeFrameInterval() {
        return changeFrameInterval;
    }

    public void setChangeFrameInterval(int changeFrameInterval) {
        this.changeFrameInterval = changeFrameInterval;
    }

    public int getImagesCount() {
        return images.size();
    }

    public GameImage getImage(long elapsedTime) {
        return images.get(getImageIndex(elapsedTime));
    }

    public void addImage(GameImage image) {
        images.add(image);
    }

    private int getImageIndex(long elapsedTime) {
        return (int) ((elapsedTime / changeFrameInterval) % images.size());
    }

    public long getLoopCount(long elapsedTime) {
        return elapsedTime / (changeFrameInterval * images.size());
    }

    public Iterable<GameImage> getImages() {
        return images;
    }

    public GameImage getImageByIndex(int index) {
        return images.get(index);
    }
}
