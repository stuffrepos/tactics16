package tactics16;

import com.golden.gamedev.Game;
import tactics16.phase.PhaseManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MyGame extends Game {
    
    private static MyGame instance;
    private JsonLoader loader;
    private Graphics2D defaultGraphics2D;
    private PhaseManager phaseManager = new PhaseManager();
    private KeyMapping keyMapping = new KeyMapping();

    public static MyGame getInstance() {
        return instance;
    }

    public static void createInstance(String dataPath) {
        if (instance == null) {
            instance = new MyGame(dataPath);
        }
    }

    private MyGame(String dataPath) {
        loader = new JsonLoader(this, new File(dataPath));
        keyMapping.setMapping(GameKey.UP,KeyEvent.VK_UP);
        keyMapping.setMapping(GameKey.DOWN,KeyEvent.VK_DOWN);
        keyMapping.setMapping(GameKey.LEFT,KeyEvent.VK_LEFT);
        keyMapping.setMapping(GameKey.RIGHT,KeyEvent.VK_RIGHT);
        keyMapping.setMapping(GameKey.CONFIRM,KeyEvent.VK_ENTER,KeyEvent.VK_SPACE);
        keyMapping.setMapping(GameKey.CANCEL,KeyEvent.VK_ESCAPE,KeyEvent.VK_BACK_SPACE);
        keyMapping.setMapping(GameKey.OPTIONS,KeyEvent.VK_F1);
        keyMapping.setMapping(GameKey.PREVIOUS,KeyEvent.VK_PAGE_UP);
        keyMapping.setMapping(GameKey.NEXT,KeyEvent.VK_PAGE_DOWN);
    }

    @Override
    public void initResources() {
        loader.loadDirectory(loader.getSaveDirectory());        
    }

    @Override
    public void update(long elapsedTime) {
        try {
            this.phaseManager.getCurrentPhase().update(elapsedTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.quit();
        }

    }

    @Override
    public void render(Graphics2D g) {
        if (getDefaultGraphics2D() == null) {
            defaultGraphics2D = g;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        try {
            this.phaseManager.getCurrentPhase().render(g);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.quit();
        }

    }

    public void quit() {
        this.finish();
    }

    public JsonLoader getLoader() {
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

    private class KeyMapping {

        private Map<GameKey,List<Integer>> mapping = new TreeMap<GameKey, List<Integer>>();

        private void setMapping(GameKey gameKey,int... keys) {
            List<Integer> keysList = new ArrayList<Integer>();
            for(int key: keys) {
                keysList.add(key);
            }
            mapping.put(gameKey, keysList);
        }

        private boolean isKeyPressed(GameKey gameKey) {
            for(Integer key: mapping.get(gameKey)) {
                if (keyPressed(key)) {
                    return true;
                }
            }
            return false;
        }
    }
}
