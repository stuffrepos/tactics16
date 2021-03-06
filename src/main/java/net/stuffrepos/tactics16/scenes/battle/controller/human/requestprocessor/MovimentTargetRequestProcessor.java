package net.stuffrepos.tactics16.scenes.battle.controller.human.requestprocessor;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.MovimentTargetRequest;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.MapCheckedArea;
import net.stuffrepos.tactics16.scenes.battle.eventprocessors.RequestProcessor;
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

            @Override
            public void enter(GameContainer container, StateBasedGame game) throws SlickException {
                super.enter(container, game);
                mapCheckedArea = getScene().getVisualBattleMap().createMapCheckedArea(
                        Coordinate.list(event.getMovimentRange()), MAP_CHECKED_AREA_COLOR);
                selectedPersonPosition = Coordinate.fromMapCoordinate(event.getOriginalPosition());
                getScene().putPersonOnPosition(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson().getId()), selectedPersonPosition);

                getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson().getId()).getGameActionControl().advance(Job.GameAction.SELECTED);
                getScene().getVisualBattleMap().createMovimentMapCursor(                        
                        event.getOriginalPosition());
            }

            @Override
            public void leave(GameContainer container, StateBasedGame game) throws SlickException {
                getScene().getVisualBattleMap().finalizeMapCursor();
                mapCheckedArea.finalizeEntity();
                getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson().getId()).getGameActionControl().back();
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                if (MyGame.getInstance().keys().isPressed(GameKey.CONFIRM)) {
                    if (mapCheckedArea.inArea(getScene().getVisualBattleMap().getMapCursorPosition())) {
                        answer(getScene().getVisualBattleMap().getMapCursorPosition());
                    }
                } else if (MyGame.getInstance().keys().isPressed(GameKey.CANCEL)) {
                    answer(null);
                }
            }
        };
    }

    public static Class getObjectClass() {
        return MovimentTargetRequest.class;
    }
}
