package net.stuffrepos.tactics16.game;

import java.util.HashMap;
import java.util.Map.Entry;
import net.stuffrepos.tactics16.animation.AnimationGroup;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import java.util.TreeMap;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.animation.ImageGroup;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig.Color;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.image.ColorUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteActionGroup {

    private float colorMappingMin = 0.0f;
    private float colorMappingMax = 1.0f;
    private java.util.Map<org.newdawn.slick.Color, PlayerConfig.Color> colorMapping =
            new HashMap<org.newdawn.slick.Color, PlayerConfig.Color>();
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
    private CacheableMapValue<PlayerConfig.Color,org.newdawn.slick.Color> minColors = new CacheableMapValue<PlayerConfig.Color, org.newdawn.slick.Color>() {

        @Override
        protected org.newdawn.slick.Color calculate(Color playerColor) {
            org.newdawn.slick.Color min = org.newdawn.slick.Color.black;

            for(Entry<org.newdawn.slick.Color,PlayerConfig.Color> e: colorMapping.entrySet()) {
                if (e.getValue().equals(playerColor) && ColorUtil.compareColor(e.getKey(),min) < 0 ) {
                    min = e.getKey();
                }
            }

            return min;
        }
    };
    private CacheableMapValue<PlayerConfig.Color,org.newdawn.slick.Color> maxColors = new CacheableMapValue<PlayerConfig.Color, org.newdawn.slick.Color>() {

        @Override
        protected org.newdawn.slick.Color calculate(Color playerColor) {
            org.newdawn.slick.Color max = org.newdawn.slick.Color.black;

            for(Entry<org.newdawn.slick.Color,PlayerConfig.Color> e: colorMapping.entrySet()) {
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

    public void setMapping(org.newdawn.slick.Color spriteColor, PlayerConfig.Color playerColor) {        
        colorMapping.put(ColorUtil.opaque(spriteColor), playerColor);
        minColors.clear();
        maxColors.clear();
    }

    public PlayerConfig.Color getMapping(org.newdawn.slick.Color color) {
        return colorMapping.get(ColorUtil.opaque(color));
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

    public org.newdawn.slick.Color getPlayerColorMin(PlayerConfig.Color playerColor) {
        return minColors.getValue(playerColor);
    }

    public org.newdawn.slick.Color getPlayerColorMax(PlayerConfig.Color playerColor) {
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
