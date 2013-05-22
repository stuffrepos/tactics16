package net.stuffrepos.tactics16.scenes.battle;

import org.newdawn.slick.Graphics;
import java.util.List;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesLayer;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.components.ActionBoxInfo;
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
    private MapCursor cursor = null;
    private EntitiesLayer checkedAreasLayer = new EntitiesLayer();
    private PersonBoxInfo personBoxInfo = null;
    private ActionBoxInfo actionBoxInfo = null;
    private Action currentAction;

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

        if (cursor != null) {
            if (cursor.isFinalized()) {
                cursor = null;
            } else {
                cursor.update(delta);

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

                if (personOnCursor != null && currentAction != null) {
                    if (actionBoxInfo == null
                            || !personOnCursor.equals(actionBoxInfo.getPerson())
                            || !currentAction.equals(actionBoxInfo.getAction())) {
                        actionBoxInfo = new ActionBoxInfo(
                                getBattleGame().getEngine(),
                                currentAction,
                                personOnCursor,
                                personBoxInfo.getWidth());
                        actionBoxInfo.getPosition().setXY(
                                personBoxInfo.getLeft(),
                                Layout.getTopGap(personBoxInfo, actionBoxInfo));
                    }
                } else {
                    actionBoxInfo = null;
                }
            }

        }

        if (personBoxInfo != null) {
            personBoxInfo.update(delta);
        }

        if (actionBoxInfo != null) {
            actionBoxInfo.update(delta);
        }
    }

    public void render(Graphics g) {
        visualMap.render(g);
        checkedAreasLayer.render(g);

        if (cursor != null) {
            cursor.render(g);
        }

        personsLayer.render(g);
        if (personBoxInfo != null) {
            personBoxInfo.render(g);
        }

        if (actionBoxInfo != null) {
            actionBoxInfo.render(g);
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

    public void createMovimentMapCursor(MapCoordinate initialPosition) {
        assert cursor == null;
        this.cursor = new MapCursor(visualMap);
        this.cursor.getCursor().moveTo(Coordinate.fromMapCoordinate(initialPosition));
    }

    public void createActionTargetMapCursor(MapCoordinate initialPosition, Action action) {
        createMovimentMapCursor(initialPosition);
        currentAction = action;
    }

    public void finalizeMapCursor() {
        assert cursor != null;
        cursor.finalizeEntity();
        currentAction = null;
    }

    public Coordinate getMapCursorPosition() {
        return cursor.getCursor().getPosition();
    }

    public MapCheckedArea createMapCheckedArea(List<Coordinate> positions,int color) {
        MapCheckedArea mapCheckedArea = new MapCheckedArea(visualMap, positions, color);
        checkedAreasLayer.addEntity(mapCheckedArea);
        return mapCheckedArea;
    }
}
