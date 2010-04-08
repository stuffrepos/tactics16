package tactics16.scenes.battle;

import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import tactics16.game.Job;
import tactics16.game.Map;
import tactics16.game.Person;
import tactics16.game.Player;
import tactics16.phase.Phase;
import tactics16.phase.PhaseManager;
import tactics16.util.Cursor1D;
import tactics16.util.ObjectCursor1D;
import tactics16.util.listeners.Listener;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleScene implements Phase {

    private final BattleGame geoGame;
    private ObjectCursor1D<Job.GameAction> gameActionCursor;
    // Layout
    public static final int MAP_GAP = Layout.OBJECT_GAP * 5;
    private TextDialog statusDialog = new TextDialog();
    private Coordinate mapPosition;
    // Phases
    private final MovimentSubPhase selectMovimentSubPhase = new MovimentSubPhase(this);
    private PhaseManager phaseManager = new PhaseManager();

    public BattleScene(BattleGame geoGame) {
        this.geoGame = geoGame;

        List<Job.GameAction> gameActionList = new ArrayList<Job.GameAction>();
        for (Job.GameAction gameAction : Job.GameAction.values()) {
            gameActionList.add(gameAction);
        }
        this.gameActionCursor = new ObjectCursor1D<Job.GameAction>(gameActionList);
        this.gameActionCursor.getCursor().setKeys(KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN);
        this.gameActionCursor.getCursor().addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {
                changePersonsGameAction();
            }
        });

        // Positions and Dimensions
        statusDialog.setWidth(100);
        statusDialog.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - statusDialog.getWidth(),
                Layout.OBJECT_GAP);
        mapPosition = new Coordinate(MAP_GAP, MAP_GAP);

        // Persons Positions
        for (int player = 0; player < geoGame.getPlayers().size(); ++player) {
            for (int person = 0; person < geoGame.getPlayers().get(player).getPersons().size(); ++person) {
                putPersonOnPosition(
                        geoGame.getPlayers().get(player).getPersons().get(person),
                        geoGame.getPersonInitialPosition(player, person));
            }
        }

        phaseManager.change(selectMovimentSubPhase);
    }

    public void onAdd() {
    }

    public void onRemove() {
    }

    @Override
    public void update(long elapsedTime) {
        geoGame.getMap().update(elapsedTime);
        updateStatusDialog();

        if (MyGame.getInstance().keyPressed(KeyEvent.VK_ESCAPE)) {
            MyGame.getInstance().getPhaseManager().back();
        }

        for (Player player : geoGame.getPlayers()) {
            for (Person person : player.getPersons()) {
                person.update(elapsedTime);
            }
        }

        gameActionCursor.update(elapsedTime);
        phaseManager.getCurrentPhase().update(elapsedTime);
    }

    @Override
    public void render(Graphics2D g) {
        geoGame.getMap().render(g, mapPosition);
        statusDialog.render(g);
        phaseManager.getCurrentPhase().render(g);
        for (Player player : geoGame.getPlayers()) {
            for (Person person : player.getPersons()) {
                person.render(g);
            }
        }

    }

    private void changePersonsGameAction() {
        for (Player player : geoGame.getPlayers()) {
            for (Person person : player.getPersons()) {
                person.setCurrentGameAction(gameActionCursor.getSelected());
            }
        }
    }

    private void updateStatusDialog() {
        StringBuilder b = new StringBuilder();
//        b.append("Cursor: " + mapCursor.getCursor());
        statusDialog.setText(b.toString());
    }

    public void onExit() {
    }

    public void onEnter() {
    }

    public BattleGame getGeoGame() {
        return geoGame;
    }

    public Coordinate getMapPosition() {
        return mapPosition;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public void putPersonOnPosition(Person person, Coordinate personMapPosition) {
        person.getMapPosition().set(personMapPosition);
        person.getPosition().set(Map.getPersonPosition(mapPosition, personMapPosition));
    }
}
