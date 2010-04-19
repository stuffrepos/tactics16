package tactics16.scenes.battle.effects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import tactics16.GameKey;
import tactics16.MyGame;
import tactics16.game.Job.GameAction;
import tactics16.phase.AbstractPhase;
import tactics16.scenes.battle.BattleAction;
import tactics16.scenes.battle.BattleScene;
import tactics16.scenes.battle.Person;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectsSubPhase extends AbstractPhase {

    private final BattleScene parentScene;
    private final BattleAction battleAction;

    public EffectsSubPhase(BattleScene parentScene, BattleAction battleAction) {
        this.parentScene = parentScene;
        this.battleAction = battleAction;
    }

    @Override
    public void onEnter() {
        this.parentScene.getPhaseManager().advance(new SelectEvade());
    }

    private class SelectEvade extends AbstractPhase {

        private final PersonsTargets personsTargets;

        // <editor-fold defaultstate="collapsed" desc="implementation">
        public SelectEvade() {
            this.personsTargets = new PersonsTargets(battleAction.getPersonsTargets());
        }

        @Override
        public void onEnter() {
            battleAction.getAgent().setCurrentGameAction(GameAction.ON_ATTACKING);

        }

        @Override
        public void update(long elapsedTime) {
            if (personsTargets.isFinalized()) {
                parentScene.getPhaseManager().advance(new EffectPhase());
            } else {
                if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
                    personsTargets.previous();
                }

                personsTargets.update(elapsedTime);
            }
        }

        @Override
        public void render(Graphics2D g) {
            personsTargets.render(g);
        }// </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="class PersonTargets">
        private class PersonsTargets {

            private int current;
            private final ArrayList<Person> personsTargets;
            private EvadeSelector evadeSelector;
            private Map<Person, Boolean> personsEvaded =
                    new HashMap<Person, Boolean>();

            private PersonsTargets(Set<Person> personsTargets) {
                this.personsTargets = new ArrayList<Person>(personsTargets);
                this.setTarget(0);
            }

            private void setTarget(int target) {
                current = target;

                if (current < 0) {
                    current = 0;
                } else if (current > personsTargets.size()) {
                    current = personsTargets.size();
                }

                for (int i = 0; i < personsTargets.size(); ++i) {
                    personsTargets.get(i).setCurrentGameAction(
                            i == current
                            ? GameAction.SELECTED
                            : GameAction.STOPPED);
                }

                if (current < personsTargets.size()) {
                    evadeSelector = new EvadeSelector(battleAction, personsTargets.get(current));
                } else {
                    evadeSelector = null;
                }
            }

            public void previous() {
                setTarget(current - 1);
            }

            public void update(long elapsedTime) {
                if (evadeSelector != null) {

                    evadeSelector.update(elapsedTime);

                    if (evadeSelector.isFinalized()) {
                        personsEvaded.put(
                                personsTargets.get(current),
                                evadeSelector.getResponse());
                        setTarget(current + 1);
                    }
                }
            }

            public void render(Graphics2D g) {
                if (evadeSelector != null) {
                    evadeSelector.render(g);
                }
            }

            private boolean isFinalized() {
                return current >= this.personsTargets.size();
            }

            public Map<Person,Boolean> getPersonsEvaded() {
                return this.personsEvaded;
            }

        }// </editor-fold>

        private class EffectPhase extends AbstractPhase {

            private EffectAnimation effectAnimation;

            @Override
            public void onEnter() {
                this.effectAnimation = new EffectAnimation(
                        parentScene.getVisualBattleMap(),
                        battleAction,
                        personsTargets.getPersonsEvaded());
            }

            @Override
            public void update(long elapsedTime) {
                effectAnimation.update(elapsedTime);

                if (effectAnimation.isFinalized()) {
                    parentScene.toPersonActionSubPhase();
                }
            }

            @Override
            public void render(Graphics2D g) {
                effectAnimation.render(g);
            }
        }
    }
}
