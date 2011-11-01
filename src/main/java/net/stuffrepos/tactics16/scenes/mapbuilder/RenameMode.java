package net.stuffrepos.tactics16.scenes.mapbuilder;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.Keyboard;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.util.javabasic.StringUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class RenameMode extends AbstractPhase {

    private PhaseTitle title;
    private Keyboard keyboard;
    private final MapBuilderScene parentScene;

    public RenameMode(MapBuilderScene parentScene) {
        this.parentScene = parentScene;
    }

    @Override
    public void onAdd() {

        title = parentScene.createModeTitle("Rename Map");

        keyboard = new Keyboard(parentScene.getMap().getName(), 32) {

            @Override
            public boolean confirmExit(boolean confirm, String text) {
                if (confirm == false) {
                    return true;
                } else {
                    if (StringUtil.isEmpty(text)) {
                        new MessageBox("Name is empty", keyboard).createPhase();
                        return false;
                    } else {
                        parentScene.getMap().setName(StringUtil.parseString(text));
                        return true;
                    }
                }
            }
        };
        keyboard.getPosition().setXY(
                Layout.getCentralizedLeft(keyboard),
                Layout.getBottomGap(title));

    }

    @Override
    public void update(long elapsedTime) {
        keyboard.update(elapsedTime);

        if (keyboard.isFinalized()) {
            parentScene.toMenuMode();
        }
    }

    @Override
    public void render(Graphics g) {
        title.render(g);
        keyboard.render(g);
    }
}
