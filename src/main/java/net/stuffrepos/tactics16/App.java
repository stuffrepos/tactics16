package net.stuffrepos.tactics16;

import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.MainMenuScene;
import net.stuffrepos.tactics16.scenes.battle.BattleGame;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battleconfig.ControllerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.HumanControllerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.PersonToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.PlayerToBattle;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) throws SlickException, ParseException {
        Options options = new Options();
        options.addOption(null, "debug", false, "Show menu options for debug purposes");
        options.addOption(null, "data-path", true, "Path for alternative data");

        CommandLineParser parser = new PosixParser();
        MyGame.createInstance(new CommandLineOptions(parser.parse(options, args)));
        MyGame.getInstance().start(MainMenuScene.getInstance());
        //MyGame.getInstance().start(bootBattleScene());
    }

    public static BattleScene buildBattleScene() {
        return new BattleScene(battleGame());

    }

    private static BattleGame battleGame() {
        Map map = buildMap();
        return new BattleGame(map, createPlayers(map));
    }

    private static Map buildMap() {
        Map map = new Map("Direct", 8, 8);
        map.setPersonInitialPosition(0, new Coordinate(0, 0));
        map.setPersonInitialPosition(1, new Coordinate(7, 7));
        return map;
    }

    private static List<PlayerToBattle> createPlayers(Map map) {
        List<PlayerToBattle> players = new LinkedList<PlayerToBattle>();
        for (int playerId = 0; playerId < map.getPlayerCount(); ++playerId) {
            PlayerToBattle player = new PlayerToBattle(PlayerConfig.getPlayer(playerId), new HumanControllerToBattle(), "PlayerConfig " + (playerId + 1));
            player.addPerson(createPerson(player));
            players.add(player);
        }
        return players;
    }

    private static PersonToBattle createPerson(PlayerToBattle player) {
        return new PersonToBattle(player, "Person " + (1 + (int) (Math.random() * 98)), getJob());        
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

    private static class CommandLineOptions implements MyGameOptions {

        private final CommandLine cmd;

        private CommandLineOptions(CommandLine cmd) {
            this.cmd = cmd;
        }

        public String getDataPath() {
            return cmd.getOptionValue("data-path", "./src/main/resources/game-data");
        }

        public boolean isDebug() {
            return cmd.hasOption("debug");
        }
    }
}
