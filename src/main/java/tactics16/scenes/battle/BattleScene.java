package tactics16.scenes.battle;

import tactics16.scenes.battle.personaction.PersonActionSubPhase;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.TextDialog;
import tactics16.game.Action;
import tactics16.game.Coordinate;
import tactics16.game.Job;
import tactics16.phase.PhaseManager;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import tactics16.GameKey;
import tactics16.phase.AbstractPhase;
import tactics16.scenes.battle.effects.EffectsSubPhase;
import tactics16.scenes.battle.effects.EvadeSelector;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleScene extends AbstractPhase {

    // Layout
    public static final int MAP_GAP = Layout.OBJECT_GAP * 5;
    private TextDialog statusDialog = new TextDialog();
    // Phases
    private final PersonActionSubPhase personActionSubPhase = new PersonActionSubPhase(this);
    private final OptionsSubPhase optionsSubPhase = new OptionsSubPhase(this);
    private PhaseManager phaseManager = new PhaseManager();
    private VisualBattleMap visualBattleMap;
    private JobAnimationTest jobAnimationTest;

    public BattleScene(BattleGame battleGame) {
        this.visualBattleMap = new VisualBattleMap(battleGame);
        this.jobAnimationTest = new JobAnimationTest(battleGame);

        List<Job.GameAction> gameActionList = new ArrayList<Job.GameAction>();
        for (Job.GameAction gameAction : Job.GameAction.values()) {
            gameActionList.add(gameAction);
        }

        // Positions and Dimensions
        statusDialog.setWidth(200);
        statusDialog.getPosition().setXY(
                Layout.getScreenWidth() - Layout.OBJECT_GAP - statusDialog.getWidth(),
                Layout.OBJECT_GAP);
        getVisualBattleMap().getVisualMap().getPosition().setXY(MAP_GAP, MAP_GAP);

        // Persons Positions
        for (int player = 0; player < battleGame.getPlayers().size(); ++player) {
            for (int person = 0; person < battleGame.getPlayers().get(player).getPersons().size(); ++person) {
                putPersonOnPosition(
                        battleGame.getPlayers().get(player).getPersons().get(person),
                        battleGame.getPersonInitialPosition(player, person));
            }
        }

        phaseManager.change(personActionSubPhase);
    }

    @Override
    public void update(long elapsedTime) {
        updateStatusDialog();
        jobAnimationTest.update(elapsedTime);

        if (MyGame.getInstance().isKeyPressed(GameKey.OPTIONS)) {
            phaseManager.advance(optionsSubPhase);
        }

        for (Player player : getVisualBattleMap().getBattleGame().getPlayers()) {
            for (Person person : player.getPersons()) {
                person.update(elapsedTime);
            }
        }

        getVisualBattleMap().update(elapsedTime);
        phaseManager.getCurrentPhase().update(elapsedTime);
    }

    @Override
    public void render(Graphics2D g) {
        getVisualBattleMap().render(g);
        statusDialog.render(g);
        phaseManager.getCurrentPhase().render(g);
    }

    private void updateStatusDialog() {
        StringBuilder b = new StringBuilder();
        if (phaseManager.getCurrentPhase() != null) {
            b.append(phaseManager.getCurrentPhase().toString());
        }
        statusDialog.setText(b.toString());
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public void putPersonOnPosition(Person person, Coordinate personMapPosition) {
        person.getMapPosition().set(personMapPosition);
        person.getPosition().set(getVisualBattleMap().getVisualMap().getPersonPosition(personMapPosition));
    }

    public VisualBattleMap getVisualBattleMap() {
        return visualBattleMap;
    }

    public void toEffectSubPhase(BattleAction battleAction) {
        getPhaseManager().clear();
        getPhaseManager().change(new EffectsSubPhase(this, battleAction));
    }

    public void toPersonActionSubPhase() {
        getPhaseManager().clear();
        getPhaseManager().change(personActionSubPhase);
    }
}
