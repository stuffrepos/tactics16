package tactics16.animation;

import java.util.HashMap;
import java.util.Map.Entry;
import tactics16.util.CollectionUtil;
import tactics16.util.DataGroup;
import tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class AnimationGroupBuilder {

    private ImageGroup imageGroup = new ImageGroup();
    private DataGroup<Animation> animationGroup = new DataGroup<Animation>();

    private Animation getAnimation(String name) {
        if (!animationGroup.has(name)) {
            animationGroup.add(new Animation(name));
        }

        return animationGroup.getRequired(name);
    }

    private GameImage getImage(String name) {
        if (!imageGroup.hasImage(name)) {
            imageGroup.addImage(name, loadImage(name));
        }

        return imageGroup.getImage(name);
    }

    public void setChangeFrameInterval(String animationName, int changeFrameInterval) {
        getAnimation(animationName).setChangeFrameInterval(changeFrameInterval);
    }

    public void addImage(String animationName, String imageName) {
        getAnimation(animationName).addImage(getImage(imageName));
    }

    public abstract GameImage loadImage(String imageName);

    public AnimationGroup build() {
        AnimationGroupImplementation group = new AnimationGroupImplementation();

        for (Animation a : CollectionUtil.iterableFromIterator(animationGroup.iterator())) {
            group.getAnimationDataGroup().add(a);
        }

        return group;
    }

    private class Animation extends SpriteAnimation implements Nameable {

        private String name;

        public Animation(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class AnimationGroupImplementation implements AnimationGroup {

        private final DataGroup<Animation> animations = new DataGroup<Animation>();

        public DataGroup<Animation> getAnimationDataGroup() {
            return animations;
        }

        public Iterable<java.util.Map.Entry<String, SpriteAnimation>> getAnimations() {
            java.util.Map<String, SpriteAnimation> map = new HashMap<String, SpriteAnimation>();

            for (java.util.Map.Entry<String, Animation> e : this.animations.entrySet()) {
                map.put(e.getKey(), e.getValue());
            }

            return map.entrySet();
        }
    }

    public void setImageCenterY(String imageName, String centerY) {

        GameImage image = getImage(imageName);
        ImageVerticalCenter verticalCenter;
        try {
            verticalCenter = ImageVerticalCenter.valueOf(centerY.toUpperCase());
        } catch (IllegalArgumentException ex) {
            verticalCenter = null;
        }

        if (verticalCenter != null) {
            switch (verticalCenter) {
                case TOP:
                    image.getCenter().setY(0);
                    break;

                case MIDDLE:
                    image.getCenter().setY(image.getImage().getHeight() / 2);
                    break;

                case BOTTOM:
                    image.getCenter().setY(image.getImage().getHeight());
                    break;
            }
        } else {
            image.getCenter().setY(Integer.parseInt(centerY));
        }
    }

    public void setImageCenterX(String imageName, String centerX) {
        GameImage image = getImage(imageName);
        ImageHorizontalCenter horizontalCenter;
        try {
            horizontalCenter = ImageHorizontalCenter.valueOf(centerX.toUpperCase());
        } catch (IllegalArgumentException ex) {
            horizontalCenter = null;
        }

        if (horizontalCenter != null) {
            switch (horizontalCenter) {
                case LEFT:
                    image.getCenter().setX(0);
                    break;

                case CENTER:
                    image.getCenter().setX(image.getImage().getWidth() / 2);
                    break;

                case RIGHT:
                    image.getCenter().setX(image.getImage().getWidth());
                    break;
            }
        } else {
            image.getCenter().setX(Integer.parseInt(centerX));
        }
    }

    private static enum ImageVerticalCenter {

        TOP,
        MIDDLE,
        BOTTOM;
    }

    private static enum ImageHorizontalCenter {

        LEFT,
        CENTER,
        RIGHT
    }
}
