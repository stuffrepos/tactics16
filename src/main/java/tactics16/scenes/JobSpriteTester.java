package tactics16.scenes;

import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.animation.EntitiesBoard;
import tactics16.components.AnimationBox;
import tactics16.components.PhaseTitle;
import tactics16.game.Job;
import tactics16.game.Map;
import tactics16.phase.AbstractPhase;
import tactics16.scenes.battle.Player;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobSpriteTester extends AbstractPhase {

    private static final int ANIMATION_BOX_SIZE = 60;
    private static final JobSpriteTester instance = new JobSpriteTester();
    private EntitiesBoard board = new EntitiesBoard();

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
                    animation.getPosition().setXY(
                            Layout.OBJECT_GAP + actionIndex * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP),
                            Layout.getBottomGap(title) + (playerIndex + jobIndex * Map.MAX_PLAYERS) * (ANIMATION_BOX_SIZE + Layout.OBJECT_GAP));
                    board.getChildren().add(animation);
                    actionIndex++;
                }
                jobIndex++;
            }
        }
    }

    @Override
    public void update(long elapsedTime) {
        board.update(elapsedTime);

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            MyGame.getInstance().getPhaseManager().back();
        }
    }

    @Override
    public void render(Graphics2D g) {
        board.render(g);
    }
}
