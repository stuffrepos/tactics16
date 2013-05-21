package net.stuffrepos.tactics16.battleengine.events;

import net.stuffrepos.tactics16.battleengine.BattleNotify;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class WinningPlayerNotify implements BattleNotify {

    private final Integer winnerPlayer;

    public WinningPlayerNotify(Integer winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public Integer getWinnerPlayer() {
        return winnerPlayer;
    }
}
