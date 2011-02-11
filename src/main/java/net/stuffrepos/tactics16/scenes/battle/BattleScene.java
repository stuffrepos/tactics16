package net.stuffrepos.tactics16.scenes.battle;

import java.awt.Color;
import net.stuffrepos.tactics16.scenes.battle.personaction.PersonActionSubPhase;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.phase.PhaseManager;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.scenes.battle.effects.EffectsSubPhase;
import net.stuffrepos.tactics16.util.image.ColorUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleScene extends AbstractPhase {

    // Layout
    public static final int MAP_GAP = Layout.OBJECT_GAP * 5;
    private TextBox statusDialog = new TextBox();
    // Phases
    private final PersonActionSubPhase personActionSubPhase = new PersonActionSubPhase(this);
    private final OptionsSubPhase optionsSubPhase = new OptionsSubPhase(this);
    private PhaseManager phaseManager = new PhaseManager();
    private VisualBattleMap visualBattleMap;
    private JobAnimationTest jobAnimationTest;
    private int currentPlayer;
    private Set<Person> usedPersons;

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
        getVisualBattleMap().getVisualMap().getPosition().set(
                Layout.getCentralizedOnObject2D(
                Layout.getScreenObject2D(), getVisualBattleMap().getVisualMap()));

        // Persons Positions
        for (int player = 0; player < battleGame.getPlayerCount(); ++player) {
            for (int person = 0; person < battleGame.getPlayer(player).getPersons().size(); ++person) {
                putPersonOnPosition(
                        battleGame.getPlayer(player).getPersons().get(person),
                        battleGame.getPersonInitialPosition(player, person));
            }
        }

        phaseManager.change(personActionSubPhase);
    }

    @Override
    public void onAdd() {
        currentPlayer = -1;
        newTurn();
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
        usedPersons.add(battleAction.getAgent());
        battleAction.getAgent().getGameActionControl().set(Job.GameAction.USED);

        getPhaseManager().clear();
        if (battleAction.getAction() == null) {
            toPersonActionSubPhase();
        } else {
            getPhaseManager().change(new EffectsSubPhase(this, battleAction));
        }
    }

    public void toPersonActionSubPhase() {
        toPersonActionSubPhase(null);
    }

    public void toPersonActionSubPhase(BattleActionResult battleActionResult) {
        if (battleActionResult != null) {
            battleActionResult.applyResults();
        }
        getPhaseManager().clear();
        getPhaseManager().change(personActionSubPhase);
        if (usedPersons.size() == getCurrentPlayer().getPersons().size()) {
            newTurn();
        }
    }

    public boolean isUsed(Person person) {
        return usedPersons.contains(person);
    }

    public Player getCurrentPlayer() {
        return getVisualBattleMap().getBattleGame().getPlayer(currentPlayer);
    }

    public void newTurn() {
        currentPlayer = (currentPlayer + 1) % getVisualBattleMap().getBattleGame().getPlayerCount();
        if (usedPersons != null) {
            for (Person person : usedPersons) {
                person.getGameActionControl().set(Job.GameAction.STOPPED);
            }
        }
        usedPersons = new HashSet<Person>();
        new MessageBox(
                getCurrentPlayer().getName() + "'s Turn",
                getVisualBattleMap().getVisualMap(),
                1000).setBackgroundColor(
                ColorUtil.transparent(getCurrentPlayer().getDefaultColor(),0.8f)).setForegroundColor(
                Color.WHITE).createPhase(phaseManager);
    }
}
