package net.stuffrepos.tactics16.game.playerconfig;

import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import org.newdawn.slick.Color;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectivePlayerColorMode extends PixelToPixelPlayerColorMode {

    private static final SelectivePlayerColorMode instance = new SelectivePlayerColorMode();

    public static SelectivePlayerColorMode getInstance() {
        return instance;
    }    

    private SelectivePlayerColorMode() {
    }

    @Override
    protected Color getJobColor(PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup, Color originalColor) {
        PlayerConfig.Color playerColor = jobSpriteActionGroup.getMapping(originalColor);
        if (playerColor != null) {
            return playerColors.getMaskedColor(playerColor).getColor(ColorUtil.getBetweenFactor(jobSpriteActionGroup.getPlayerColorMin(playerColor), jobSpriteActionGroup.getPlayerColorMax(playerColor), originalColor), jobSpriteActionGroup.getColorMappingMin(), jobSpriteActionGroup.getColorMappingMax());
        } else {
            return originalColor;
        }
    }
}
