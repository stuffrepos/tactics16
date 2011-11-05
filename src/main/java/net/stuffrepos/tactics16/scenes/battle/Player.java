package net.stuffrepos.tactics16.scenes.battle;

import net.stuffrepos.tactics16.scenes.battle.playercolors.PlayerColors;
import net.stuffrepos.tactics16.scenes.battle.playercolors.PlayerColorMode;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.game.DataObject;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.game.JobSpriteActionGroup;
import net.stuffrepos.tactics16.scenes.battle.playercolors.SelectivePlayerColorMode;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import net.stuffrepos.tactics16.util.image.ImageUtil;
import net.stuffrepos.tactics16.util.image.PixelImageIterator;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Player extends DataObject {

    private static final int SELECTED_ACTION_CHANGE_FRAME_INTERVAL = 100;
    private static PlayerColorMode colorMode = SelectivePlayerColorMode.getInstance();

    public static void setColorMode(PlayerColorMode aColorMode) {
        colorMode = aColorMode;
        for (Player player : PLAYERS) {
            player.jobAnimations.clear();
            player.selectedAnimations.clear();
            player.usedAnimations.clear();
        }        
    }
    
    public static PlayerColorMode getColorMode() {
        return colorMode;
    }
    
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
                    final ImageBuffer newImage = new ImageBuffer(image.getImage().getWidth(), image.getImage().getHeight());

                    new PixelImageIterator(image.getImage()) {

                        @Override
                        public void iterate(int x, int y, org.newdawn.slick.Color color) {
                            if (org.newdawn.slick.Color.magenta.equals(color)) {
                                newImage.setRGBA(x, y, 0, 0, 0, 0);
                            } else {
                                ImageUtil.setColor(newImage, x, y, ColorUtil.applyFactor(color, factor));
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
                    GameImage gameImage = new GameImage(ImageUtil.grayScale(image.getImage()));
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
    public static final ArrayList<PlayerColors> PLAYER_COLORS;
    private static final ArrayList<Player> PLAYERS;

    static {
        PlayerColors player1Colors = new PlayerColors();
        player1Colors.setMapping(Color.MAIN, 0x400000, 0xFF0000);
        player1Colors.setMapping(Color.SECUNDARY, 0x603030, 0xFFDDDD);

        PlayerColors player2Colors = new PlayerColors();
        player2Colors.setMapping(Color.MAIN, 0x000040, 0x0000FF);
        player2Colors.setMapping(Color.SECUNDARY, 0x303060, 0xDDDDFF);

        PlayerColors player3Colors = new PlayerColors();
        player3Colors.setMapping(Color.MAIN, 0x004000, 0x00FF00);
        player3Colors.setMapping(Color.SECUNDARY, 0x306030, 0xDDFFDD);

        PlayerColors player4Colors = new PlayerColors();
        player4Colors.setMapping(Color.MAIN, 0x404000, 0xFFFF00);
        player4Colors.setMapping(Color.SECUNDARY, 0x606050, 0xFFFFDD);

        PLAYER_COLORS = new ArrayList<PlayerColors>();
        PLAYER_COLORS.add(player1Colors);
        PLAYER_COLORS.add(player2Colors);
        PLAYER_COLORS.add(player3Colors);
        PLAYER_COLORS.add(player4Colors);

        PLAYERS = new ArrayList<Player>();
        for (int i = 0; i < PLAYER_COLORS.size(); i++) {
            PLAYERS.add(new Player("Player " + (i + 1), i));
        }
    }

    public static Player getPlayer(int playerIndex) {
        return PLAYERS.get(playerIndex);
    }
    private PlayerControl control = new HumanPlayerControl();
    private List<Person> persons = new ArrayList<Person>();
    private CacheableMapValue<Job, CacheableMapValue<Job.GameAction, SpriteAnimation>> jobAnimations = new CacheableMapValue<Job, CacheableMapValue<GameAction, SpriteAnimation>>() {

        @Override
        protected CacheableMapValue<GameAction, SpriteAnimation> calculate(final Job job) {
            return new CacheableMapValue<GameAction, SpriteAnimation>() {

                @Override
                protected SpriteAnimation calculate(GameAction gameAction) {
                    SpriteAnimation spriteAction;
                    switch (gameAction) {
                        case SELECTED:
                            spriteAction = getSelectedSpriteAnimation(job);
                            break;

                        case USED:
                            spriteAction = getUsedSpriteAnimation(job);
                            break;

                        default:
                            spriteAction = job.getSpriteActionGroup().getSpriteAction(gameAction);
                    }

                    SpriteAnimation playerJobAnimation = new SpriteAnimation();
                    playerJobAnimation.setChangeFrameInterval(spriteAction.getChangeFrameInterval());

                    for (GameImage image : spriteAction.getImages()) {
                        playerJobAnimation.addImage(getImage(job.getSpriteActionGroup(), image));
                    }

                    return playerJobAnimation;
                }
            };
        }
    };

    private Player(String name, int index) {
        super(name);
        this.index = index;
    }

    private GameImage getImage(JobSpriteActionGroup jobSpriteActionGroup, GameImage spriteImage) {
        try {
            return colorMode.applyColors(spriteImage, PLAYER_COLORS.get(index), jobSpriteActionGroup);
        } catch (SlickException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    private SpriteAnimation getSelectedSpriteAnimation(Job job) {
        return selectedAnimations.getValue(job);
    }

    private SpriteAnimation getUsedSpriteAnimation(Job job) {
        return usedAnimations.getValue(job);
    }

    public SpriteAnimation getSpriteAnimation(Job job, Job.GameAction gameAction) {
        return jobAnimations.getValue(job).getValue(gameAction);
    }

    public org.newdawn.slick.Color getDefaultColor() {
        return getColors().getDefault();
    }

    private PlayerColors getColors() {
        return PLAYER_COLORS.get(index);
    }

    /**
     * @return the control
     */
    public PlayerControl getControl() {
        return control;
    }

    public static enum Color {

        MAIN,
        SECUNDARY,
    }
}
