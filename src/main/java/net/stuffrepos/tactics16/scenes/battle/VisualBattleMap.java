package net.stuffrepos.tactics16.scenes.battle;

import java.awt.Graphics2D;
import java.util.List;
import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.components.MapCursor;
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
    private EntitiesLayer cursorsLayer = new EntitiesLayer();
    private EntitiesLayer checkedAreasLayer = new EntitiesLayer();

    public VisualBattleMap(BattleGame battleGame) {
        this.battleGame = battleGame;
        this.visualMap = new VisualMap(battleGame.getMap());
        for(Player player:battleGame.getPlayers()) {
            for(Person person: player.getPersons()) {
                personsLayer.addEntity(person);                
            }
        }
    }

    public void update(long elapsedTime) {
        visualMap.update(elapsedTime);
        checkedAreasLayer.update(elapsedTime);
        personsLayer.update(elapsedTime);
        cursorsLayer.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        visualMap.render(g);
        checkedAreasLayer.render(g);
        cursorsLayer.render(g);
        personsLayer.render(g);
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
