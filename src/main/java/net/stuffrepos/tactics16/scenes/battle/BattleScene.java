package net.stuffrepos.tactics16.scenes.battle;

import org.newdawn.slick.Color;
import net.stuffrepos.tactics16.scenes.battle.personaction.PersonActionSubPhase;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.phase.PhaseManager;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.battlegameengine.Map;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battlegameengine.Monitor;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleGameEvents.Event;
import net.stuffrepos.tactics16.scenes.battle.effects.EffectsSubPhase;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleScene extends Phase {

    // Layout
    public static final int MAP_GAP = Layout.OBJECT_GAP * 5;
    private static java.util.Map<Class, EventInitializer> eventInitializer;
    private TextBox statusDialog = new TextBox();
    // Phases
    private final PersonActionSubPhase personActionSubPhase = new PersonActionSubPhase(this);
    private final OptionsSubPhase optionsSubPhase = new OptionsSubPhase(this);
    private PhaseManager phaseManager = new PhaseManager();
    private VisualBattleMap visualBattleMap;
    private JobAnimationTest jobAnimationTest;
    private int currentPlayer;
    private Set<Person> usedPersons;
    private Thread engineThread;
    private Queue<BattleGameEvents.Event> eventQueue = new LinkedList<BattleGameEvents.Event>();
    private boolean requestMovimentTarget = false;
    private MapCoordinate requestMovimentTargetReturn = null;

    static {        
        eventInitializer = new HashMap<Class, EventInitializer>();
        eventInitializer.put(BattleGameEvents.NewTurnNotifiy.class, new EventInitializer<BattleGameEvents.NewTurnNotifiy>() {
            public Phase init(Event event) {
                return new Phase() {
                };                
            }
        });
    }
    private Phase currentEventPhase = null;

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
    public void initResources(GameContainer container, StateBasedGame game) {
        currentPlayer = -1;
        //newTurn();
        engineThread = new Thread() {
            @Override
            public void run() {
                getVisualBattleMap().getBattleGame().getBattleGameEngine().run(
                        new Monitor() {
                            public void notifyNewTurn(int currentTurn) {
                                eventQueue.add(new BattleGameEvents.NewTurnNotifiy(currentTurn));
                            }

                            public void notifyPersonsActOrderDefined(List<Integer> persons) {
                                eventQueue.add(new BattleGameEvents.PersonsActOrderDefinedNotifiy(persons));
                            }

                            public void notifySelectedPerson(Integer selectedPerson) {
                                eventQueue.add(new BattleGameEvents.SelectedPersonNotify(selectedPerson));
                            }

                            public MapCoordinate requestMovimentTarget(Integer person, Map map, MapCoordinate originalPosition, Collection<MapCoordinate> movimentRange) {
                                requestMovimentTarget = true;
                                requestMovimentTargetReturn = null;
                                getVisualBattleMap().getBattleGame().getBattleGameEngine().waitRequest();
                                return requestMovimentTargetReturn;
                            }

                            public void notifyOutOfReachMoviment(Integer person, MapCoordinate coordinate) {
                                eventQueue.add(new BattleGameEvents.OutOfReachMovimentNotifiy(person, coordinate));
                            }

                            public void notifyPersonMoved(Integer person, MapCoordinate originalPosition, MapCoordinate movimentTarget) {
                                eventQueue.add(new BattleGameEvents.PersonMovedNotify(person, originalPosition, movimentTarget));
                            }

                            public Action requestPersonAction(Integer person, Map map, MapCoordinate personPosition, java.util.Map<Action, Boolean> classifyPersonActions) {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void notifyMovimentCancelled(Integer person, MapCoordinate originalPosition, MapCoordinate movimentTarget) {
                                eventQueue.add(new BattleGameEvents.MovimentCancelledNotifiy(person, originalPosition, movimentTarget));
                            }

                            public void notifySelectedAction(Integer person, Action selectedAction) {
                                eventQueue.add(new BattleGameEvents.SelectedActionNotify(person, selectedAction));
                            }

                            public void notifyNotEnabledAction(Integer person, Action selectedAction) {
                                eventQueue.add(new BattleGameEvents.NotEnabledActionNotify(person, selectedAction));
                            }

                            public void notifyChooseActionCancelled(Integer person, Action selectedAction) {
                                eventQueue.add(new BattleGameEvents.ChooseActionCancelled(person, selectedAction));
                            }

                            public void notifyChoosedTarget(Integer person, MapCoordinate actionTarget) {
                                eventQueue.add(new BattleGameEvents.ChoosedTarget(person, actionTarget));
                            }

                            public void notifyOutOfReach(Integer person, Action selectedAction) {
                                eventQueue.add(new BattleGameEvents.OutOfReachNotify(person, selectedAction));
                            }

                            public MapCoordinate requestActionTarget(Integer person, Map map, MapCoordinate get, Action selectedAction, Collection<MapCoordinate> actionRange) {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public boolean requestActConfirm(Integer person, Action selectedAction, MapCoordinate actionTarget, Collection<MapCoordinate> actRay, Collection<Integer> affectedPersons) {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void notifyConfirmCancelled(Integer person, Action selectedAction, MapCoordinate actionTarget) {
                                eventQueue.add(new BattleGameEvents.ConfirmCancelledNotify(person, selectedAction, actionTarget));
                            }

                            public void notifyPerformedAction(Integer agentPerson, Action action,
                                    MapCoordinate target, Collection<MapCoordinate> buildActionReachRay,
                                    Collection<Integer> affectedPersons, int agentLostSpecialPoints,
                                    int agentLostHealthPoints) {
                                eventQueue.add(new BattleGameEvents.PerformedActionNotify(
                                        agentPerson,
                                        action,
                                        target,
                                        buildActionReachRay,
                                        affectedPersons,
                                        agentLostSpecialPoints,
                                        agentLostHealthPoints));
                            }

                            public void notifyActionAffectedPerson(Integer affectedPerson, boolean hits, int damage, boolean affectedPersonIsAlive) {
                                eventQueue.add(new BattleGameEvents.ActionAffectedPersonNotify(affectedPerson,
                                        hits, damage, affectedPersonIsAlive));
                            }

                            public void notifyWiningPlayer(Integer winnerPlayer) {
                                eventQueue.add(new BattleGameEvents.WinningPlayerNotify(winnerPlayer));
                            }
                        });
            }
        };

        engineThread.start();        
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        updateStatusDialog();
        jobAnimationTest.update(delta);

        if (MyGame.getInstance().isKeyPressed(GameKey.OPTIONS)) {
            phaseManager.advance(optionsSubPhase);
        }

        for (Player player : getVisualBattleMap().getBattleGame().getPlayers()) {
            for (Person person : player.getPersons()) {
                person.update(delta);
            }
        }

        getVisualBattleMap().update(delta);

        if (currentEventPhase == null) {
            BattleGameEvents.Event event = eventQueue.poll();
            if (event != null) {
                currentEventPhase = eventInitializer.get(event.getClass()).init(event);
            }
        }

        if (currentEventPhase != null) {
            currentEventPhase.update(container, game, delta);
        }

        //phaseManager.getCurrentPhase().update(container, game, delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        getVisualBattleMap().render(g);
        statusDialog.render(g);

        if (currentEventPhase != null) {
            currentEventPhase.render(container, game, g);
        }
        //phaseManager.getCurrentPhase().render(container, game, g);
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
                ColorUtil.transparent(getCurrentPlayer().getDefaultColor(), 0.8f)).setForegroundColor(
                Color.white).createPhase(phaseManager);
    }

    private void initEvent(Event event) {
        eventInitializer.get(event.getClass()).init(event);
    }

    private interface EventInitializer<EventType extends BattleGameEvents.Event> {

        public Phase init(BattleGameEvents.Event event);
    }
}
