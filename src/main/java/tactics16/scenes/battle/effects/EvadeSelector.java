package tactics16.scenes.battle.effects;

import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.components.TextDialog;
import tactics16.components.menu.CommonMenuOption;
import tactics16.components.menu.Menu;
import tactics16.game.Coordinate;
import tactics16.game.Map;
import tactics16.scenes.battle.BattleAction;
import tactics16.scenes.battle.Person;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EvadeSelector {

    private Response response = null;
    private final Coordinate position = new Coordinate();
    private final TextDialog title = new TextDialog();
    private final Menu confirmMenu = new Menu(new CommonMenuOption("Yes") {

        @Override
        public void executeAction() {
            response = Response.YES;
        }
    }, new CommonMenuOption("No") {

        @Override
        public void executeAction() {
            response = Response.NO;
        }
    });

    public EvadeSelector(BattleAction battleAction, Person personTarget) {
        title.setText(
                String.format(
                "Evade? (Need %d AP)",
                battleAction.getAgentAgilityPoints() + battleAction.getAction().getAgility() -
                personTarget.getEvasiveness()));
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                title.getPosition().set(source);
                confirmMenu.getPosition().setXY(
                        title.getLeft(),
                        Layout.getBottomGap(title));
            }
        });

        position.setXY(
                personTarget.getPosition().getX() + Map.TERRAIN_SIZE,
                personTarget.getPosition().getY() - Map.TERRAIN_SIZE);
    }

    public void update(long elapsedTime) {
        confirmMenu.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        title.render(g);
        confirmMenu.render(g);
    }

    public boolean isFinalized() {
        return response != null;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Response getResponse() {
        return response;
    }

    public static enum Response {

        YES,
        NO
    }
}
