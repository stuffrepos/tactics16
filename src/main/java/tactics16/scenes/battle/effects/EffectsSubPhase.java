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
import tactics16.phase.PhaseManager;
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
            battleAction.getAgent().getGameActionControl().advance(GameAction.ON_ATTACKING);
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
            private Map<Person, Boolean> personsEvaded =
                    new HashMap<Person, Boolean>();
            private PhaseManager phaseManager = new PhaseManager();
            private boolean checkFinalized = false;

            private PersonsTargets(Set<Person> personsTargets) {
                this.personsTargets = new ArrayList<Person>(personsTargets);
                this.current = -1;
                next();
            }

            private void next() {
                current++;
                if (current < personsTargets.size()) {
                    phaseManager.advance(new EvadeSelectPhase(personsTargets.get(current)));
                }
            }

            public void previous() {
                if (current > 0) {
                    current--;
                    phaseManager.back();
                }
            }

            public void update(long elapsedTime) {
                phaseManager.getCurrentPhase().update(elapsedTime);

                if (isFinalized() && !checkFinalized) {
                    checkFinalized = true;
                    phaseManager.finalizeEntity();
                }
            }

            public void render(Graphics2D g) {
                if (phaseManager.getCurrentPhase() != null) {
                    phaseManager.getCurrentPhase().render(g);
                }
            }

            private boolean isFinalized() {
                return current >= this.personsTargets.size();
            }

            public Map<Person, Boolean> getPersonsEvaded() {
                return this.personsEvaded;
            }

            private class EvadeSelectPhase extends AbstractPhase {

                private final Person person;
                private EvadeSelector evadeSelector;

                public EvadeSelectPhase(Person person) {
                    this.person = person;
                }

                @Override
                public void render(Graphics2D g) {
                    evadeSelector.render(g);
                }

                @Override
                public void update(long elapsedTime) {
                    evadeSelector.update(elapsedTime);

                    if (evadeSelector.isFinalized()) {
                        personsEvaded.put(
                                personsTargets.get(current),
                                evadeSelector.getResponse());
                        next();
                    }
                }

                @Override
                public void onAdd() {
                    this.evadeSelector = new EvadeSelector(battleAction, person);
                }

                @Override
                public void onEnter() {
                    person.getGameActionControl().advance(GameAction.SELECTED);
                }

                @Override
                public void onExit() {
                    person.getGameActionControl().back();
                }
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
