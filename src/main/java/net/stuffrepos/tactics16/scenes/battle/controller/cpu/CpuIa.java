package net.stuffrepos.tactics16.scenes.battle.controller.cpu;

import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface CpuIa extends Nameable {

    public CpuCommand buildCpuCommand(BattleEngine engine, int personId);
}
