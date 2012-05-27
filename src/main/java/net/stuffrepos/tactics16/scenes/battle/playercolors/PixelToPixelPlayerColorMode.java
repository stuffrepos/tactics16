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

    private Map<PlayerColors, CacheableMapValue<GameImage, GameImage>> maskedImages = new HashMap<PlayerColors, CacheableMapValue<GameImage, GameImage>>();

    public final GameImage applyColors(GameImage image, PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup) throws SlickException {
        return getMaskedImage(image, playerColors, jobSpriteActionGroup);
    }

    private GameImage getMaskedImage(final GameImage image, final PlayerColors playerColors, final JobSpriteActionGroup jobSpriteActionGroup) {
        CacheableMapValue<GameImage, GameImage> cachedMaskedImage = maskedImages.get(playerColors);
        if (cachedMaskedImage == null) {
            cachedMaskedImage = new CacheableMapValue<GameImage, GameImage>() {

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
            maskedImages.put(playerColors, cachedMaskedImage);
        }
        return cachedMaskedImage.getValue(image);
    }

    protected abstract Color getJobColor(PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup, Color color);
}
