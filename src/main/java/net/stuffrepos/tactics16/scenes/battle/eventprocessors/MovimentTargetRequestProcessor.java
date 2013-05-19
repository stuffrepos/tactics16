package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battlegameengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.game.Job;
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
public class MovimentTargetRequestProcessor extends RequestProcessor<MovimentTargetRequest, Coordinate> {

    private final static Log log = LogFactory.getLog(MovimentTargetRequestProcessor.class);
    private static final int MAP_CHECKED_AREA_COLOR = 0x0000FF;

    public MovimentTargetRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final MovimentTargetRequest event) {
        return new Phase() {
            private MapCheckedArea mapCheckedArea;
            private Coordinate selectedPersonPosition;
            private MapCursor movimentCursor;

            @Override
            public void enter(GameContainer container, StateBasedGame game) throws SlickException {
                super.enter(container, game);
                mapCheckedArea = getScene().getVisualBattleMap().createMapCheckedArea(
                        Coordinate.list(event.getMovimentRange()), MAP_CHECKED_AREA_COLOR);
                movimentCursor = getScene().getVisualBattleMap().createMapCursor();
                selectedPersonPosition = Coordinate.fromMapCoordinate(event.getOriginalPosition());
                getScene().putPersonOnPosition(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()), selectedPersonPosition);

                getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getGameActionControl().advance(Job.GameAction.SELECTED);
                movimentCursor.getCursor().moveTo(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getMapPosition());
            }

            @Override
            public void leave(GameContainer container, StateBasedGame game) throws SlickException {                
                movimentCursor.finalizeEntity();
                mapCheckedArea.finalizeEntity();                
                getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getGameActionControl().back();
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                    if (mapCheckedArea.inArea(movimentCursor.getCursor().getPosition())) {
                        answer(movimentCursor.getCursor().getPosition());
                    }
                } else if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
                    answer(null);
                }
            }
        };
    }

    @Override
    public Class getEventClass() {
        return MovimentTargetRequest.class;
    }
}
