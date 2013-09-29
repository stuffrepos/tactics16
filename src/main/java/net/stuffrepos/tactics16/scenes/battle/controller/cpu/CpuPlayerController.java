package net.stuffrepos.tactics16.scenes.battle.controller.cpu;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.EventProcessorFinder;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuPlayerController implements PlayerController {

    private CpuCommand cpuCommand;

    public EventProcessor getEventProcessor(final BattleScene battleScene, final BattleRequest request) {
        return new EventProcessorFinder<CpuRequestProcessor>("net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor", CpuRequestProcessor.class) {
            @Override
            protected CpuRequestProcessor instantiateProcessor(Class<? extends CpuRequestProcessor> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
                return clazz.getConstructor(BattleScene.class, CpuCommand.class).newInstance(battleScene, getCpuCommand(battleScene, request));
            }
        }.getEventProcessor(request);
    }

    private CpuCommand getCpuCommand(BattleScene battleScene, BattleRequest request) {
        if (request instanceof MovimentTargetRequest) {
            MovimentTargetRequest movimentRequest = (MovimentTargetRequest) request;
            MapCoordinate movimentTarget = random(
                    battleScene.getVisualBattleMap().getBattleGame().getEngine().buildMovimentRange(movimentRequest.getPerson().getId()));
            Action action = randomAction(
                    battleScene.getVisualBattleMap().getBattleGame().getEngine(),
                    movimentRequest.getPerson().getId());
            MapCoordinate actionTarget = null;
            if (action != null) {
                actionTarget = random(
                        battleScene.getVisualBattleMap().getBattleGame().getEngine().buildActionRange(
                        movimentRequest.getPerson().getId(),
                        action,
                        movimentTarget));
            }


            cpuCommand = new CpuCommand(movimentTarget, action, actionTarget);
        }

        return cpuCommand;
    }

    private Action randomAction(BattleEngine engine, int personId) {
        Map<Action, Boolean> classifiedActions = engine.classifyPersonActions(personId);
        List<Action> actions = new ArrayList<Action>(classifiedActions.size());
        for (Map.Entry<Action, Boolean> e : classifiedActions.entrySet()) {
            if (e.getValue()) {
                actions.add(e.getKey());
            }
        }
        actions.add(null);
        return random(actions);
    }

    private <T> T random(Collection<T> coordinates) {
        Object[] targets = coordinates.toArray(new Object[0]);
        return (T) targets[new Random().nextInt(targets.length)];
    }
}
