package tactics16.phase;

import tactics16.util.LifoQueue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PhaseManager {

    private LifoQueue<Phase> phaseStack = new LifoQueue<Phase>();

    public void change(Phase phase) {
        if (phaseStack.peek() != phase) {
            removeHead();
            addHead(phase);
        }
    }

    public void advance(Phase phase) {
        if (phaseStack.peek() != phase) {
            Phase previousPhase = phaseStack.peek();
            if (previousPhase != null) {
                previousPhase.onExit();
            }
            addHead(phase);
        }
    }

    public void back() {
        removeHead();

        Phase phase = phaseStack.peek();
        if (phase != null) {
            phase.onEnter();
        }
    }

    private void addHead(Phase phase) {
        phaseStack.add(phase);
        phase.onAdd();
        phase.onEnter();
    }

    private void removeHead() {
        Phase p = phaseStack.poll();
        if (p != null) {
            p.onExit();
            p.onRemove();
        }
    }

    public Phase getCurrentPhase() {
        return phaseStack.peek();
    }

    public void clear() {
        phaseStack.clear();
    }
}
