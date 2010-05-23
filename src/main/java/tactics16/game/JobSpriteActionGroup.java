package tactics16.game;

import java.util.Map.Entry;
import tactics16.animation.AnimationGroup;
import tactics16.scenes.battle.Player;
import tactics16.animation.SpriteAnimation;
import java.util.TreeMap;
import tactics16.game.Job.GameAction;
import tactics16.animation.ImageGroup;
import tactics16.scenes.battle.Player.Color;
import tactics16.util.cache.CacheableMapValue;
import tactics16.util.cache.CacheableValue;
import tactics16.util.image.ColorUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteActionGroup {

    private float colorMappingMin = 0.0f;
    private float colorMappingMax = 1.0f;
    private java.util.Map<Integer, Player.Color> colorMapping =
            new TreeMap<Integer, Player.Color>();
    private java.util.Map<Job.GameAction, SpriteAnimation> spriteActions =
            new TreeMap<Job.GameAction, SpriteAnimation>();
    private ImageGroup images = new ImageGroup();
    private CacheableValue<SpriteAnimation> onAttackingSpriteAnimation =
            new CacheableValue<SpriteAnimation>() {

                @Override
                protected SpriteAnimation calculate() {
                    SpriteAnimation attackingAnimation = spriteActions.get(Job.GameAction.ATTACKING);
                    SpriteAnimation onAttackingAnimation = new SpriteAnimation();
                    onAttackingAnimation.setChangeFrameInterval(1000);
                    onAttackingAnimation.addImage(attackingAnimation.getImageByIndex(
                            attackingAnimation.getImagesCount() - 1));

                    return onAttackingAnimation;
                }
            };
    private CacheableMapValue<Player.Color,Integer> minColors = new CacheableMapValue<Player.Color, Integer>() {

        @Override
        protected Integer calculate(Color playerColor) {
            int min = 0;

            for(Entry<Integer,Player.Color> e: colorMapping.entrySet()) {
                if (e.getValue().equals(playerColor) && ColorUtil.compareColor(e.getKey(),min) < 0 ) {
                    min = e.getKey();
                }
            }

            return min;
        }
    };
    private CacheableMapValue<Player.Color,Integer> maxColors = new CacheableMapValue<Player.Color, Integer>() {

        @Override
        protected Integer calculate(Color playerColor) {
            int max = 0;

            for(Entry<Integer,Player.Color> e: colorMapping.entrySet()) {
                if (e.getValue().equals(playerColor) && ColorUtil.compareColor(e.getKey(),max) > 0 ) {
                    max = e.getKey();
                }
            }

            return max;
        }
    };

    public JobSpriteActionGroup() {
        for (Job.GameAction gameAction : Job.GameAction.values()) {
            spriteActions.put(gameAction, new SpriteAnimation());
        }
    }

    public void setMapping(Integer spriteColor, Player.Color playerColor) {
        colorMapping.put(0x00FFFFFF & spriteColor, playerColor);
        minColors.clear();
        maxColors.clear();
    }

    public Player.Color getMapping(int rgb) {
        return colorMapping.get(0x00FFFFFF & rgb);
    }

    public SpriteAnimation getSpriteAction(GameAction gameAction) {
        if (Job.GameAction.ON_ATTACKING.equals(gameAction)) {
            return onAttackingSpriteAnimation.getValue();
        } else {
            return spriteActions.get(gameAction);
        }
    }

    public void addAction(GameAction gameAction, SpriteAnimation spriteAction) {
        spriteActions.put(gameAction, spriteAction);
    }

    public ImageGroup getImages() {
        return images;
    }

    public void addAnimationGroup(AnimationGroup animationGroup) {
        for (java.util.Map.Entry<String, SpriteAnimation> e : animationGroup.getAnimations()) {
            spriteActions.put(GameAction.valueOf(e.getKey().toUpperCase()), e.getValue());
        }
    }

    public int getPlayerColorMin(Player.Color playerColor) {
        return minColors.getValue(playerColor);
    }

    public int getPlayerColorMax(Player.Color playerColor) {
        return maxColors.getValue(playerColor);
    }

    public void setColorMappingMin(float min) {
        this.colorMappingMin = min;
    }

    public void setColorMappingMax(float max) {
        this.colorMappingMax = max;
    }

    public float getColorMappingMin() {
        return colorMappingMin;
    }

    public float getColorMappingMax() {
        return colorMappingMax;
    }
}
