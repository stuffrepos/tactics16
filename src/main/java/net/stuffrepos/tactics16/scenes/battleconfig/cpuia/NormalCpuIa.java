package net.stuffrepos.tactics16.scenes.battleconfig.cpuia;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleData;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuCommand;
import net.stuffrepos.tactics16.scenes.battle.controller.cpu.CpuIa;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class NormalCpuIa implements CpuIa {

    private final Log log = LogFactory.getLog(NormalCpuIa.class);
    private static final NormalCpuIa instance = new NormalCpuIa();

    public static NormalCpuIa getInstance() {
        return instance;
    }
    private final Random random = new Random(System.currentTimeMillis());

    private NormalCpuIa() {
    }

    public CpuCommand buildCpuCommand(BattleEngine engine, int personId) {
        int maxValue = Integer.MIN_VALUE;
        List<CpuCommand> selected = null;
        for (CpuCommand command : findCommands(engine, personId)) {
            int value = calculateCommandValue(engine, personId, command);
            if (value >= maxValue) {
                if (value > maxValue) {
                    maxValue = value;
                    selected = new LinkedList<CpuCommand>();
                }
                selected.add(command);
            }

        }
        assert selected != null;
        assert !selected.isEmpty();

        return selected.get(random.nextInt(selected.size()));
    }

    private int calculateCommandValue(BattleEngine engine, int personId, CpuCommand command) {
        BattleData data = engine.cloneData();

        int friendAffected = 0;
        int enemyAffected = 0;
        int lostSpecialPoints = data.getDefinitions().actionLostSpecialPoints(personId, command.getAction());
        int lostHealthPoints = data.getDefinitions().actionLostHealthPoints(personId, command.getAction());
        float lostSpeedPoints = data.getDefinitions().actionLostSpeedPoints(personId, command.getAction());

        if (command.getAction() != null) {
            data.getPersonSet().getPerson(personId).setPosition(command.getMovimentTarget());
            for (int affected : data.getFinders().findAffectedActionPersons(command.getAction(), command.getActionTarget())) {
                if (engine.getPerson(affected).getPlayerId() == engine.getPerson(personId).getPlayerId()) {
                    friendAffected++;
                } else {
                    enemyAffected++;
                }
            }
        }

        return enemyAffected * 5
                - friendAffected
                - lostSpecialPoints
                - lostHealthPoints
                - ((int) (lostSpeedPoints * 2.0f));
    }

    public String getName() {
        return "Normal";
    }

    private Collection<CpuCommand> findCommands(BattleEngine engine, int personId) {
        List<CpuCommand> commands = new LinkedList<CpuCommand>();
        for (MapCoordinate movimentTarget : engine.buildMovimentRange(personId)) {
            commands.addAll(findCommandsByMovimentTarget(engine, personId, movimentTarget));
        }
        return commands;
    }

    private Collection<CpuCommand> findCommandsByMovimentTarget(BattleEngine engine, int personId, MapCoordinate movimentTarget) {
        List<CpuCommand> commands = new LinkedList<CpuCommand>();
        for (java.util.Map.Entry<Action, Boolean> e : engine.classifyPersonActions(personId).entrySet()) {
            if (e.getValue()) {
                commands.addAll(findCommandsByAction(engine, personId, movimentTarget, e.getKey()));
            }
        }
        commands.add(new CpuCommand(movimentTarget, null, null));
        return commands;
    }

    private Collection<CpuCommand> findCommandsByAction(BattleEngine engine, int personId, MapCoordinate movimentTarget, Action action) {
        List<CpuCommand> commands = new LinkedList<CpuCommand>();
        Collection<MapCoordinate> actionRange = engine.buildActionRange(
                personId,
                action,
                movimentTarget);
        for (MapCoordinate actionTarget : actionRange) {
            commands.add(new CpuCommand(movimentTarget, action, actionTarget));
        }
        return commands;
    }
}
