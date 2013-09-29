package net.stuffrepos.tactics16.scenes.battle;

import net.stuffrepos.tactics16.battleengine.BattleRequest;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface PlayerControl {

    public EventProcessor getEventProcessor(BattleScene battleScene, BattleRequest request);
}
