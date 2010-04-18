package tactics16.scenes.battle;

import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.MyGame;
import tactics16.components.menu.CommonMenuOption;
import tactics16.components.menu.Menu;
import tactics16.phase.AbstractPhase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class OptionsSubPhase extends AbstractPhase {

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
    public void update(long elapsedTime) {
        menu.update(elapsedTime);
    }

    @Override
    public void render(Graphics2D g) {
        menu.render(g);
    }
}
