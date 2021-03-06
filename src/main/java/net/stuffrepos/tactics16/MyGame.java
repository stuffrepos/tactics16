package net.stuffrepos.tactics16;

import net.stuffrepos.tactics16.datamanager.DataManager;
import net.stuffrepos.tactics16.phase.PhaseManager;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.EnumMap;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.Transition;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MyGame {

    public static enum FontType {

        Common,
        MainTitle,
        PhaseTitle,
        Message
    }
    private static final String COMMON_FONT_NAME = "Purisa";
    private static final String TITLE_FONT_NAME = "Purisa";
    private static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(800, 600);
    private static MyGame instance;
    AppGameContainer app;
    private GameContainer gameContainer;
    private StateBasedGame game = new StateBasedGame("Tactics16") {

        @Override
        public void initStatesList(GameContainer container) throws SlickException {
            MyGame.this.initResources(container);
        }

        @Override
        public void addState(GameState state) {
            if (this.getState(state.getID()) == null) {
                try {
                    super.addState(state);
                    state.init(gameContainer, this);
                } catch (SlickException ex) {
                    throw new RuntimeException();
                }
            }
        }

        @Override
        protected void preUpdateState(GameContainer container, int delta) throws SlickException {
            super.preUpdateState(container, delta);
            keyMapping.preUpdate(container, delta);
        }
    };
    private DataManager loader;
    private PhaseManager phaseManager = new PhaseManager(true);
    private KeyMapping keyMapping;
    private EnumMap<FontType, org.newdawn.slick.Font> fonts;
    private Object2D screenObject2D = new Object2D() {
        public int getTop() {
            return 0;
        }

        public int getLeft() {
            return 0;
        }

        public int getWidth() {
            return MyGame.this.getWidth();
        }

        public int getHeight() {
            return MyGame.this.getHeight();
        }
    };
    private final MyGameOptions options;

    public MyGameOptions getOptions() {
        return options;
    }

    public static MyGame getInstance() {
        return instance;
    }

    public static void createInstance(MyGameOptions options) {
        if (instance == null) {
            try {
                instance = new MyGame(options);
            } catch (SlickException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private Phase initialPhase;
    private PhaseManager.SubPhaseManager mainPhaseManager = new PhaseManager.SubPhaseManager() {

        public void onChange(Phase phase, Phase previousPhase, Transition leaveTransition, Transition enterTransation) throws SlickException {
            MyGame.getInstance().enterGameStateState(phase, leaveTransition, enterTransation);
        }
    };
    private PhaseManager.SubPhaseManager noMainPhaseManager = new PhaseManager.SubPhaseManager() {

        public void onChange(Phase phase, Phase previousPhase, Transition leaveTransition, Transition enterTransation) throws SlickException {
            if (previousPhase != null) {
                previousPhase.leave(gameContainer, game);
            }

            if (phase != null) {
                phase.init(gameContainer, game);
                phase.enter(gameContainer, game);
            }
        }
    };

    private MyGame(MyGameOptions options) throws SlickException {
        this.options = options;
        loader = new DataManager(new File(options.getDataPath()));
    }

    private void initResources(GameContainer gameContainer) throws SlickException {
        fonts = new EnumMap<FontType, org.newdawn.slick.Font>(FontType.class);
        fonts.put(FontType.Common, createUnicodeFont(COMMON_FONT_NAME, Font.BOLD, 16));
        fonts.put(FontType.Message, createUnicodeFont(MyGame.COMMON_FONT_NAME, Font.BOLD, 20));
        fonts.put(FontType.MainTitle, createUnicodeFont(TITLE_FONT_NAME, Font.BOLD, 48));
        fonts.put(FontType.PhaseTitle, createUnicodeFont(TITLE_FONT_NAME, Font.BOLD, 28));        
        this.gameContainer = gameContainer;
        this.keyMapping = new KeyMapping();
        loader.loadDirectory(loader.getDataDirectory());
    }

    private UnicodeFont createUnicodeFont(String name, int style, int size) throws SlickException {
        UnicodeFont font = new UnicodeFont(new Font(name, style, size));
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect());
        font.loadGlyphs();
        return font;
    }

    public void quit() {
        if (gameContainer != null) {
            gameContainer.exit();
        }
    }

    public DataManager getLoader() {
        return this.loader;
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    public KeyMapping keys() {
        return keyMapping;
    }

    public org.newdawn.slick.Font getFont(FontType fontType) {
        return fonts.get(fontType);
    }

    public GameImage getImage(File file) {
        try {
            return new GameImage(new Image(file.getAbsolutePath()));
        } catch (SlickException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getHeight() {
        return gameContainer.getHeight();
    }

    public int getWidth() {
        return gameContainer.getWidth();
    }

    public void start(Phase initalPhase) throws SlickException {
        this.initialPhase = initalPhase;
        phaseManager.change(new BootstrapPhase());
        AppGameContainer app = new AppGameContainer(game);

        app.setShowFPS(options.isDebug());
        app.setDisplayMode(
                DEFAULT_SCREEN_SIZE.width,
                DEFAULT_SCREEN_SIZE.height,
                false);
        app.setShowFPS(getOptions().isDebug());        
        
        app.start();
    }

    public Object2D getScreenObject2D() {
        return screenObject2D;
    }

    private void enterGameStateState(Phase phase, Transition leaveTransition, Transition enterTransation) throws SlickException {
        if (game.getState(phase.getID()) == null) {
            game.addState(phase);
        }
        game.enterState(phase.getID(), leaveTransition, enterTransation);
    }

    public PhaseManager.SubPhaseManager getMainSubPhaseManager() {
        return mainPhaseManager;
    }

    public PhaseManager.SubPhaseManager getNoMainSubPhaseManager() {
        return noMainPhaseManager;
    }

    private class BootstrapPhase extends Phase {

        @Override
        public void enter(GameContainer container, StateBasedGame game) throws SlickException {
            phaseManager.clear();
            phaseManager.change(initialPhase, null, new FadeInTransition(Color.black));
        }
    }
}
