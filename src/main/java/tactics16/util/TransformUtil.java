package tactics16.util;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TransformUtil {    

    public static AffineTransform getFlipHorizontalTransform(BufferedImage image) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1.0d, 1.0d);
        transform.preConcatenate(AffineTransform.getTranslateInstance(image.getWidth(), 0.0d));
        return transform;
    }
}
