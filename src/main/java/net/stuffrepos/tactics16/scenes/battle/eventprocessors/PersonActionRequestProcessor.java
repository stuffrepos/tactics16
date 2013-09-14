package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import java.util.Map.Entry;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine.SelectedAction;

import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.battleengine.events.PersonActionRequest;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.ObjectMenuOption;
import net.stuffrepos.tactics16.game.Coordinate;

import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.RequestProcessor;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionRequestProcessor extends RequestProcessor<PersonActionRequest, SelectedAction> {

    private final static Log log = LogFactory.getLog(PersonActionRequestProcessor.class);

    public PersonActionRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final PersonActionRequest event) {
        return new Phase() {
            private Menu actionMenu;
            private TextBox actionStatus;

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                actionMenu.update(delta);
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) {
                actionMenu.render(g);
                actionStatus.render(g);
            }

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                actionMenu = new Menu();
                actionStatus = new TextBox();

                for (Entry<Action, Boolean> e : event.getClassifyPersonActions().entrySet()) {
                    actionMenu.addOption(new ObjectMenuOption<Action>(e.getKey(), e.getValue()) {
                        public void executeAction() {
                            answer(new SelectedAction(getSource()));                            
                        }

                        @Override
                        public String toString() {
                            StringBuilder b = new StringBuilder();


                            b.append("Power: " + getSource().getPower() + "\n");
                            b.append("Accuracy: " + getSource().getAccuracy() + "\n");
                            b.append(String.format(
                                    "Reach: %d..%d\n",
                                    getSource().getReach().getDistanceMin(),
                                    getSource().getReach().getDistanceMax()
                                    ));
                            b.append(String.format(
                                    "Effect: %d..%d",
                                    getSource().getReach().getRayMin(),
                                    getSource().getReach().getRayMax()
                                    ));
                            b.append(String.format(
                                    "\nSP Cost: %d",
                                    getSource().getSpecialPointsCost()
                                    ));
                            return b.toString();
                        }

                    });
                }
                
                actionMenu.addOption(new CommonMenuOption("No Action", "Just moviment and saves speed") {
                    @Override
                    public void executeAction() {
                        getScene().putPersonOnPosition(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()), Coordinate.fromMapCoordinate(event.getPosition()));
                        answer(new SelectedAction(null));
                    }
                });

                actionMenu.addOption(new CommonMenuOption("Cancel", GameKey.CANCEL, "Back to action selector") {
                    @Override
                    public void executeAction() {
                        getScene().putPersonOnPosition(getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()), Coordinate.fromMapCoordinate(event.getPosition()));
                        answer(null);
                    }
                });

                actionMenu.getPosition().setXY(
                        getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getPosition().getX() + Map.TERRAIN_SIZE,
                        getScene().getVisualBattleMap().getBattleGame().getPerson(event.getPerson()).getPosition().getY() - Map.TERRAIN_SIZE);
                actionStatus.getPosition().setXY(
                        Layout.getRightGap(actionMenu),
                        actionMenu.getPosition().getY());

                actionMenu.getCursor().getCursor().addListener(new Listener<Cursor1D>() {
                    public void onChange(Cursor1D source) {
                        actionStatus.setText(actionMenu.getCursor().getSelected().toString());
                    }
                });
            }
        };
    }

    @Override
    public Class getEventClass() {
        return PersonActionRequest.class;
    }
}
