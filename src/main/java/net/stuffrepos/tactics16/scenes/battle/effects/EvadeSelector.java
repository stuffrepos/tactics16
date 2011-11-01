package net.stuffrepos.tactics16.scenes.battle.effects;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battle.BattleAction;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EvadeSelector {

    private Boolean response = null;
    private final Coordinate position = new Coordinate();
    private final TextBox title = new TextBox();
    private final Menu confirmMenu = new Menu(new CommonMenuOption("Yes") {

        @Override
        public void executeAction() {
            response = true;
        }
    }, new CommonMenuOption("No") {

        @Override
        public void executeAction() {
            response = false;
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

    public void render(Graphics g) {
        title.render(g);
        confirmMenu.render(g);
    }

    public boolean isFinalized() {
        return response != null;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Boolean getResponse() {
        return response;
    }
}
