package net.stuffrepos.tactics16.scenes;

import java.awt.Graphics2D;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.AbstractObject2D;
import net.stuffrepos.tactics16.components.AnimationBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.scenes.battle.Player;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteTester extends AbstractPhase {

    private static final int ANIMATION_BOX_SIZE = 60;
    private static final JobSpriteTester instance = new JobSpriteTester();
    private EntitiesBoard board = new EntitiesBoard();
    private SpritesBoard spritesBoard;
    private static final double MOVIMENT_SPEED = 5.0;

    private JobSpriteTester() {
    }

    public static JobSpriteTester getInstance() {
        return instance;
    }

    @Override
    public void onAdd() {
        board.getChildren().clear();
        final PhaseTitle title = new PhaseTitle("Job Sprite Tester");

        board.getChildren().add(title);

        spritesBoard = new SpritesBoard();
        spritesBoard.getPosition().setXY(
                Layout.OBJECT_GAP,
                Layout.getBottomGap(title));
        board.getChildren().add(spritesBoard);


    }

    @Override
    public void update(long elapsedTime) {
        board.update(elapsedTime);

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            MyGame.getInstance().getPhaseManager().back();
        } else if (MyGame.getInstance().isKeyPressed(GameKey.UP)) {
            spritesBoard.getPosition().addY(-MOVIMENT_SPEED);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.DOWN)) {
            spritesBoard.getPosition().addY(MOVIMENT_SPEED);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.LEFT)) {
            spritesBoard.getPosition().addX(-MOVIMENT_SPEED);
        } else if (MyGame.getInstance().isKeyPressed(GameKey.RIGHT)) {
            spritesBoard.getPosition().addX(MOVIMENT_SPEED);
        }
    }

    @Override
    public void render(Graphics2D g) {
        board.render(g);
    }

    private static class SpritesBoard extends AbstractObject2D implements VisualEntity {

        private java.util.Map<Coordinate, AnimationBox> animations = new TreeMap<Coordinate, AnimationBox>();

        public SpritesBoard() {
            for (int playerIndex = 0; playerIndex < Map.MAX_PLAYERS; ++playerIndex) {
                Player player = new Player("Player " + (playerIndex + 1), playerIndex);

                int jobIndex = 0;
                for (Job job : MyGame.getInstance().getLoader().getJobs()) {
                    int actionIndex = 0;
                    for (Job.GameAction gameAction : Job.GameAction.values()) {
                        AnimationBox animation = new AnimationBox(player.getSpriteAnimation(job, gameAction));
                        animation.getWidthConfig().setFixed(ANIMATION_BOX_SIZE);
                        animation.getHeightConfig().setFixed(ANIMATION_BOX_SIZE);
                        animation.setBackgroundColor(null);
                        animations.put(
                                new Coordinate(actionIndex, playerIndex + jobIndex * Map.MAX_PLAYERS),
                                animation);
                        actionIndex++;
                    }
                    jobIndex++;
                }
            }

            getPosition().addListener(new Listener<Coordinate>() {

                public void onChange(Coordinate source) {
                    for (Entry<Coordinate, AnimationBox> e : animations.entrySet()) {
                        e.getValue().getPosition().setXY(
                                getLeft() + e.getKey().getX() * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP),
                                getTop() + e.getKey().getY() * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP));
                    }
                }
            });
        }

        public void update(long elapsedTime) {
            for (AnimationBox animationBox : animations.values()) {
                animationBox.update(elapsedTime);
            }
        }

        public void render(Graphics2D g) {
            for (AnimationBox animationBox : animations.values()) {
                animationBox.render(g);
            }
        }

        public boolean isFinalized() {
            return false;
        }

        @Override
        protected int calculateWidth() {
            return Job.GameAction.values().length * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP) - Layout.OBJECT_GAP;
        }

        @Override
        protected int calculateHeight() {
            return (MyGame.getInstance().getLoader().getJobs().size() * Map.MAX_PLAYERS) * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP) - Layout.OBJECT_GAP;
        }
    }
}
