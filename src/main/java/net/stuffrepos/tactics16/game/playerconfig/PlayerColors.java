package net.stuffrepos.tactics16.game.playerconfig;

import java.util.Map;
import java.util.TreeMap;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import org.newdawn.slick.Color;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PlayerColors {

    public static class MaskedColor {

        private int min;
        private int max;

        public MaskedColor(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public Color getColor(float factor, float minLimit, float maxLimit) {
            return ColorUtil.getBetweenColor(min, max, calculateRealFactor(factor, minLimit, maxLimit));
        }

        private static float calculateRealFactor(float factor, float minLimit, float maxLimit) {
            return factor * (maxLimit - minLimit) + minLimit;
        }
    }
    private Map<PlayerConfig.Color, MaskedColor> mapping = new TreeMap<PlayerConfig.Color, MaskedColor>();   

    public void setMapping(PlayerConfig.Color playerColor, int minRgb, int maxRgb) {
        mapping.put(playerColor, new MaskedColor(minRgb, maxRgb));
    }
    
    public MaskedColor getMaskedColor(PlayerConfig.Color playerColor) {
        return mapping.get(playerColor);
    }

    public Color getDefault() {
        return new Color(mapping.get(PlayerConfig.Color.MAIN).getColor(0.5F, 0.0F, 1.0F));
    }    
    
}
