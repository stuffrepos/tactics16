package net.stuffrepos.tactics16.scenes;

import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.phase.Phase;
import java.awt.Graphics2D;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.components.VisualThumbnailMap;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectMapScene implements Phase {

    private Menu mapSelector;
    private Runnable callback;
    private Map selectedMap;
    private VisualThumbnailMap visualMap = new VisualThumbnailMap();
    private final boolean onlyPlayables;
    private PhaseTitle title;

    public SelectMapScene(Runnable callback, boolean onlyPlayables) {
        this.callback = callback;
        this.onlyPlayables = onlyPlayables;
    }

    public void update(long elapsedTime) {
        mapSelector.update(elapsedTime);        
    }

    public void render(Graphics2D g) {
        mapSelector.render(g);
        visualMap.render(g);
        title.render(g);
    }

    public void onAdd() {
        title = new PhaseTitle("Select Map");

        mapSelector = new Menu();
        mapSelector.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));

        for (Map map : MyGame.getInstance().getLoader().getMaps()) {
            if (!onlyPlayables || map.isPlayable()) {
                mapSelector.addOption(new MapOption(map));
            }
        }

        mapSelector.addOption(new CommonMenuOption("Cancel", GameKey.CANCEL) {

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
    private class MapOption extends CommonMenuOption {

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
