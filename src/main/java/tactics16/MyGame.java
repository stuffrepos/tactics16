package tactics16;

import com.golden.gamedev.Game;
import tactics16.phase.PhaseManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MyGame extends Game {
    
    private static MyGame instance;
    private JsonLoader loader;
    private Graphics2D defaultGraphics2D;
    private PhaseManager phaseManager = new PhaseManager();

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

        g.setColor(Color.WHITE);
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
}
