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

        CommandLineParser parser = new PosixParser();
        MyGame.createInstance(new CommandLineOptions(parser.parse(options, args)));
        MyGame.getInstance().start(MainMenuScene.getInstance());
        //MyGame.getInstance().start(new TestBattleScene());
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
