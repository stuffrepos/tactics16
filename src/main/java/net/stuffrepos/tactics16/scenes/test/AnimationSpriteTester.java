package net.stuffrepos.tactics16.scenes.test;

import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class AnimationSpriteTester extends Phase {

    private static final AnimationSpriteTester instance = new AnimationSpriteTester();
    private ObjectCursor1D<Animation> animationCursor;
    private SpriteAnimationBoard spriteAnimationBoard;
    private TextBox info;

    private AnimationSpriteTester() {
    }

    public static AnimationSpriteTester getInstance() {
        return instance;
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {
        info = new TextBox();
        animationCursor = new ObjectCursor1D<Animation>(getAllSpriteAnimations());
        animationCursor.getCursor().setKeys(GameKey.LEFT, GameKey.RIGHT);
        animationCursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {
                rebuildSpriteAnimationBoard(animationCursor.getSelected());
            }
        });

        searchAnimation("Wizard", GameAction.STOPPED);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        animationCursor.update(delta);
        spriteAnimationBoard.update(delta);
        info.update(delta);

        if (MyGame.getInstance().keys().isPressed(GameKey.CANCEL)) {
            MyGame.getInstance().getPhaseManager().back();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        spriteAnimationBoard.render(g);
        info.render(g);
    }

    private void rebuildSpriteAnimationBoard(Animation animation) {
        info.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
        info.setText(String.format(
                "Job: %s\nAction: %s\nImages: %d",
                animation.getJob().getName(),
                animation.getGameAction(),
                animation.getSpriteAnimation().getImagesCount()));

        spriteAnimationBoard = new SpriteAnimationBoard(animation.getSpriteAnimation());
        spriteAnimationBoard.getPosition().setXY(
                info.getLeft(),
                Layout.getBottomGap(info));
    }

    private List<Animation> getAllSpriteAnimations() {
        List<Animation> animations = new LinkedList<Animation>();
        for (Job job : MyGame.getInstance().getLoader().getJobs()) {
            for (GameAction action : GameAction.values()) {
                animations.add(new Animation(job, action));
            }
        }
        return animations;
    }

    private void searchAnimation(String jobName, GameAction gameAction) {
        Animation current = animationCursor.getSelected();
        do {
            animationCursor.getCursor().moveNext();
        } while (!((animationCursor.getSelected().getJob().getName().equalsIgnoreCase(jobName)
                && animationCursor.getSelected().getGameAction().equals(gameAction))
                || animationCursor.getSelected().equals(current)));
    }
}

class Animation {

    private final Job job;
    private final GameAction gameAction;

    public Animation(Job job, GameAction gameAction) {
        this.job = job;
        this.gameAction = gameAction;
    }

    public SpriteAnimation getSpriteAnimation() {
        return PlayerConfig.getPlayer(0).getSpriteAnimation(job, gameAction);
    }

    public Job getJob() {
        return job;
    }

    public GameAction getGameAction() {
        return gameAction;
    }
}

class GameImageBox implements Object2D, VisualEntity {

    private GameImageObject2D image;
    private Coordinate position = new Coordinate();
    private TextBox info = new TextBox();

    public GameImageBox(GameImage image) {
        this.image = new GameImageObject2D(image);
        this.info.setText(
                String.format(
                "Size: %dx%d\nCenter: %s\nScale: %f",
                this.image.getImage().getImage().getWidth(),
                this.image.getImage().getImage().getHeight(),
                this.image.getImage().getCenter().toStringInt(),
                this.image.getImage().getScale()));

        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                GameImageBox.this.image.getPosition().setXY(
                        source.getX(),
                        source.getY());
                info.getPosition().setXY(
                        Layout.getRightGap(GameImageBox.this.image),
                        GameImageBox.this.image.getTop());
            }
        });
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return image.getWidth() + Layout.OBJECT_GAP + info.getWidth();
    }

    public int getHeight() {
        return Math.max(image.getHeight(), info.getHeight());
    }

    public void update(int delta) {
        info.update(delta);
    }

    public void render(Graphics g) {
        info.render(g);
        image.render(g);
    }

    public boolean isFinalized() {
        return false;
    }

    public Coordinate getPosition() {
        return position;
    }
}

class GameImageObject2D implements VisualEntity, Object2D {

    private GameImage image;
    private Coordinate position = new Coordinate();

    public GameImageObject2D(GameImage image) {
        this.image = image;
    }

    public void update(int delta) {
    }

    public void render(Graphics g) {
        image.render(
                g,
                position.getX() + image.getCenter().getX(),
                position.getY() + image.getCenter().getY());
    }

    public boolean isFinalized() {
        return false;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return image.getScaledImage().getImage().getWidth();
    }

    public int getHeight() {
        return image.getScaledImage().getImage().getHeight();
    }

    public GameImage getImage() {
        return image;
    }

    public Coordinate getPosition() {
        return position;
    }
}

class SpriteAnimationBoard implements Object2D, VisualEntity {

    private List<GameImageBox> imageBoxes = new LinkedList<GameImageBox>();
    private Coordinate position = new Coordinate();
    private CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            int max = 0;
            for (GameImageBox box : imageBoxes) {
                int calculatedWidth = Layout.getRight(box) - position.getX();
                if (calculatedWidth > max) {
                    max = calculatedWidth;
                }
            }
            return max;
        }
    };
    private CacheableValue<Integer> height = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            int max = 0;
            for (GameImageBox box : imageBoxes) {
                int calculatedHeight = Layout.getBottom(box) - position.getY();
                if (calculatedHeight > max) {
                    max = calculatedHeight;
                }
            }
            return max;
        }
    };

    public SpriteAnimationBoard(final SpriteAnimation spriteAnimation) {
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                rebuild(spriteAnimation);
            }
        });
    }

    private void rebuild(SpriteAnimation spriteAnimation) {
        imageBoxes.clear();
        int dx = 0;
        int dy = 0;
        int lineMaxHeight = 0;
        for (GameImage image : spriteAnimation.getImages()) {
            GameImageBox box = new GameImageBox(image);
            if (position.getX() + dx + box.getWidth() > Layout.getScreenWidth() - Layout.OBJECT_GAP) {
                dx = 0;
                dy += lineMaxHeight + Layout.OBJECT_GAP;
                lineMaxHeight = 0;
            }
            box.getPosition().setXY(
                    position.getX() + dx,
                    position.getY() + dy);

            if (box.getHeight() > lineMaxHeight) {
                lineMaxHeight = box.getHeight();
            }

            dx = Layout.getRightGap(box);
            imageBoxes.add(box);
        }
        width.clear();
        height.clear();
    }

    public int getTop() {
        return position.getX();
    }

    public int getLeft() {
        return position.getY();
    }

    public int getWidth() {
        return width.getValue();
    }

    public int getHeight() {
        return height.getValue();
    }

    public void update(int delta) {
        for (GameImageBox box : imageBoxes) {
            box.update(delta);
        }
    }

    public void render(Graphics g) {
        for (GameImageBox box : imageBoxes) {
            box.render(g);
        }
    }

    public boolean isFinalized() {
        return false;
    }

    public Coordinate getPosition() {
        return position;
    }
}
