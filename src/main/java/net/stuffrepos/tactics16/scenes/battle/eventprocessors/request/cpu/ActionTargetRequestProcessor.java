package net.stuffrepos.tactics16.scenes.battle.eventprocessors.request.cpu;

import java.util.Random;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.events.ActionTargetRequest;

import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.game.Coordinate;

import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.MapCheckedArea;
import net.stuffrepos.tactics16.scenes.battle.RequestProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActionTargetRequestProcessor extends RequestProcessor<ActionTargetRequest, MapCoordinate> {

    public ActionTargetRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ActionTargetRequest event) {
        return new Phase() {
            private MapCheckedArea mapCheckedArea;

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                MapCoordinate[] targets = event.getActionRange().toArray(new MapCoordinate[0]);
                int positionIndex = new Random().nextInt(targets.length);
                MapCoordinate target = targets[positionIndex];
                answer(Coordinate.fromMapCoordinate(target));
            }
        };
    }

    @Override
    public Class getEventClass() {
        return ActionTargetRequest.class;
    }
}
