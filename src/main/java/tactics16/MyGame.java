package tactics16;

import tactics16.datamanager.DataManager;
import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import tactics16.phase.PhaseManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import tactics16.animation.GameImage;
import tactics16.components.Object2D;

/**
 *
import tactics16.util.ObjectCursor1D;
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MyGame {

    private static MyGame instance;
    private Game game = new Game() {

        @Override
        public void initResources() {
            MyGame.this.initResources();
        }

        @Override
        public void update(long elapsedTime) {
            MyGame.this.update(elapsedTime);
        }

        @Override
        public void render(Graphics2D g) {
            MyGame.this.render(g);
        }

        @Override
        protected void notifyError(Throwable error) {
            System.out.println("Notificado!");
            super.notifyError(error);
        }
    };
    private DataManager loader;
    private Graphics2D defaultGraphics2D;
    private PhaseManager phaseManager = new PhaseManager();
    private KeyMapping keyMapping = new KeyMapping();
    private Font font = new Font("Liberation Mono", Font.PLAIN, 12);
    //private Font font = new Font("Purisa", Font.PLAIN, 12);
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

    public static MyGame getInstance() {
        return instance;
    }

    public static void createInstance(String dataPath) {
        if (instance == null) {
            instance = new MyGame(dataPath);
        }
    }

    private MyGame(String dataPath) {
        loader = new DataManager(new File(dataPath));
        keyMapping.setMapping(GameKey.UP, KeyEvent.VK_UP);
        keyMapping.setMapping(GameKey.DOWN, KeyEvent.VK_DOWN);
        keyMapping.setMapping(GameKey.LEFT, KeyEvent.VK_LEFT);
        keyMapping.setMapping(GameKey.RIGHT, KeyEvent.VK_RIGHT);
        keyMapping.setMapping(GameKey.CONFIRM, KeyEvent.VK_ENTER, KeyEvent.VK_SPACE);
        keyMapping.setMapping(GameKey.CANCEL, KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE);
        keyMapping.setMapping(GameKey.OPTIONS, KeyEvent.VK_F1);
        keyMapping.setMapping(GameKey.PREVIOUS, KeyEvent.VK_PAGE_UP);
        keyMapping.setMapping(GameKey.NEXT, KeyEvent.VK_PAGE_DOWN);
    }

    public void initResources() {
        loader.loadDirectory(loader.getDataDirectory());
    }

    public void update(long elapsedTime) {
        try {
            this.phaseManager.getCurrentPhase().update(elapsedTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.quit();
        }

    }

    public void render(Graphics2D g) {
        defaultGraphics2D = g;
        if (font != null) {
            g.setFont(font);
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
        try {
            this.phaseManager.getCurrentPhase().render(g);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.quit();
        }

    }

    public void quit() {
        this.game.finish();
    }

    public DataManager getLoader() {
        return this.loader;
    }

    public Graphics2D getDefaultGraphics2D() {
        return defaultGraphics2D;
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    public boolean isKeyPressed(GameKey key) {
        return keyMapping.isKeyPressed(key);
    }

    public KeyMapping getKeyMapping() {
        return keyMapping;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public GameImage getImage(File file) {
        return new GameImage(game.getImage(file.getAbsolutePath()));
    }

    public int getHeight() {
        return game.getHeight();
    }

    public int getWidth() {
        return game.getWidth();
    }

    public void start() {
        GameLoader gameLoader = new GameLoader();
        gameLoader.setup(game, new Dimension(640, 480), false);
        gameLoader.start();
    }

    public Object2D getScreenObject2D() {
        return screenObject2D;
    }

    public class KeyMapping {

        private Map<GameKey, List<Integer>> mapping = new TreeMap<GameKey, List<Integer>>();

        private void setMapping(GameKey gameKey, int... keys) {
            List<Integer> keysList = new ArrayList<Integer>();
            for (int key : keys) {
                keysList.add(key);
            }
            mapping.put(gameKey, keysList);
        }

        private boolean isKeyPressed(GameKey gameKey) {
            for (Integer key : mapping.get(gameKey)) {
                if (game.keyPressed(key)) {
                    return true;
                }
            }
            return false;

        }

        public Collection<Integer> getKeys(GameKey gameKey) {
            return mapping.get(gameKey);
        }
    }
}
