package tactics16.game;

import tactics16.scenes.battle.Player;
import tactics16.animation.SpriteAnimation;
import java.util.TreeMap;
import tactics16.game.Job.GameAction;
import tactics16.animation.ImageGroup;
import tactics16.util.CacheableValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteActionGroup {

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

    public JobSpriteActionGroup() {
        for (Job.GameAction gameAction : Job.GameAction.values()) {
            spriteActions.put(gameAction, new SpriteAnimation());
        }
    }

    public void setMapping(Integer spriteColor, Player.Color playerColor) {
        colorMapping.put(0x00FFFFFF & spriteColor, playerColor);
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
}
