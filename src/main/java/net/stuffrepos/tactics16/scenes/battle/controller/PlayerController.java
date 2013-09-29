package net.stuffrepos.tactics16.scenes.battle.controller;

import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface PlayerController {

    public EventProcessor getEventProcessor(BattleScene battleScene, BattleRequest request);
}
