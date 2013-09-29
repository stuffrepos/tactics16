package net.stuffrepos.tactics16.scenes.battleconfig.cpuia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuIa;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class RandomCpuIa implements CpuIa {

    private static final RandomCpuIa instance = new RandomCpuIa();

    public static RandomCpuIa getInstance() {
        return instance;
    }

    private RandomCpuIa() {
    }

    public CpuCommand buildCpuCommand(BattleEngine engine, int personId) {
        Map.MapCoordinate movimentTarget = random(
                engine.buildMovimentRange(personId));
        Action action = randomAction(
                engine,
                personId);
        Map.MapCoordinate actionTarget = null;
        if (action != null) {
            actionTarget = random(
                    engine.buildActionRange(
                    personId,
                    action,
                    movimentTarget));
        }

        return new CpuCommand(movimentTarget, action, actionTarget);
    }

    private Action randomAction(BattleEngine engine, int personId) {
        java.util.Map<Action, Boolean> classifiedActions = engine.classifyPersonActions(personId);
        List<Action> actions = new ArrayList<Action>(classifiedActions.size());
        for (java.util.Map.Entry<Action, Boolean> e : classifiedActions.entrySet()) {
            if (e.getValue()) {
                actions.add(e.getKey());
            }
        }
        actions.add(null);
        return random(actions);
    }

    private <T> T random(Collection<T> coordinates) {
        Object[] targets = coordinates.toArray(new Object[0]);
        return (T) targets[new Random().nextInt(targets.length)];
    }

    public String getName() {
        return "Random";
    }
}
