package net.stuffrepos.tactics16.scenes.battle;

import java.lang.reflect.InvocationTargetException;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.EventProcessorFinder;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.RequestProcessor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.BattleEvent;
import net.stuffrepos.tactics16.battleengine.BattleNotify;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.Monitor;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import net.stuffrepos.tactics16.scenes.MainMenuScene;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleScene extends Phase {

    // Layout
    public static final int MAP_GAP = Layout.OBJECT_GAP * 5;
    private final EventProcessorFinder notifyEventProcessorFinder;
    // Phases    
    private final OptionsSubPhase optionsSubPhase = new OptionsSubPhase(this);
    private PhaseManager phaseManager = new PhaseManager();
    private VisualBattleMap visualBattleMap;
    private JobAnimationTest jobAnimationTest;
    private int currentPlayer;
    private Thread engineThread;
    private Queue<BattleEvent> eventQueue = new LinkedList<BattleEvent>();
    private static final Log log = LogFactory.getLog(BattleScene.class);
    private RequestProcessor currentRequestProcessor = null;
    private boolean currentEventPhaseRunning = false;

    public BattleScene(BattleGame battleGame) {
        this.notifyEventProcessorFinder = new EventProcessorFinder<EventProcessor>(
                "net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify",
                EventProcessor.class) {
            @Override
            protected EventProcessor instantiateProcessor(Class<? extends EventProcessor> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
                return clazz.getConstructor(BattleScene.class).newInstance(BattleScene.this);
            }
        };
        this.visualBattleMap = new VisualBattleMap(battleGame);
        this.jobAnimationTest = new JobAnimationTest(battleGame);

        List<Job.GameAction> gameActionList = new ArrayList<Job.GameAction>();
        for (Job.GameAction gameAction : Job.GameAction.values()) {
            gameActionList.add(gameAction);
        }

        // Positions and Dimensions        
        getVisualBattleMap().getVisualMap().getPosition().set(
                Layout.getCentralizedOnObject2D(
                Layout.getScreenObject2D(), getVisualBattleMap().getVisualMap()));

        // Persons Positions        
        for (int player = 0; player < battleGame.getPlayerCount(); ++player) {
            for (int person = 0; person < battleGame.getPlayer(player).getPersons().size(); ++person) {
                putPersonOnPosition(
                        battleGame.getPlayer(player).getPerson(person),
                        battleGame.getPersonInitialPosition(player, person));
            }
        }
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {
        currentPlayer = -1;
        //newTurn();
        engineThread = new Thread() {
            @Override
            public void run() {
                getVisualBattleMap().getBattleGame().getEngine().run(
                        new Monitor() {
                    public void notify(BattleNotify notify) {
                        eventQueue.add(notify);
                    }

                    public <T> T request(BattleRequest<T> request) {
                        eventQueue.add(request);
                        log.debug("Engine will be blocked by " + request.getClass().getSimpleName());
                        getVisualBattleMap().getBattleGame().getEngine().waitRequest();
                        log.debug("Engine was unblocked by " + request.getClass().getSimpleName());
                        assert currentRequestProcessor != null;
                        T answer = (T) currentRequestProcessor.getAnswer();
                        currentRequestProcessor = null;
                        return answer;
                    }
                });
            }
        };

        engineThread.start();
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
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

        if (!currentEventPhaseRunning) {
            Phase currentEventPhase = nextEventPhase();
            if (currentEventPhase != null) {
                currentEventPhaseRunning = true;
                phaseManager.change(currentEventPhase);
            }
        }

        if (phaseManager.getCurrentPhase() != null) {
            phaseManager.getCurrentPhase().update(container, game, delta);
        }

        if (BattleEngine.State.Finalized.equals(getVisualBattleMap().getBattleGame().getEngine().getState())
                && !currentEventPhaseRunning
                && eventQueue.isEmpty()) {
            MyGame.getInstance().getPhaseManager().change(MainMenuScene.getInstance());
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        getVisualBattleMap().render(g);

        if (phaseManager.getCurrentPhase() != null) {
            phaseManager.getCurrentPhase().render(container, game, g);
        }
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public void putPersonOnPosition(Person person, Coordinate personMapPosition) {
        person.getPosition().set(getVisualBattleMap().getVisualMap().getPersonPosition(personMapPosition));
    }

    public VisualBattleMap getVisualBattleMap() {
        return visualBattleMap;
    }

    public Player getCurrentPlayer() {
        return getVisualBattleMap().getBattleGame().getPlayer(currentPlayer);
    }

    public void stopCurrentEventPhase() {
        currentEventPhaseRunning = false;
        if (currentRequestProcessor != null) {
            getVisualBattleMap().getBattleGame().getEngine().resumeRequest();
        }
    }

    private Phase nextEventPhase() {
        BattleEvent event = eventQueue.poll();
        if (event != null) {
            if (log.isDebugEnabled()) {
                log.debug("Event polled: " + event.getClass().getSimpleName());
            }

            EventProcessor eventProcessor;
            if (event instanceof BattleNotify) {
                eventProcessor = notifyEventProcessorFinder.getEventProcessor(event);
            } else if (event instanceof BattleRequest) {
                BattleRequest request = (BattleRequest) event;
                eventProcessor = visualBattleMap.getBattleGame().getPlayer(request.getPlayer()).getControl().getEventProcessor(this, request);
            } else {
                throw new RuntimeException("Unknown battle event class: " + event.getClass());
            }

            if (log.isDebugEnabled()) {
                log.debug("Event processor found: " + eventProcessor.getClass().getSimpleName());
            }

            if (eventProcessor instanceof RequestProcessor) {
                currentRequestProcessor = (RequestProcessor) eventProcessor;
            }

            return eventProcessor.init(event);
        } else {
            return null;
        }
    }

    public void putPersonOnPosition(Integer person, MapCoordinate originalPosition) {
        putPersonOnPosition(
                getVisualBattleMap().getBattleGame().getPerson(person),
                Coordinate.fromMapCoordinate(originalPosition));
    }
}
