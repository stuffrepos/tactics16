package net.stuffrepos.tactics16.scenes.battle.eventprocessors;

import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.battleengine.ActConfirmRequest;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.MapCheckedArea;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.RequestProcessor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActConfirmRequestProcessor extends RequestProcessor<ActConfirmRequest, Boolean> {

    public ActConfirmRequestProcessor(final BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final ActConfirmRequest event) {
        return new Phase() {
            private Menu menu;
            private MapCheckedArea targetRay;            
            private List<Person> personsTargets;

            @Override
            public void init(GameContainer container, StateBasedGame game) throws SlickException {
                super.init(container, game);
                this.menu = new Menu(
                        new CommonMenuOption("Confirm") {
                    @Override
                    public void executeAction() {
                        answer(true);
                    }
                },
                        new CommonMenuOption("Cancel") {
                    @Override
                    public void executeAction() {
                        answer(false);
                    }
                });
            }

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                if (event.getSelectedAction() != null) {
                    Coordinate visualTargetPosition = getScene().getVisualBattleMap().getVisualMap().getTerrainPosition(Coordinate.fromMapCoordinate(event.getActionTarget()));
                    this.targetRay = getScene().getVisualBattleMap().createMapCheckedArea(
                            Coordinate.list(event.getActRay()),
                            0xFF0000);
                    this.menu.getPosition().setXY(
                            visualTargetPosition.getX() + Map.TERRAIN_SIZE + Layout.OBJECT_GAP,
                            visualTargetPosition.getY() + -Layout.OBJECT_GAP);
                    personsTargets = new LinkedList<Person>();

                    for (Coordinate c : this.targetRay.getTerrainPositions()) {
                        Person person = getScene().getVisualBattleMap().getBattleGame().getPersonOnMapPosition(c);
                        if (person != null) {
                            personsTargets.add(person);
                            person.getGameActionControl().advance(Job.GameAction.SELECTED);
                        }
                    }
                } else {
                    Coordinate visualTargetPosition = getScene().getVisualBattleMap().getVisualMap().getTerrainPosition(
                            Coordinate.fromMapCoordinate(event.getPerson().getPosition()));
                    this.menu.getPosition().setXY(
                            visualTargetPosition.getX() + Map.TERRAIN_SIZE + Layout.OBJECT_GAP,
                            visualTargetPosition.getY() + -Layout.OBJECT_GAP);
                }
            }

            @Override
            public void leave(GameContainer container, StateBasedGame game) throws SlickException {
                if (targetRay != null) {
                    targetRay.finalizeEntity();
                }
                if (personsTargets != null) {
                    for (Person person : personsTargets) {
                        person.getGameActionControl().back();
                    }
                }
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                menu.update(delta);
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) {
                menu.render(g);
            }
        };
    }

    @Override
    public Class getEventClass() {
        return ActConfirmRequest.class;
    }
}
