package tactics16.scenes.battle;

import tactics16.animation.SpriteAnimation;
import tactics16.game.*;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import tactics16.game.Job.GameAction;
import tactics16.util.CacheableMapValue;
import tactics16.util.CacheableValue;
import tactics16.util.ColorUtil;
import tactics16.util.PixelImageIterator;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Player extends DataObject {

    private static final int SELECTED_ACTION_CHANGE_FRAME_INTERVAL = 100;
    private CacheableMapValue<Job, SpriteAnimation> selectedAnimations =
            new CacheableMapValue<Job, SpriteAnimation>() {

                private Collection<BufferedImage> createImages(Job job,BufferedImage image) {
                    List<BufferedImage> list = new LinkedList<BufferedImage>();
                    final int COUNT = 4;
                    for (int i = 0; i <= COUNT; ++i) {
                        float factor = ((float) i / COUNT) * 0.4f + 0.8f;
                        list.add(createSelectedImage(Player.this.getImage(job.getSpriteActionGroup(), image), factor));
                    }

                    return list;
                }

                private BufferedImage createSelectedImage(BufferedImage image, final float factor) {
                    
                    final BufferedImage newImage =
                            new BufferedImage(image.getWidth(), image.getHeight(), Transparency.BITMASK);

                    new PixelImageIterator(image) {

                        @Override
                        public void iterate(int x, int y, int rgb) {
                            if (rgb == 0) {
                                newImage.setRGB(x, y, 0);
                            } else {
                                newImage.setRGB(x, y, ColorUtil.applyFactor(new java.awt.Color(rgb), factor).getRGB());
                            }
                        }
                    };

                    return newImage;
                }

                @Override
                protected SpriteAnimation calculate(Job key) {
                    SpriteAnimation animation = new SpriteAnimation();
                    SpriteAnimation stoppedAnimation = key.getSpriteActionGroup().getSpriteAction(GameAction.STOPPED);
                    animation.setChangeFrameInterval(SELECTED_ACTION_CHANGE_FRAME_INTERVAL);

                    for (BufferedImage sourceImage : stoppedAnimation.getImages()) {
                        for (BufferedImage selectedImage : createImages(key,sourceImage)) {
                            animation.addImage(selectedImage);
                        }
                    }

                    return animation;
                }
            };
    private final int index;
    private static final ArrayList<PlayerColors> playersColors;

    static {
        PlayerColors player1Colors = new PlayerColors();
        player1Colors.setMapping(Color.DARK_0, 0x400000);
        player1Colors.setMapping(Color.DARK_1, 0x980000);
        player1Colors.setMapping(Color.DARK_2, 0xE00000);
        player1Colors.setMapping(Color.DARK_3, 0xFFFFFF);
        player1Colors.setMapping(Color.LIGHT_0, 0x605000);
        player1Colors.setMapping(Color.LIGHT_1, 0xD0D000);
        player1Colors.setMapping(Color.LIGHT_2, 0xA08000);
        player1Colors.setMapping(Color.LIGHT_3, 0xFFFF00);

        PlayerColors player2Colors = new PlayerColors();
        player2Colors.setMapping(Color.DARK_0, 0x000040);
        player2Colors.setMapping(Color.DARK_1, 0x000098);
        player2Colors.setMapping(Color.DARK_2, 0x0000E0);
        player2Colors.setMapping(Color.DARK_3, 0xFFFFFF);
        player2Colors.setMapping(Color.LIGHT_0, 0x005060);
        player2Colors.setMapping(Color.LIGHT_1, 0x00D0D0);
        player2Colors.setMapping(Color.LIGHT_2, 0x0080A0);
        player2Colors.setMapping(Color.LIGHT_3, 0x00FFFF);

        playersColors = new ArrayList<PlayerColors>();
        playersColors.add(player2Colors);
        playersColors.add(player1Colors);
    }

    private List<Person> persons = new ArrayList<Person>();

    public Player(String name, int index) {
        super(name);
        this.index = index;        
    }

    public BufferedImage getImage(JobSpriteActionGroup jobSpriteActionGroup, BufferedImage spriteImage) {
        return playersColors.get(index).getMaskedImage(jobSpriteActionGroup, spriteImage);
    }

    public List<Person> getPersons() {
        return persons;
    }

    public SpriteAnimation getSelectedSpriteAnimation(Job job) {
        return selectedAnimations.getValue(job);
    }

    public static enum Color {

        DARK_0,
        DARK_1,
        DARK_2,
        DARK_3,
        LIGHT_0,
        LIGHT_1,
        LIGHT_2,
        LIGHT_3
    }

    private static class PlayerColors {

        private java.util.Map<Color, Integer> mapping = new TreeMap<Color, Integer>();
        private java.util.Map<BufferedImage, CacheableValue<BufferedImage>> maskedImages =
                new HashMap<BufferedImage, CacheableValue<BufferedImage>>();

        public void setMapping(Color playerColor, int rgb) {
            mapping.put(playerColor, rgb);
        }

        public BufferedImage getMaskedImage(final JobSpriteActionGroup jobSpriteActionGroup, final BufferedImage image) {
            CacheableValue<BufferedImage> cachedMaskedImage = maskedImages.get(image);

            if (cachedMaskedImage == null) {
                cachedMaskedImage = new CacheableValue<BufferedImage>() {

                    @Override
                    protected BufferedImage calculate() {
                        BufferedImage maskedImage = new BufferedImage(
                                image.getWidth(),
                                image.getHeight(),
                                Transparency.BITMASK);

                        for (int x = 0; x < maskedImage.getWidth(); ++x) {
                            for (int y = 0; y < maskedImage.getHeight(); ++y) {
                                int color = image.getRGB(x, y);
                                Player.Color playerColor = jobSpriteActionGroup.getMapping(color);
                                if (playerColor != null) {
                                    color = ((color & 0xFF000000) | mapping.get(playerColor));
                                }
                                maskedImage.setRGB(x, y, color);
                            }
                        }
                        return maskedImage;
                    }
                };
                maskedImages.put(image, cachedMaskedImage);
            }

            return cachedMaskedImage.getValue();
        }
    }
}
