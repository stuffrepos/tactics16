package tactics16.util.image;

import java.awt.Color;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ColorUtil {

    public static Color dark(Color color) {
        return applyFactor(color, 0.5f);
    }

    public static Color light(Color color) {
        return applyFactor(color, 1.5f);
    }

    public static Color grayScale(Color color) {
        int c = Math.max(color.getRed(), Math.max(color.getGreen(), color.getBlue()));
        return new Color(c, c, c);
    }

    public static Color getBetweenColor(Color beginColor, Color endColor, float factor) {
        return new Color(
                limitColor((endColor.getRed() - beginColor.getRed()) * factor + beginColor.getRed()),
                limitColor((endColor.getGreen() - beginColor.getGreen()) * factor + beginColor.getGreen()),
                limitColor((endColor.getBlue() - beginColor.getBlue()) * factor + beginColor.getBlue()));
    }

    public static Color applyFactor(Color color, float factor) {
        /*
        int red = limitColor(color.getRed() * factor);
        int green = limitColor(color.getGreen() * factor);
        int blue = limitColor(color.getBlue() * factor);
        float redFactor = (float)red / color.getRed();
        float greenFactor = (float)green / color.getGreen();
        float blueFactor = (float)blue / color.getBlue();
        factor = Math.min(redFactor, Math.min(blueFactor, greenFactor));*/
        return new Color(
                limitColor(color.getRed() * factor),
                limitColor(color.getGreen() * factor),
                limitColor(color.getBlue() * factor));
    }

    public static int getRgbBitmask(int rgba) {
        Color color = new Color(rgba, true);

        if (color.getAlpha() < 0x100 / 2) {
            return 0x00FFFFFF & rgba;
        } else {
            return 0xFF000000 | rgba;
        }
    }

    private static int limitColor(float color) {
        return limitColor((int) color);
    }

    private static int limitColor(int color) {
        color = Math.abs(color);
        if (color < 0) {
            return color;
        } else if (color > 0xFF) {
            return 0xFF;
        } else {
            return color;
        }
    }
}
