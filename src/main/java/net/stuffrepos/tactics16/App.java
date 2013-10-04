package net.stuffrepos.tactics16;

import net.stuffrepos.tactics16.scenes.MainMenuScene;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class App {

    public static void main(String[] args) throws SlickException, ParseException {
        Options options = new Options();
        options.addOption(null, "debug", false, "Show menu options for debug purposes");
        options.addOption(null, "data-path", true, "Path for alternative data");
        options.addOption(null, "test-battle", false, "Initialize game with a test battle");

        CommandLineParser parser = new PosixParser();
        MyGame.createInstance(new CommandLineOptions(parser.parse(options, args)));
        if (MyGame.getInstance().getOptions().isTestBattle()) {
            MyGame.getInstance().start(new TestBattleScene());
        } else {
            MyGame.getInstance().start(MainMenuScene.getInstance());
        }

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

        public boolean isTestBattle() {
            return cmd.hasOption("test-battle");
        }
    }
}
