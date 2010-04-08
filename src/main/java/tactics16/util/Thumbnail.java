package tactics16.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Thumbnail {

    public static BufferedImage getThumbnail(BufferedImage image,
            int maxThumbWidth,
            int maxThumbHeight,
            RenderingHints hints) {
        BufferedImage thumbnail = null;
        if (image != null) {
            AffineTransform tx = new AffineTransform();
            // Determine scale so image is not larger than the max height and/or width.
            double scale = scaleToFit(image.getWidth(),
                    image.getHeight(),
                    maxThumbWidth, maxThumbHeight);

            tx.scale(scale, scale);

            double d1 = (double) image.getWidth() * scale;
            double d2 = (double) image.getHeight() * scale;
            thumbnail = new BufferedImage(
                    ((int) d1) < 1 ? 1 : (int) d1, // don't allow width to be less than 1
                    ((int) d2) < 1 ? 1 : (int) d2, // don't allow height to be less than 1
                    image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB : image.getType());
            Graphics2D g2d = thumbnail.createGraphics();

            if (hints == null) {
                hints = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            }
            g2d.setRenderingHints(hints);
            g2d.drawImage(image, tx, null);
            g2d.dispose();
        }
        return thumbnail;
    }

    private static double scaleToFit(double w1, double h1, double w2, double h2) {
        double scale = 1.0D;
        if (w1 > h1) {
            if (w1 > w2) {
                scale = w2 / w1;
            }
            h1 *= scale;
            if (h1 > h2) {
                scale *= h2 / h1;
            }
        } else {
            if (h1 > h2) {
                scale = h2 / h1;
            }
            w1 *= scale;
            if (w1 > w2) {
                scale *= w2 / w1;
            }
        }
        return scale;
    }
}
