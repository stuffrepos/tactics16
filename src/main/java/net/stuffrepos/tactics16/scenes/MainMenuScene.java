package net.stuffrepos.tactics16.scenes;

import net.stuffrepos.tactics16.scenes.test.JobSpriteTester;
import net.stuffrepos.tactics16.scenes.test.AnimationSpriteTester;
import net.stuffrepos.tactics16.scenes.test.FontTesterScene;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.scenes.mapbuilder.MapBuilderScene;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.TextBox;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battleconfig.BattleConfigScene;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MainMenuScene extends Phase {

    private static final MainMenuScene instance = new MainMenuScene();
    private Text title;
    private TextBox keysHelp;
    private final Menu mainMenu;

    private MainMenuScene() {
        mainMenu = new Menu();
        mainMenu.addOption(new CommonMenuOption("Play") {
            @Override
            public void executeAction() {
                MyGame.getInstance().getPhaseManager().advance(new BattleConfigScene());

            }
        });

        mainMenu.addOption(new CommonMenuOption("Map Creator") {
            @Override
            public void executeAction() {
                MyGame.getInstance().getPhaseManager().advance(MapBuilderScene.getInstance());

            }
        });

        if (MyGame.getInstance().getOptions().isDebug()) {
            mainMenu.addOption(new CommonMenuOption("Text Font Tester") {
                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(FontTesterScene.getInstance());

                }
            });
            mainMenu.addOption(new CommonMenuOption("Job Sprite Tester") {
                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(JobSpriteTester.getInstance());

                }
            });
            mainMenu.addOption(new CommonMenuOption("Animation Tester") {
                @Override
                public void executeAction() {
                    MyGame.getInstance().getPhaseManager().advance(AnimationSpriteTester.getInstance());

                }
            });
            mainMenu.addOption(new CommonMenuOption("Message Box Test") {
                @Override
                public void executeAction() {
                    new MessageBox("Message Box Test", MyGame.getInstance().getScreenObject2D()).createPhase(
                            MyGame.getInstance().getPhaseManager());

                }
            });

        }

        mainMenu.addOption(new CommonMenuOption("Quit", GameKey.CANCEL) {
            @Override
            public void executeAction() {
                MyGame.getInstance().quit();
            }
        });

    }

    public static final MainMenuScene getInstance() {
        return instance;
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) throws SlickException {
        title = new Text();
        title.setFont(MyGame.FontType.MainTitle);
        title.setColor(PhaseTitle.DEFAULT_COLOR);
        title.setText("Tactics16");

        keysHelp = new TextBox();
        StringBuilder b = new StringBuilder();
        b.append("Keys Help\n");
        for (GameKey gameKey : GameKey.values()) {
            b.append(gameKey.toString());
            b.append(": ");
            boolean first = true;
            for (int key : MyGame.getInstance().keys().getKeys(gameKey)) {
                if (first) {
                    first = false;
                } else {
                    b.append(", ");
                }
                b.append(Input.getKeyName(key));
            }
            b.append('\n');
        }
        keysHelp.setText(b.toString());

        title.getPosition().setX(
                Layout.getCentralizedLeft(title));

        mainMenu.getPosition().setX(
                Layout.getCentralizedLeft(mainMenu));

        keysHelp.getPosition().setX(
                Layout.getCentralizedLeft(keysHelp));

        title.getPosition().setY(
                (Layout.getScreenHeight() -
                (title.getHeight() + mainMenu.getHeight() +
                keysHelp.getHeight() + Layout.OBJECT_GAP * 2)) / 2);

        mainMenu.getPosition().setY(
                Layout.getBottom(title) + Layout.OBJECT_GAP);

        keysHelp.getPosition().setY(
                Layout.getBottom(mainMenu) + Layout.OBJECT_GAP);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        mainMenu.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        mainMenu.render(g);
        title.render(g);
        keysHelp.render(g);
    }
}
