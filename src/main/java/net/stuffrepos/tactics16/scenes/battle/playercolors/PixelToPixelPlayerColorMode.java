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
    private CacheableMapValue<PlayerColors, CacheableMapValue<JobSpriteActionGroup, CacheableMapValue<Color, Color>>> jobsColors = new CacheableMapValue<PlayerColors, CacheableMapValue<JobSpriteActionGroup, CacheableMapValue<Color, Color>>>() {

        @Override
        protected CacheableMapValue<JobSpriteActionGroup, CacheableMapValue<Color, Color>> calculate(final PlayerColors playerColors) {
            return new CacheableMapValue<JobSpriteActionGroup, CacheableMapValue<Color, Color>>() {

                @Override
                protected CacheableMapValue<Color, Color> calculate(final JobSpriteActionGroup jobSpriteActionGroup) {
                    return new CacheableMapValue<Color, Color>() {

                        @Override
                        protected Color calculate(Color originalColor) {
                            return getJobColor(playerColors, jobSpriteActionGroup, originalColor);
                        }
                    };
                }
            };
        }
    };

    public final GameImage applyColors(GameImage image, PlayerColors playerColors, JobSpriteActionGroup jobSpriteActionGroup) throws SlickException {
        GameImage valueImage = getMaskedImage(image, playerColors, jobSpriteActionGroup);
        if (valueImage.getScale() == 1.0) {
            return valueImage;
        } else {
            return valueImage.getScaledImage();
        }
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
                            return jobsColors.getValue(playerColors).getValue(jobSpriteActionGroup).getValue(color);
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
