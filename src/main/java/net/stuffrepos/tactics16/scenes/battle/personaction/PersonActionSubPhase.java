package net.stuffrepos.tactics16.scenes.battle.personaction;

import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.MapCursor;
import org.newdawn.slick.Graphics;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.ObjectMenuOption;
import net.stuffrepos.tactics16.game.Action;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.Terrain;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleAction;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.MapCheckedArea;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionSubPhase extends Phase {

    /*
    private final BattleScene parentScene;
    private final SelectPersonStep selectPersonStep = new SelectPersonStep();
    private Coordinate cursorLastPosition = null;

    public PersonActionSubPhase(BattleScene parentScene) {
        this.parentScene = parentScene;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        parentScene.getPhaseManager().change(selectPersonStep);
    }

    @Override
    public String toString() {
        if (parentScene.getPhaseManager().getCurrentPhase() == null) {
            return null;
        } else if (parentScene.getPhaseManager().getCurrentPhase() == this) {
            return super.toString();
        } else {
            return parentScene.getPhaseManager().getCurrentPhase().toString();
        }

    }

    public void checkCancelInvocation() {
        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            parentScene.getPhaseManager().back();
        }
    }

    private class SelectPersonStep extends Phase {

        private MapCursor selectPersonCursor;

        // <editor-fold defaultstate="collapsed" desc="implementation">
        @Override
        public void enter(GameContainer container, StateBasedGame game) {
            this.selectPersonCursor = parentScene.getVisualBattleMap().createMapCursor();
            if (cursorLastPosition != null) {
                this.selectPersonCursor.getCursor().moveTo(cursorLastPosition);
            }
        }

        @Override
        public void leave(GameContainer container, StateBasedGame game) throws SlickException {
            this.selectPersonCursor.finalizeEntity();
        }

        @Override
        public void update(GameContainer container, StateBasedGame game, int delta) {            
            if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                Person person = parentScene.getVisualBattleMap().getBattleGame().getPersonOnMapPosition(selectPersonCursor.getCursor().getPosition());
                if (person != null && !parentScene.isUsed(person) && person.getPlayer().equals(parentScene.getCurrentPlayer())) {
                    parentScene.getPhaseManager().advance(new SelectMovimentTargetStep(person));
                }
            }

        }

        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g) {
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();

            Terrain terrain = parentScene.getVisualBattleMap().getVisualMap().getMap().getTerrain(selectPersonCursor.getCursor().getPosition());

            if (terrain != null) {
                b.append("Terrain\n");
                b.append("\tName: " + terrain.getName() + "\n");
                b.append("\tAllow moviment: " + (terrain.getAllowMoviment() ? "Yes" : "No") + "\n");
                b.append("\tAllow action: " + (terrain.getAllowAction() ? "Yes" : "No") + "\n");
            }

            Person person = parentScene.getVisualBattleMap().getBattleGame().getPersonOnMapPosition(selectPersonCursor.getCursor().getPosition());
            if (person != null) {
                b.append("Person\n");
                b.append("\tPlayer: " + person.getPlayer().getName() + "\n");
                b.append("\tName: " + person.getJob().getName() + "\n");
                b.append("\tDefense: " + person.getJob().getDefense() + "\n");
                b.append("\tEvasiveness: " + person.getJob().getEvasiveness() + "\n");
                b.append("\tHP: " + person.getHealthPoints() + "/" + Person.MAX_HEALTH_POINTS + "\n");
                b.append("\tAP: " + person.getAgilityPoints() + "/" + Person.MAX_AGILITY_POINTS + "\n");
            }

            return b.toString();
        }
        // </editor-fold>

        private class SelectMovimentTargetStep extends Phase {
            
            private class PersonMovimentStep extends Phase {

               

                private class SelectActionStep extends Phase {

                    
                    private class SelectTargetStep extends Phase {
                        

                        private class ConfirmActionStep extends Phase {
                            
                            private class PersonAction extends Phase {

                                
                            }
                        }
                    }
                }
            }
        }
    }*/
}
