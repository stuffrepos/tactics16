package tactics16.scenes;

import tactics16.MyGame;
import tactics16.components.Menu;
import tactics16.components.MenuOption;
import tactics16.game.Map;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectMapScene implements Phase {

    private void setSelectedMap(Map map) {
        this.selectedMap = map;
    }

    private Map getCurrentMap() {
        return (mapSelector.getCursor().getSelected() instanceof MapOption)
                ? ((MapOption) mapSelector.getCursor().getSelected()).getMap()
                : null;
    }

    public void onExit() {
    }

    public void onEnter() {
        setSelectedMap(null);
    }

    // <editor-fold defaultstate="collapsed" desc="class MapOption">
    private class MapOption extends MenuOption {

        private final Map map;

        public MapOption(Map map) {
            super(map.getName());
            this.map = map;
        }

        @Override
        public void executeAction() {
            setSelectedMap(MapOption.this.map);
            if (callback != null) {
                callback.run();
            }
        }

        public Map getMap() {
            return map;
        }
    }// </editor-fold>
    private Menu mapSelector;
    private Runnable callback;
    private Map selectedMap;

    public SelectMapScene(Runnable callback) {
        this.callback = callback;
    }

    public void update(long elapsedTime) {
        mapSelector.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        mapSelector.render(g);
        if (getCurrentMap() != null) {
            g.drawImage(getCurrentMap().getThumbnail(), 200, 20, null);
        }
    }

    public void onAdd() {
        mapSelector = new Menu();

        for (Map map : MyGame.getInstance().getLoader().getMaps()) {
            mapSelector.addOption(new MapOption(map));
        }

        mapSelector.addOption(new MenuOption("Cancel", KeyEvent.VK_ESCAPE) {

            @Override
            public void executeAction() {
                if (callback != null) {
                    callback.run();
                }
            }
        });
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void onRemove() {
    }
}
