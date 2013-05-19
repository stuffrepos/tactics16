package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.battleengine.events.ActionTargetRequest;
import net.stuffrepos.tactics16.components.MapCursor;

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

    private final static Log log = LogFactory.getLog(ActionTargetRequestProcessor.class);
    private static final int RANGE_MAP_CHECKED_COLOR = 0xFF0000;

    public ActionTargetRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ActionTargetRequest event) {
        return new Phase() {
            private MapCheckedArea mapCheckedArea;
            private Action selectedAction;
            private MapCursor targetCursor;

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                mapCheckedArea = getScene().getVisualBattleMap().createMapCheckedArea(
                        Coordinate.list(event.getActionRange()),
                        RANGE_MAP_CHECKED_COLOR);
                targetCursor = getScene().getVisualBattleMap().createMapCursor();
                targetCursor.getCursor().moveTo(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getMapPosition());
            }

            @Override
            public void leave(GameContainer container, StateBasedGame game) throws SlickException {
                targetCursor.finalizeEntity();
                mapCheckedArea.finalizeEntity();
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                    if (mapCheckedArea.inArea(targetCursor.getCursor().getPosition())) {
                        answer(targetCursor.getCursor().getPosition());
                    }
                }


                if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
                    answer(null);
                }
            }
        };
    }

    @Override
    public Class getEventClass() {
        return ActionTargetRequest.class;
    }
}
