package net.stuffrepos.tactics16.scenes.battle.playercolors;

import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.scenes.battle.Player;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import org.newdawn.slick.Color;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SingleColorPlayerColorMode extends PixelToPixelPlayerColorMode {

    private static final SingleColorPlayerColorMode instance = new SingleColorPlayerColorMode();

    public static SingleColorPlayerColorMode getInstance() {
        return instance;
    }

    private SingleColorPlayerColorMode() {
    }

    @Override
    protected Color getJobColor(PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup, Color color) {
        return ColorUtil.tonalize(color, playerColors.getMaskedColor(Player.Color.MAIN).getColor(1.0f, 1.0f, 1.0f));
    }
}
