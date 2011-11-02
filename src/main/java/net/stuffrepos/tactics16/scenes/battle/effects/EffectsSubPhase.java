package net.stuffrepos.tactics16.scenes.battle.effects;

import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.util.Set;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Job.GameAction;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import net.stuffrepos.tactics16.scenes.battle.BattleAction;
import net.stuffrepos.tactics16.scenes.battle.BattleActionResult;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.Person;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class EffectsSubPhase extends Phase {

    private final BattleScene parentScene;
    private final BattleActionResult battleActionResult;

    public EffectsSubPhase(BattleScene parentScene, BattleAction battleAction) {
        this.parentScene = parentScene;
        this.battleActionResult = new BattleActionResult(battleAction);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        this.parentScene.getPhaseManager().advance(new SelectEvade());
    }

    private class SelectEvade extends Phase {

        private final PersonsTargets personsTargets;

        // <editor-fold defaultstate="collapsed" desc="implementation">
        public SelectEvade() {
            this.personsTargets = new PersonsTargets(battleActionResult.getAction().getPersonsTargets());
        }

        @Override
        public void enter(GameContainer container, StateBasedGame game) {
            battleActionResult.getAction().getAgent().getGameActionControl().advance(GameAction.ON_ATTACKING);
        }

        @Override
        public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
            if (personsTargets.isFinalized()) {
                parentScene.getPhaseManager().advance(new EffectPhase());
            } else {
                if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
                    personsTargets.previous();
                }

                personsTargets.update(container, game, delta);
            }
        }

        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
            personsTargets.render(container, game, g);
        }// </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="class PersonTargets">
        private class PersonsTargets {

            private int current;
            private final ArrayList<Person> personsTargets;
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

            private void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                phaseManager.getCurrentPhase().update(container, game, delta);

                if (isFinalized() && !checkFinalized) {
                    checkFinalized = true;
                    phaseManager.finalizeEntity();
                }
            }

            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                if (phaseManager.getCurrentPhase() != null) {
                    phaseManager.getCurrentPhase().render(container, game, g);
                }
            }

            private boolean isFinalized() {
                return current >= this.personsTargets.size();
            }

            private class EvadeSelectPhase extends Phase {

                private final Person person;
                private EvadeSelector evadeSelector;

                public EvadeSelectPhase(Person person) {
                    this.person = person;
                }

                @Override
                public void render(GameContainer container, StateBasedGame game, Graphics g) {
                    evadeSelector.render(g);
                }

                @Override
                public void update(GameContainer container, StateBasedGame game, int delta) {
                    evadeSelector.update(delta);

                    if (evadeSelector.isFinalized()) {
                        battleActionResult.setPersonEvaded(
                                personsTargets.get(current),
                                evadeSelector.getResponse());
                        next();
                    }
                }

                @Override
                public void initResources(GameContainer container, StateBasedGame game) {
                    this.evadeSelector = new EvadeSelector(battleActionResult.getAction(), person);
                }

                @Override
                public void enter(GameContainer container, StateBasedGame game) {
                    person.getGameActionControl().advance(GameAction.SELECTED);
                }

                @Override
                public void leave(GameContainer container, StateBasedGame game) throws SlickException {
                    person.getGameActionControl().back();
                }
            }
        }// </editor-fold>

        private class EffectPhase extends Phase {

            private EffectAnimation effectAnimation;

            @Override
            public void enter(GameContainer container, StateBasedGame game) {
                this.effectAnimation = new EffectAnimation(
                        parentScene.getVisualBattleMap(),
                        battleActionResult);
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) {
                effectAnimation.update(delta);

                if (effectAnimation.isFinalized()) {
                    parentScene.toPersonActionSubPhase(battleActionResult);
                }
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) {
                effectAnimation.render(g);
            }
        }
    }
}
