package net.stuffrepos.tactics16.scenes.battle;

import org.newdawn.slick.Graphics;
import java.util.List;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.MapCursor;
import net.stuffrepos.tactics16.components.PersonBoxInfo;
import net.stuffrepos.tactics16.components.VisualMap;
import net.stuffrepos.tactics16.game.Coordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class VisualBattleMap implements VisualEntity {
    private final BattleGame battleGame;
    private final VisualMap visualMap;
    private EntitiesLayer personsLayer = new EntitiesLayer();
    private EntitiesLayer<MapCursor> cursorsLayer = new EntitiesLayer<MapCursor>();
    private EntitiesLayer checkedAreasLayer = new EntitiesLayer();
    private PersonBoxInfo personBoxInfo = null;

    public VisualBattleMap(BattleGame battleGame) {
        this.battleGame = battleGame;
        this.visualMap = new VisualMap(battleGame.getMap());
        for(Player player:battleGame.getPlayers()) {
            for(Person person: player.getPersons()) {
                personsLayer.addEntity(person);
            }
        }
    }

    public void update(int delta) {
        visualMap.update(delta);
        checkedAreasLayer.update(delta);
        personsLayer.update(delta);
        cursorsLayer.update(delta);

        for (MapCursor cursor : cursorsLayer.getChildren()) {
            Person personOnCursor = getBattleGame().getPersonOnMapPosition(cursor.getCursor().getPosition());
            if (personOnCursor != null) {
                if (personBoxInfo == null || !personOnCursor.equals(personBoxInfo.getPerson())) {
                    personBoxInfo = new PersonBoxInfo(personOnCursor);
                    personBoxInfo.getPosition().setXY(
                            Layout.OBJECT_GAP,
                            Layout.getScreenHeight() - Layout.OBJECT_GAP - personBoxInfo.getHeight());
                }
            } else {
                personBoxInfo = null;
            }

        }

        if (personBoxInfo != null) {
            personBoxInfo.update(delta);
        }
    }

    public void render(Graphics g) {
        visualMap.render(g);
        checkedAreasLayer.render(g);
        cursorsLayer.render(g);
        personsLayer.render(g);
        if (personBoxInfo != null) {
            personBoxInfo.render(g);
        }
    }

    public boolean isFinalized() {
        return false;
    }

    public BattleGame getBattleGame() {
        return battleGame;
    }

    public VisualMap getVisualMap() {
        return visualMap;
    }

    public MapCursor createMapCursor() {
        MapCursor mapCursor = new MapCursor(visualMap);
        cursorsLayer.addEntity(mapCursor);
        return mapCursor;
    }

    public MapCheckedArea createMapCheckedArea(List<Coordinate> positions,int color) {
        MapCheckedArea mapCheckedArea = new MapCheckedArea(visualMap, positions, color);
        checkedAreasLayer.addEntity(mapCheckedArea);
        return mapCheckedArea;
    }
}
