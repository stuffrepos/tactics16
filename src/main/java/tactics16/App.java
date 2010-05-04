package tactics16;

import tactics16.scenes.MainMenuScene;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) {
        args = new String[]{"./src/main/resources/game-data"};
        MyGame.createInstance(args[0]);        
        MyGame.getInstance().start(MainMenuScene.getInstance());
    }
}
