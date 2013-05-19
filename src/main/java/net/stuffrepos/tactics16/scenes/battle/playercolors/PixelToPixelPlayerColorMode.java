package net.stuffrepos.tactics16.scenes.battle.playercolors;

import java.util.HashMap;
import java.util.Map;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import net.stuffrepos.tactics16.util.image.PixelImageCopyIterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class PixelToPixelPlayerColorMode implements PlayerColorMode {

    private CacheableMapValue<
            JobSpriteActionGroup, CacheableMapValue<PlayerColors, CacheableMapValue<GameImage, GameImage>>> maskedImages = new CacheableMapValue<JobSpriteActionGroup, CacheableMapValue<PlayerColors, CacheableMapValue<GameImage, GameImage>>>() {
        @Override
        protected CacheableMapValue<PlayerColors, CacheableMapValue<GameImage, GameImage>> calculate(final JobSpriteActionGroup jobSpriteActionGroup) {
            return new CacheableMapValue<PlayerColors, CacheableMapValue<GameImage, GameImage>>() {
                @Override
                protected CacheableMapValue<GameImage, GameImage> calculate(final PlayerColors playerColors) {
                    return new CacheableMapValue<GameImage, GameImage>() {
                        @Override
                        protected GameImage calculate(GameImage image) {
                            return image.clone(new PixelImageCopyIterator(image.getImage()) {
                                @Override
                                protected Color iterate(int x, int y, Color color) {
                                    return getJobColor(playerColors, jobSpriteActionGroup, color);
                                }
                            }.build());
                        }
                    };
                }
            };
        }
    };

    public final GameImage applyColors(GameImage image, PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup) throws SlickException {
        return maskedImages.getValue(jobSpriteActionGroup).getValue(playerColors).getValue(image);
    }

    protected abstract Color getJobColor(PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup, Color color);
}
