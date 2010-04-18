package tactics16.scenes.battle;

import tactics16.GameKey;
import tactics16.util.CollectionUtil;
import tactics16.game.Job;
import tactics16.util.Cursor1D;
import tactics16.util.ObjectCursor1D;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobAnimationTest {

    private BattleGame battleGame;
    private ObjectCursor1D<Job.GameAction> actionCursor;

    public JobAnimationTest(BattleGame battleGame) {
        this.battleGame = battleGame;
        this.actionCursor = new ObjectCursor1D<Job.GameAction>(
                CollectionUtil.listFromArray(Job.GameAction.values()));
        this.actionCursor.getCursor().setKeys(GameKey.PREVIOUS, GameKey.NEXT);
        this.actionCursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {
                for (Player player : JobAnimationTest.this.battleGame.getPlayers()) {
                    for (Person person : player.getPersons()) {
                        person.setCurrentGameAction(actionCursor.getSelected());
                    }
                }
            }
        });
    }

    public void update(long elapsedTime) {
        this.actionCursor.update(elapsedTime);
    }
}
