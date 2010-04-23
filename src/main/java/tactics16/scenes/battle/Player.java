package tactics16.scenes.battle;

import tactics16.animation.GameImage;
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
import tactics16.util.cache.CacheableMapValue;
import tactics16.util.cache.CacheableValue;
import tactics16.util.image.ColorUtil;
import tactics16.util.image.PixelImageIterator;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Player extends DataObject {

    private static final int SELECTED_ACTION_CHANGE_FRAME_INTERVAL = 100;
    private CacheableMapValue<Job, SpriteAnimation> selectedAnimations =
            new CacheableMapValue<Job, SpriteAnimation>() {

                private Collection<GameImage> createImages(Job job, GameImage image) {
                    List<GameImage> list = new LinkedList<GameImage>();
                    final int COUNT = 4;
                    for (int i = 0; i <= COUNT; ++i) {
                        float factor = ((float) i / COUNT) * 0.4f + 0.8f;
                        list.add(createSelectedImage(Player.this.getImage(job.getSpriteActionGroup(), image), factor));
                    }

                    return list;
                }

                private GameImage createSelectedImage(GameImage image, final float factor) {

                    final BufferedImage newImage =
                            new BufferedImage(image.getImage().getWidth(), image.getImage().getHeight(), Transparency.BITMASK);

                    new PixelImageIterator(image.getImage()) {

                        @Override
                        public void iterate(int x, int y, int rgb) {
                            if (rgb == 0) {
                                newImage.setRGB(x, y, 0);
                            } else {
                                newImage.setRGB(x, y, ColorUtil.applyFactor(new java.awt.Color(rgb), factor).getRGB());
                            }
                        }
                    };

                    GameImage gameImage = new GameImage(newImage);
                    gameImage.getCenter().set(image.getCenter());

                    return gameImage;
                }

                @Override
                protected SpriteAnimation calculate(Job key) {
                    SpriteAnimation animation = new SpriteAnimation();
                    SpriteAnimation stoppedAnimation = key.getSpriteActionGroup().getSpriteAction(GameAction.STOPPED);
                    animation.setChangeFrameInterval(SELECTED_ACTION_CHANGE_FRAME_INTERVAL);

                    for (GameImage sourceImage : stoppedAnimation.getImages()) {
                        for (GameImage selectedImage : createImages(key, sourceImage)) {
                            animation.addImage(selectedImage);
                        }
                    }

                    return animation;
                }
            };
    private CacheableMapValue<Job, SpriteAnimation> usedAnimations =
            new CacheableMapValue<Job, SpriteAnimation>() {

                private Collection<GameImage> createImages(Job job, GameImage image) {
                    List<GameImage> list = new LinkedList<GameImage>();
                    list.add(createSelectedImage(Player.this.getImage(job.getSpriteActionGroup(), image)));

                    return list;
                }

                private GameImage createSelectedImage(GameImage image) {

                    final BufferedImage newImage =
                            new BufferedImage(image.getImage().getWidth(), image.getImage().getHeight(), Transparency.BITMASK);

                    new PixelImageIterator(image.getImage()) {

                        @Override
                        public void iterate(int x, int y, int rgb) {
                            if (rgb == 0) {
                                newImage.setRGB(x, y, 0);
                            } else {
                                newImage.setRGB(x, y, ColorUtil.grayScale(new java.awt.Color(rgb)).getRGB());
                            }
                        }
                    };

                    GameImage gameImage = new GameImage(newImage);
                    gameImage.getCenter().set(image.getCenter());

                    return gameImage;
                }

                @Override
                protected SpriteAnimation calculate(Job key) {
                    SpriteAnimation animation = new SpriteAnimation();
                    SpriteAnimation stoppedAnimation = key.getSpriteActionGroup().getSpriteAction(GameAction.STOPPED);
                    animation.setChangeFrameInterval(stoppedAnimation.getChangeFrameInterval());

                    for (GameImage sourceImage : stoppedAnimation.getImages()) {
                        for (GameImage selectedImage : createImages(key, sourceImage)) {
                            animation.addImage(selectedImage);
                        }
                    }

                    return animation;
                }
            };
    private final int index;
    public static final ArrayList<PlayerColors> playersColors;

    static {
        PlayerColors player1Colors = new PlayerColors();
        player1Colors.setMapping(Color.DARK_0, 0x400000);
        player1Colors.setMapping(Color.DARK_1, 0x980000);
        player1Colors.setMapping(Color.DARK_2, 0xE00000);
        player1Colors.setMapping(Color.DARK_3, 0xFF0000);
        player1Colors.setMapping(Color.LIGHT_0, 0x605000);
        player1Colors.setMapping(Color.LIGHT_1, 0xD0D000);
        player1Colors.setMapping(Color.LIGHT_2, 0xA08000);
        player1Colors.setMapping(Color.LIGHT_3, 0xFFFF00);

        PlayerColors player2Colors = new PlayerColors();
        player2Colors.setMapping(Color.DARK_0, 0x000040);
        player2Colors.setMapping(Color.DARK_1, 0x000098);
        player2Colors.setMapping(Color.DARK_2, 0x0000E0);
        player2Colors.setMapping(Color.DARK_3, 0x0000FF);
        player2Colors.setMapping(Color.LIGHT_0, 0x005060);
        player2Colors.setMapping(Color.LIGHT_1, 0x00D0D0);
        player2Colors.setMapping(Color.LIGHT_2, 0x0080A0);
        player2Colors.setMapping(Color.LIGHT_3, 0x00DDFF);
        /*
        player2Colors.setMapping(Color.DARK_0, 0x004000);
        player2Colors.setMapping(Color.DARK_1, 0x009800);
        player2Colors.setMapping(Color.DARK_2, 0x00E000);
        player2Colors.setMapping(Color.DARK_3, 0x00FFFF);
        player2Colors.setMapping(Color.LIGHT_0, 0x006050);
        player2Colors.setMapping(Color.LIGHT_1, 0x00D0D0);
        player2Colors.setMapping(Color.LIGHT_2, 0x00A080);
        player2Colors.setMapping(Color.LIGHT_3, 0x00FFDD);
         */
        playersColors = new ArrayList<PlayerColors>();
        playersColors.add(player1Colors);
        playersColors.add(player2Colors);
    }
    private List<Person> persons = new ArrayList<Person>();

    public Player(String name, int index) {
        super(name);
        this.index = index;
    }

    public GameImage getImage(JobSpriteActionGroup jobSpriteActionGroup, GameImage spriteImage) {
        return playersColors.get(index).getMaskedImage(jobSpriteActionGroup, spriteImage);
    }

    public List<Person> getPersons() {
        return persons;
    }

    public SpriteAnimation getSelectedSpriteAnimation(Job job) {
        return selectedAnimations.getValue(job);
    }

    public SpriteAnimation getUsedSpriteAnimation(Job job) {
        return usedAnimations.getValue(job);
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

    public static class PlayerColors {

        private java.util.Map<Color, Integer> mapping = new TreeMap<Color, Integer>();
        private java.util.Map<GameImage, CacheableValue<GameImage>> maskedImages =
                new HashMap<GameImage, CacheableValue<GameImage>>();

        public void setMapping(Color playerColor, int rgb) {
            mapping.put(playerColor, rgb);
        }

        public GameImage getMaskedImage(final JobSpriteActionGroup jobSpriteActionGroup, final GameImage image) {
            CacheableValue<GameImage> cachedMaskedImage = maskedImages.get(image);

            if (cachedMaskedImage == null) {
                cachedMaskedImage = new CacheableValue<GameImage>() {

                    @Override
                    protected GameImage calculate() {
                        final GameImage maskedImage = image.sameSize();

                        new PixelImageIterator(image.getImage()) {

                            @Override
                            public void iterate(int x, int y, int rgb) {
                                int color = image.getImage().getRGB(x, y);
                                Player.Color playerColor = jobSpriteActionGroup.getMapping(color);
                                if (playerColor != null) {
                                    color = ((color & 0xFF000000) | mapping.get(playerColor));
                                }
                                maskedImage.getImage().setRGB(x, y, color);
                            }
                        };

                        return maskedImage;
                    }
                };
                maskedImages.put(image, cachedMaskedImage);
            }

            return cachedMaskedImage.getValue();
        }

        public Integer getColor(Color playerColor) {
            return mapping.get(playerColor);
        }
    }
}
