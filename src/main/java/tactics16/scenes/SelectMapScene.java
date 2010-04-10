package tactics16.scenes;

import tactics16.MyGame;
import tactics16.components.Menu;
import tactics16.components.MenuOption;
import tactics16.game.Map;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import tactics16.Layout;
import tactics16.components.VisualThumbnailMap;
import tactics16.util.Cursor1D;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectMapScene implements Phase {

    private Menu mapSelector;
    private Runnable callback;
    private Map selectedMap;
    private VisualThumbnailMap visualMap = new VisualThumbnailMap();
    
    public SelectMapScene(Runnable callback) {
        this.callback = callback;
    }

    public void update(long elapsedTime) {
        mapSelector.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        mapSelector.render(g);
        visualMap.render(g);
    }

    public void onAdd() {
        mapSelector = new Menu();
        mapSelector.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);

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

        visualMap.getPosition().setXY(Layout.getRightGap(mapSelector), mapSelector.getTop());

        mapSelector.getCursor().getCursor().addListener(new Listener<Cursor1D>() {
            public void onChange(Cursor1D source) {
                visualMap.setMap(getCurrentMap());
            }
        });
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void onRemove() {
    }

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
}
