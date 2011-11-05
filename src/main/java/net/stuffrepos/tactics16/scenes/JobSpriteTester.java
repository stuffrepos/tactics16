package net.stuffrepos.tactics16.scenes;

import org.newdawn.slick.Graphics;
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
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.Player;
import net.stuffrepos.tactics16.scenes.battle.playercolors.PlayerColorMode;
import net.stuffrepos.tactics16.scenes.battle.playercolors.SelectivePlayerColorMode;
import net.stuffrepos.tactics16.scenes.battle.playercolors.SingleColorPlayerColorMode;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor1D;
import net.stuffrepos.tactics16.util.image.DrawerUtil;
import net.stuffrepos.tactics16.util.javabasic.CollectionUtil;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteTester extends Phase {
    
    private static final int ANIMATION_BOX_SIZE = 60;
    private static final JobSpriteTester instance = new JobSpriteTester();
    private EntitiesBoard board = new EntitiesBoard();
    private SpritesBoard spritesBoard;
    private static final double MOVIMENT_SPEED = 5.0;    
    private static final Color[] COLORS = new Color[]{
        Color.black,
        Color.white,
        Color.gray,
        Color.magenta,
        Color.cyan,
        Color.blue,
        Color.red,
        Color.yellow,
        Color.green    
    };
    private ObjectCursor1D<Color> backgroundColorCursor;
    private static final PlayerColorMode[] PLAYER_COLOR_MODES = new PlayerColorMode[]{
        SelectivePlayerColorMode.getInstance(),
        SingleColorPlayerColorMode.getInstance()
    };
    private ObjectCursor1D<PlayerColorMode> playerColorModeCursor;
    private TextBox info;

    private JobSpriteTester() {
    }

    public static JobSpriteTester getInstance() {
        return instance;
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {
        info = new TextBox();
        info.setText(Player.getColorMode().getClass().getSimpleName());
        info.getPosition().setXY(Layout.OBJECT_GAP, Layout.getScreenHeight() - Layout.OBJECT_GAP - info.getHeight());
        backgroundColorCursor = new ObjectCursor1D<Color>(
                CollectionUtil.listFromArray(COLORS));
        backgroundColorCursor.getCursor().setKeys(GameKey.PREVIOUS, GameKey.NEXT);
        playerColorModeCursor = new ObjectCursor1D<PlayerColorMode>(
                CollectionUtil.listFromArray(PLAYER_COLOR_MODES));
        playerColorModeCursor.getCursor().setKeys(null, GameKey.CONFIRM);
        playerColorModeCursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {
                Player.setColorMode(playerColorModeCursor.getSelected());
                info.setText(Player.getColorMode().getClass().getSimpleName());
                rebuildBoard();
            }
        });
    }

    private void rebuildBoard() {
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
    public void update(GameContainer container, StateBasedGame game, int delta) {
        info.update(delta);
        backgroundColorCursor.update(delta);
        playerColorModeCursor.update(delta);
        board.update(delta);

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
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(backgroundColorCursor.getSelected());
        DrawerUtil.fillScreen(g);
        board.render(g);
        info.render(g);
    }

    private static class SpritesBoard extends AbstractObject2D implements VisualEntity {

        private java.util.Map<Coordinate, AnimationBox> animations = new TreeMap<Coordinate, AnimationBox>();

        public SpritesBoard() {
            for (int playerIndex = 0; playerIndex < Map.MAX_PLAYERS; ++playerIndex) {
                Player player = Player.getPlayer(playerIndex);

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

        public void update(int delta) {
            for (AnimationBox animationBox : animations.values()) {
                animationBox.update(delta);
            }
        }

        public void render(Graphics g) {
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
