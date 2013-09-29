package net.stuffrepos.tactics16.scenes.battle.controller.cpu;

import java.lang.reflect.InvocationTargetException;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.controller.PlayerController;
import net.stuffrepos.tactics16.util.ObjectProcessorFinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class CpuPlayerController implements PlayerController {

    private final static Log log = LogFactory.getLog(CpuPlayerController.class);
    private CpuCommand cpuCommand;
    private final CpuIa ia;
    ObjectProcessorFinder<BattleRequest, CpuRequestProcessor> eventProcessorFinder =
            new ObjectProcessorFinder<BattleRequest, CpuRequestProcessor>(
            "net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor",
            CpuRequestProcessor.class);

    public CpuPlayerController(CpuIa ia) {
        this.ia = ia;
    }

    public EventProcessor getEventProcessor(final BattleScene battleScene, final BattleRequest request) {
        log.debug("CPU received event: " + request.getClass().getSimpleName());
        Class<? extends CpuRequestProcessor> processorClass = eventProcessorFinder.getEventProcessorClass(request);
        try {
            return processorClass.getConstructor(
                    BattleScene.class,
                    CpuCommand.class).newInstance(
                    battleScene,
                    getCpuCommand(battleScene, request));
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private CpuCommand getCpuCommand(BattleScene battleScene, BattleRequest request) {
        if (request instanceof MovimentTargetRequest) {
            cpuCommand = ia.buildCpuCommand(
                    battleScene.getVisualBattleMap().getBattleGame().getEngine(),
                    ((MovimentTargetRequest) request).getPerson().getId());
            if (log.isDebugEnabled()) {
                log.debug(
                        String.format(
                        "Moviment Target: %s, Action Target: %s, Action: %s",
                        Coordinate.fromMapCoordinate(cpuCommand.getMovimentTarget()).toStringInt(),
                        cpuCommand.getActionTarget() == null ? "null" : Coordinate.fromMapCoordinate(cpuCommand.getActionTarget()).toStringInt(),
                        cpuCommand.getAction() == null ? "null" : cpuCommand.getAction().getName()));
            }
        }

        return cpuCommand;
    }
}
