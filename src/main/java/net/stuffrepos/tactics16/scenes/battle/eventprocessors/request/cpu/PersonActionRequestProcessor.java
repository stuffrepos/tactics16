package net.stuffrepos.tactics16.scenes.battle.eventprocessors.request.cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine.SelectedAction;

import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.PersonActionRequest;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.ObjectMenuOption;
import net.stuffrepos.tactics16.game.Coordinate;

import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.RequestProcessor;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionRequestProcessor extends RequestProcessor<PersonActionRequest, SelectedAction> {

    public PersonActionRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final PersonActionRequest event) {
        return new Phase() {
            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                List<Action> actions = new ArrayList<Action>(event.getClassifyPersonActions().size());
                for (Entry<Action, Boolean> e : event.getClassifyPersonActions().entrySet()) {
                    if (e.getValue()) {
                        actions.add(e.getKey());
                    }
                }
                actions.add(null);
                int randomIndex = new Random().nextInt(actions.size());
                answer(new SelectedAction(actions.get(randomIndex)));
            }
        };
    }

    @Override
    public Class getEventClass() {
        return PersonActionRequest.class;
    }
}
