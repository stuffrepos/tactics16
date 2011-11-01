package net.stuffrepos.tactics16;

import net.stuffrepos.tactics16.scenes.MainMenuScene;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) throws SlickException {
        args = new String[]{"./src/main/resources/game-data"};
        MyGame.createInstance(args[0]);        
        MyGame.getInstance().start(MainMenuScene.getInstance());
    }
}
