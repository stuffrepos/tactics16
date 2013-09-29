package net.stuffrepos.tactics16.scenes.battle.controller.cpu.requestprocessor;

import java.util.Random;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuRequestProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MovimentTargetRequestProcessor extends CpuRequestProcessor<MovimentTargetRequest, Coordinate> {

    public MovimentTargetRequestProcessor(final BattleScene battleScene, CpuCommand cpuCommand) {
        super(battleScene, cpuCommand);
    }

    public static Class getObjectClass() {
        return MovimentTargetRequest.class;
    }

    @Override
    protected Coordinate buildAnswer(MovimentTargetRequest event) {
        return Coordinate.fromMapCoordinate(getCpuCommand().getMovimentTarget());
    }
}
