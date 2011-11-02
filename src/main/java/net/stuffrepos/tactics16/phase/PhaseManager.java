package net.stuffrepos.tactics16.phase;

import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.util.LifoQueue;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PhaseManager {

    private LifoQueue<Phase> phaseStack = new LifoQueue<Phase>();
    private boolean finalized = false;
    private boolean main;

    public PhaseManager() {
        this(false);
    }

    public PhaseManager(boolean main) {
        this.main = main;
    }

    private SubPhaseManager getSubPhaseManager() {
        return main
                ? MyGame.getInstance().getMainSubPhaseManager()
                : MyGame.getInstance().getNoMainSubPhaseManager();
    }

    public void change(Phase phase) {
        change(phase, null, null);
    }

    public void change(Phase phase, Transition leaveTransition, Transition enterTransition) {
        changeOrAdvance(phase, true, leaveTransition, enterTransition);
    }

    public void advance(Phase phase) {
        changeOrAdvance(phase, false, null, null);
    }

    private void changeOrAdvance(Phase phase, boolean pool, Transition leaveTransition, Transition enterTransition) {
        if (!finalized) {
            if (phaseStack.peek() != phase) {
                Phase previousPhase = (pool ? phaseStack.poll() : phaseStack.peek());
                phaseStack.add(phase);                
                try {
                    getSubPhaseManager().onChange(
                            phase,
                            previousPhase,
                            leaveTransition,
                            enterTransition);
                } catch (SlickException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void back() {
        if (!finalized) {
            Phase previousPhase = phaseStack.poll();
       
            if (phaseStack.peek() != null) {
                try {
                    getSubPhaseManager().onChange(
                            phaseStack.peek(),
                            previousPhase,
                            null,
                            null);
                } catch (SlickException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public Phase getCurrentPhase() {
        return finalized ? null : phaseStack.peek();
    }

    public void clear() {
        phaseStack.clear();
    }

    public void finalizeEntity() {
        try {
            getSubPhaseManager().onChange(null, phaseStack.poll(), null, null);
        } catch (SlickException ex) {
            throw new RuntimeException(ex);
        }
        finalized = true;
    }

    public void resetTo(Phase phase) {
        while (getCurrentPhase() != null && getCurrentPhase() != phase) {
            back();
        }
    }

    public static interface SubPhaseManager {

        public void onChange(Phase phase, Phase previousPhase, Transition leaveTransition, Transition enterTransation) throws SlickException;
    }
}
