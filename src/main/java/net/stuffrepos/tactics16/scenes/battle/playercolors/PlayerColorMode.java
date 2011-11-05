package net.stuffrepos.tactics16.scenes.battle.playercolors;

import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface PlayerColorMode {

    public GameImage applyColors(GameImage image, PlayerColors playerColors,
            JobSpriteActionGroup jobSpriteActionGroup) throws SlickException;
}
