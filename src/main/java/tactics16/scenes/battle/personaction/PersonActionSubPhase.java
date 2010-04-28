package tactics16.scenes.battle.personaction;

import tactics16.scenes.battle.*;
import tactics16.MyGame;
import tactics16.components.MapCursor;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import tactics16.GameKey;
import tactics16.Layout;
import tactics16.components.TextBox;
import tactics16.components.menu.CommonMenuOption;
import tactics16.components.menu.Menu;
import tactics16.components.menu.ObjectMenuOption;
import tactics16.game.Action;
import tactics16.game.Coordinate;
import tactics16.game.Job.GameAction;
import tactics16.game.Map;
import tactics16.game.Terrain;
import tactics16.phase.AbstractPhase;
import tactics16.util.cursors.Cursor1D;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonActionSubPhase extends AbstractPhase {

    private final BattleScene parentScene;
    private final SelectPersonStep selectPersonStep = new SelectPersonStep();
    private Coordinate cursorLastPosition = null;

    public PersonActionSubPhase(BattleScene parentScene) {
        this.parentScene = parentScene;
    }

    @Override
    public void onEnter() {
        parentScene.getPhaseManager().change(selectPersonStep);
    }

    @Override
    public String toString() {
        if (parentScene.getPhaseManager().getCurrentPhase() == null) {
            return null;
        } else {
            return parentScene.getPhaseManager().getCurrentPhase().toString();
        }

    }

    public void checkCancelInvocation() {
        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            parentScene.getPhaseManager().back();
        }
    }

    private class SelectPersonStep extends AbstractPhase {

        private MapCursor selectPersonCursor;

        // <editor-fold defaultstate="collapsed" desc="implementation">
        @Override
        public void onEnter() {
            this.selectPersonCursor = parentScene.getVisualBattleMap().createMapCursor();
            if (cursorLastPosition != null) {
                this.selectPersonCursor.getCursor().moveTo(cursorLastPosition);
            }
        }

        @Override
        public void onExit() {
            this.selectPersonCursor.finalizeEntity();
        }

        @Override
        public void update(long elapsedTime) {
            if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                Person person = parentScene.getVisualBattleMap().getBattleGame().getPersonOnMapPosition(selectPersonCursor.getCursor().getPosition());
                if (person != null && !parentScene.isUsed(person) && person.getPlayer().equals(parentScene.getCurrentPlayer())) {
                    parentScene.getPhaseManager().advance(new SelectMovimentTargetStep(person));
                }
            }

        }

        @Override
        public void render(Graphics2D g) {
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

        private class SelectMovimentTargetStep extends AbstractPhase {

            private MapCheckedArea mapCheckedArea;
            private Person selectedPerson;
            private Coordinate selectedPersonPosition;
            private MapCursor movimentCursor;

            // <editor-fold defaultstate="collapsed" desc="implementation">
            private SelectMovimentTargetStep(Person person) {
                selectedPerson = person;
                selectedPersonPosition = selectedPerson.getMapPosition().clone();
                cursorLastPosition = selectedPersonPosition.clone();
            }

            public List<Coordinate> calculateSelectedPersonMovimentArea() {
                List<Coordinate> area = new LinkedList<Coordinate>();

                for (java.util.Map.Entry<Coordinate, Integer> e :
                        parentScene.getVisualBattleMap().getBattleGame().calculateMovimentDistances(
                        selectedPerson.getMapPosition(),selectedPerson.getPlayer()).entrySet()) {
                    Person personOnPosition = parentScene.getVisualBattleMap().getBattleGame().getPersonOnMapPosition(e.getKey());
                    if (e.getValue() <= selectedPerson.getMoviment() &&
                            (personOnPosition == null || personOnPosition == selectedPerson)) {
                        area.add(e.getKey());
                    }
                }

                return area;
            }

            @Override
            public void onEnter() {
                parentScene.putPersonOnPosition(selectedPerson, selectedPersonPosition);
                selectedPerson.getGameActionControl().advance(GameAction.SELECTED);
                mapCheckedArea = parentScene.getVisualBattleMap().createMapCheckedArea(
                        calculateSelectedPersonMovimentArea(),
                        0x0000FF);
                movimentCursor = parentScene.getVisualBattleMap().createMapCursor();
                movimentCursor.getCursor().moveTo(selectedPerson.getMapPosition());
            }

            @Override
            public void onExit() {
                movimentCursor.finalizeEntity();
                mapCheckedArea.finalizeEntity();
                selectedPerson.getGameActionControl().back();
            }

            @Override
            public void update(long elapsedTime) {
                if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                    if (mapCheckedArea.inArea(movimentCursor.getCursor().getPosition())) {
                        parentScene.getPhaseManager().advance(new PersonMovimentStep());
                    }
                }

                checkCancelInvocation();
            }

            @Override
            public String toString() {
                return "Select person's next position";
            }

            // </editor-fold>
            private class PersonMovimentStep extends AbstractPhase {

                private PersonMovimentAnimation moviment;

                // <editor-fold defaultstate="collapsed" desc="implementation">
                @Override
                public void onEnter() {
                    moviment = new PersonMovimentAnimation(
                            selectedPerson,
                            parentScene.getVisualBattleMap(),
                            movimentCursor.getCursor().getPosition().clone());
                }

                @Override
                public void update(long elapsedTime) {
                    moviment.update(elapsedTime);
                    if (moviment.isFinalized()) {
                        parentScene.putPersonOnPosition(selectedPerson, moviment.getTerrainTarget());
                        parentScene.getPhaseManager().change(new SelectActionStep());
                    }
                }

                @Override
                public void onExit() {
                    if (!moviment.isFinalized()) {
                    }
                }// </editor-fold>

                private class SelectActionStep extends AbstractPhase {

                    private Menu actionMenu;
                    private TextBox actionStatus;

                    // <editor-fold defaultstate="collapsed" desc="implementation">
                    @Override
                    public void update(long elapsedTime) {
                        actionMenu.update(elapsedTime);
                    }

                    @Override
                    public void render(Graphics2D g) {
                        actionMenu.render(g);
                        actionStatus.render(g);
                    }

                    @Override
                    public void onEnter() {
                        actionMenu = new Menu();
                        actionStatus = new TextBox();

                        for (Action action : selectedPerson.getJob().getActions()) {
                            actionMenu.addOption(new ObjectMenuOption<Action>(action) {

                                public void executeAction() {
                                    parentScene.getPhaseManager().advance(new SelectTargetStep(this.getSource()));
                                }

                                @Override
                                public String toString() {
                                    StringBuilder b = new StringBuilder();


                                    b.append("Power: " + getSource().getPower() + "\n");
                                    b.append("Agility: " + getSource().getAgility() + "\n");
                                    b.append(String.format(
                                            "Reach: %d/%d -> %d\n",
                                            getSource().getReach().getMin(),
                                            getSource().getReach().getMax(),
                                            getSource().getReach().getRay()));
                                    return b.toString();
                                }
                            });
                        }

                        actionMenu.addOption(new CommonMenuOption("Wait", "Waits for next turn") {

                            @Override
                            public void executeAction() {
                                cursorLastPosition = movimentCursor.getCursor().getPosition().clone();
                                 parentScene.toEffectSubPhase(
                                         new BattleAction(
                                         parentScene.getVisualBattleMap().getBattleGame(),
                                         selectedPerson, null, null, null)
                                         );
                            }
                        });

                        actionMenu.addOption(new CommonMenuOption("Cancel", GameKey.CANCEL, "Back to action selector") {

                            @Override
                            public void executeAction() {
                                parentScene.putPersonOnPosition(selectedPerson, selectedPersonPosition);
                                parentScene.getPhaseManager().back();
                            }
                        });

                        actionMenu.getPosition().setXY(
                                selectedPerson.getPosition().getX() + Map.TERRAIN_SIZE,
                                selectedPerson.getPosition().getY() - Map.TERRAIN_SIZE);
                        actionStatus.getPosition().setXY(
                                Layout.getRightGap(actionMenu),
                                actionMenu.getPosition().getY());

                        actionMenu.getCursor().getCursor().addListener(new Listener<Cursor1D>() {

                            public void onChange(Cursor1D source) {
                                actionStatus.setText(actionMenu.getCursor().getSelected().toString());
                            }
                        });
                    }

                    // </editor-fold>
                    private class SelectTargetStep extends AbstractPhase {

                        private MapCheckedArea mapCheckedArea;
                        private Action selectedAction;
                        private MapCursor targetCursor;

                        // <editor-fold defaultstate="collapsed" desc="implementation">
                        private SelectTargetStep(Action action) {
                            this.selectedAction = action;
                        }

                        @Override
                        public void onEnter() {
                            mapCheckedArea = parentScene.getVisualBattleMap().createMapCheckedArea(
                                    calculateSelectedActionReachArea(),
                                    0xFF0000);
                            targetCursor = parentScene.getVisualBattleMap().createMapCursor();
                            targetCursor.getCursor().moveTo(selectedPerson.getMapPosition());
                        }

                        @Override
                        public void onExit() {
                            targetCursor.finalizeEntity();
                            mapCheckedArea.finalizeEntity();
                        }

                        @Override
                        public void update(long elapsedTime) {
                            if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
                                if (mapCheckedArea.inArea(targetCursor.getCursor().getPosition())) {
                                    parentScene.getPhaseManager().advance(new ConfirmActionStep(targetCursor.getCursor().getPosition()));
                                }
                            }
                            checkCancelInvocation();
                        }

                        private List<Coordinate> calculateSelectedActionReachArea() {
                            List<Coordinate> area = new LinkedList<Coordinate>();

                            for (java.util.Map.Entry<Coordinate, Integer> e :
                                    parentScene.getVisualBattleMap().getVisualMap().getMap().calculateActionDistances(
                                    selectedPerson.getMapPosition()).entrySet()) {
                                if (e.getValue() != 0 &&
                                        e.getValue() >= selectedAction.getReach().getMin() &&
                                        e.getValue() <= selectedAction.getReach().getMax()) {
                                    area.add(e.getKey());
                                }
                            }

                            return area;
                        }
                        // </editor-fold>

                        private class ConfirmActionStep extends AbstractPhase {

                            private final Coordinate targetPosition;
                            private Menu menu;
                            private MapCheckedArea targetRay;
                            private AgilityPointsSelector agilityPointsSelector;
                            private List<Person> personsTargets;

                            // <editor-fold defaultstate="collapsed" desc="implementation">
                            public ConfirmActionStep(Coordinate targetPosition) {
                                this.targetPosition = targetPosition;
                                this.menu = new Menu(
                                        new CommonMenuOption("Confirm") {

                                            @Override
                                            public void executeAction() {
                                                cursorLastPosition = ConfirmActionStep.this.targetPosition.clone();
                                                parentScene.getPhaseManager().advance(new PersonAction());
                                            }
                                        },
                                        new CommonMenuOption("Cancel") {

                                            @Override
                                            public void executeAction() {
                                                parentScene.getPhaseManager().back();
                                            }
                                        });
                            }

                            @Override
                            public void onEnter() {
                                Coordinate visualTargetPosition = parentScene.getVisualBattleMap().getVisualMap().getTerrainPosition(targetPosition);
                                this.targetRay = parentScene.getVisualBattleMap().createMapCheckedArea(
                                        calculateTargetActionRayArea(),
                                        0xFF0000);
                                agilityPointsSelector = new AgilityPointsSelector(
                                        Math.min(selectedPerson.getAgilityPoints(), Person.MAX_ACTION_USE_AGILITY_POINTS));
                                agilityPointsSelector.getPosition().setXY(
                                        visualTargetPosition.getX() + Map.TERRAIN_SIZE + Layout.OBJECT_GAP,
                                        visualTargetPosition.getY() + -Layout.OBJECT_GAP);
                                this.menu.getPosition().setXY(
                                        agilityPointsSelector.getLeft(),
                                        Layout.getBottomGap(agilityPointsSelector));
                                personsTargets = new LinkedList<Person>();

                                for (Coordinate c : this.targetRay.getTerrainPositions()) {
                                    Person person = parentScene.getVisualBattleMap().getBattleGame().getPersonOnMapPosition(c);
                                    if (person != null) {
                                        personsTargets.add(person);
                                        person.getGameActionControl().advance(GameAction.SELECTED);
                                    }
                                }
                            }

                            @Override
                            public void onExit() {
                                targetRay.finalizeEntity();
                                for (Person person : personsTargets) {
                                    person.getGameActionControl().back();
                                }
                            }

                            @Override
                            public void update(long elapsedTime) {
                                menu.update(elapsedTime);
                                agilityPointsSelector.update(elapsedTime);
                            }

                            @Override
                            public void render(Graphics2D g) {
                                menu.render(g);
                                agilityPointsSelector.render(g);
                            }

                            private List<Coordinate> calculateTargetActionRayArea() {
                                List<Coordinate> area = new LinkedList<Coordinate>();

                                for (java.util.Map.Entry<Coordinate, Integer> e :
                                        parentScene.getVisualBattleMap().getVisualMap().getMap().calculateActionDistances(
                                        targetPosition).entrySet()) {
                                    if (e.getValue() <= selectedAction.getReach().getRay()) {
                                        area.add(e.getKey());
                                    }
                                }

                                return area;
                            }// </editor-fold>

                            private class PersonAction extends AbstractPhase {

                                private PersonActionAnimation actionAnimation;

                                @Override
                                public void onEnter() {
                                    actionAnimation = new PersonActionAnimation(
                                            parentScene.getVisualBattleMap(),
                                            selectedPerson,
                                            selectedAction,
                                            targetPosition);
                                }

                                @Override
                                public void update(long elapsedTime) {
                                    if (actionAnimation.isFinalized()) {
                                        parentScene.toEffectSubPhase(
                                                new BattleAction(
                                                parentScene.getVisualBattleMap().getBattleGame(),
                                                selectedPerson,
                                                selectedAction,
                                                targetCursor.getCursor().getPosition(),
                                                agilityPointsSelector.getAgilityPoints()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
