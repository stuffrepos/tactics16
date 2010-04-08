package tactics16;

import com.golden.gamedev.GameLoader;
import java.awt.Dimension;
import tactics16.scenes.MainMenuScene;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) {
        args = new String[]{"./src/main/resources/game-data"};
        MyGame.createInstance(args[0]);
        MyGame.getInstance().getPhaseManager().change(MainMenuScene.getInstance());
        GameLoader game = new GameLoader();
        game.setup(MyGame.getInstance(), new Dimension(640, 480), false);
        game.start();
    }
}
