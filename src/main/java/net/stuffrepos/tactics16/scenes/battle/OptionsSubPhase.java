package net.stuffrepos.tactics16.scenes.battle;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class OptionsSubPhase extends Phase {

    private final BattleScene parentScene;
    private Menu menu = new Menu(new CommonMenuOption("Back",GameKey.CANCEL) {

        @Override
        public void executeAction() {
            parentScene.getPhaseManager().back();
        }
    }, new CommonMenuOption("Exit Game") {

        @Override
        public void executeAction() {
            MyGame.getInstance().getPhaseManager().back();
        }
    });

    public OptionsSubPhase(BattleScene parentScene) {
        this.parentScene = parentScene;
        this.menu.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        menu.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        menu.render(g);
    }
}
