package net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify;

import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.battleengine.events.PersonMoved;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import net.stuffrepos.tactics16.scenes.battle.personaction.PersonMovimentAnimation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonMovedProcessor extends EventProcessor<PersonMoved> {

    private final Log log = LogFactory.getLog(PersonMovedProcessor.class);

    public PersonMovedProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final PersonMoved event) {
        if (log.isDebugEnabled()) {
            log.debug(
                    String.format(
                    "Moved from %s to %s",
                    Coordinate.fromMapCoordinate(event.getMovimentTarget()).toStringInt(),
                    Coordinate.fromMapCoordinate(event.getMovimentTarget()).toStringInt()));
        }
        return new Phase() {
            private PersonMovimentAnimation moviment;

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                moviment = new PersonMovimentAnimation(
                        getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()),
                        getScene().getVisualBattleMap(),
                        event.getOriginalPosition(),
                        event.getMovimentTarget());
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                moviment.update(delta);
                if (moviment.isFinalized()) {
                    getScene().putPersonOnPosition(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()), moviment.getTerrainTarget());
                    getScene().stopCurrentEventPhase();
                }
            }

            @Override
            public void leave(GameContainer container, StateBasedGame game) throws SlickException {
                if (!moviment.isFinalized()) {
                }
            }
        };
    }

   public static Class getObjectClass() {
        return PersonMoved.class;
    }
}
