package tactics16.animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class AnimationGroupBuilder {
   
    public void setChangeFrameInterval(String animationName,int changeFrameInterval) {
    }

    public void addImage(String animationName,String imageName) {
    }

    public void setImageCenterX(String imageName,int centerX) {
    }

    public void setImageCenterY(String imageName,int centerY) {
    }

    public abstract BufferedImage loadImage(String imageName);
}
