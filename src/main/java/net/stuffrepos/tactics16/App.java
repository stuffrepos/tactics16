package net.stuffrepos.tactics16;

import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.MainMenuScene;
import net.stuffrepos.tactics16.scenes.battle.BattleGame;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) throws SlickException {
        if (args.length < 1) {
            args = new String[]{"./src/main/resources/game-data"};
        }        
        MyGame.createInstance(args[0]);
        MyGame.getInstance().start(MainMenuScene.getInstance());
        //MyGame.getInstance().start(bootBattleScene());
    }

    public static BattleScene buildBattleScene() {
        return new BattleScene(battleGame());

    }

    private static BattleGame battleGame() {
        BattleGame battleGame = new BattleGame(buildMap());
        for (Player player : battleGame.getPlayers()) {
            player.getPersons().add(createPerson(player));
        }
        return battleGame;
    }

    private static Map buildMap() {
        Map map = new Map("Direct", 8, 8);
        map.setPersonInitialPosition(0, new Coordinate(0, 0));
        map.setPersonInitialPosition(1, new Coordinate(7, 7));
        return map;
    }

    private static Person createPerson(Player player) {
        return new Person(player, "Person " + (1 + (int) (Math.random() * 98)), getJob());
    }

    private static Job getJob() {
        for (Job job : MyGame.getInstance().getLoader().getJobs()) {
            return job;
        }

        throw new RuntimeException("No job found");
    }

    private static Phase bootBattleScene() {
        return new Phase() {
            @Override
            public void init(GameContainer container, StateBasedGame game) throws SlickException {
                super.init(container, game);
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                MyGame.getInstance().getPhaseManager().change(buildBattleScene());                
            }
        };
    }
}
