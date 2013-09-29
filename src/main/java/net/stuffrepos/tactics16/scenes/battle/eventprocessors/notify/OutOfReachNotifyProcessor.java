package net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify;

import net.stuffrepos.tactics16.battleengine.events.OutOfReachNotify;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class OutOfReachNotifyProcessor extends EventProcessor<OutOfReachNotify> {

    private final Log log = LogFactory.getLog(OutOfReachNotifyProcessor.class);

    public OutOfReachNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final OutOfReachNotify event) {
        return new Phase() {
            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                if (log.isDebugEnabled()) {
                    log.debug("Person position: " + Coordinate.fromMapCoordinate(event.getPerson().getPosition()).toStringInt());
                    log.debug("Target position: " + Coordinate.fromMapCoordinate(event.getTargetPosition()).toStringInt());
                    log.debug("Range size: " + event.getRange().size());
                }
                getScene().stopCurrentEventPhase();
            }
        };
    }

    public static Class getObjectClass() {
        return OutOfReachNotify.class;
    }
}
