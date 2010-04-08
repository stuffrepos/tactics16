package tactics16.scenes.battle;

import tactics16.MyGame;
import tactics16.components.GlowingRectangle;
import tactics16.components.MapCursor;
import tactics16.game.Map;
import tactics16.game.Person;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
class MovimentSubPhase implements Phase {

    private final BattleScene parentScene;
    private MapCursor mapCursor;
    private Person selectedPerson;
    private PersonMoviment moviment;
    private GlowingRectangle visualSelectedPerson = new GlowingRectangle(0x770000, Map.TERRAIN_SIZE);

    public MovimentSubPhase(BattleScene parentScene) {
        this.parentScene = parentScene;
    }

    public void onAdd() {
        this.mapCursor = new MapCursor(parentScene.getGeoGame().getMap(), parentScene.getMapPosition());
    }

    public void onRemove() {
    }

    private void updateStatusDialog() {
        StringBuilder b = new StringBuilder();
        b.append("Moviment: " + (moviment != null));
        b.append("Moviment: " + (moviment != null));
        b.append("Moviment: " + (moviment != null));
        b.append("Moviment: " + (moviment != null));

    }

    public void update(long elapsedTime) {
        if (moviment == null) {
            mapCursor.update(elapsedTime);
        }

        if (moviment != null) {
            moviment.update(elapsedTime);
            if (moviment.isFinalized()) {
                parentScene.putPersonOnPosition(selectedPerson, moviment.getTerrainTarget());
                moviment = null;
                selectedPerson = null;
            }
        } else if (selectedPerson == null) {
            if (MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
                Person person = parentScene.getGeoGame().getPersonOnMapPosition(mapCursor.getCursor().getPosition());

                if (person != null) {
                    selectedPerson = person;
                    visualSelectedPerson.getPosition().setXY(
                            parentScene.getMapPosition().getX() + selectedPerson.getMapPosition().getX() * Map.TERRAIN_SIZE,
                            parentScene.getMapPosition().getY() + selectedPerson.getMapPosition().getY() * Map.TERRAIN_SIZE);

                }
            }
        } else {
            if (MyGame.getInstance().keyPressed(KeyEvent.VK_BACK_SPACE)) {
                selectedPerson = null;
            } else if (MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
                if (parentScene.getGeoGame().getMap().getTerrain(mapCursor.getCursor().getPosition()).getAllowMoviment() &&
                        parentScene.getGeoGame().getPersonOnMapPosition(mapCursor.getCursor().getPosition()) == null) {
                    moviment = new PersonMoviment(
                            selectedPerson,
                            parentScene.getGeoGame().getMap(),
                            parentScene.getMapPosition(),
                            mapCursor.getCursor().getPosition().clone());
                }
            }
        }

        if (selectedPerson != null) {
            visualSelectedPerson.update(elapsedTime);
        }

        if (moviment != null) {
            moviment.update(elapsedTime);
        }
    }

    public void render(Graphics2D g) {
        if (moviment == null) {
            mapCursor.render(g);
        }

        if (selectedPerson != null && moviment == null) {
            visualSelectedPerson.render(g);
        }
    }

    public void onExit() {
    }

    public void onEnter() {
    }
}
