package net.stuffrepos.tactics16.battlegameengine.events;

import net.stuffrepos.tactics16.battlegameengine.BattleNotify;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class WinningPlayerNotify implements BattleNotify {

    private final int winnerPlayer;

    public WinningPlayerNotify(int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public int getWinnerPlayer() {
        return winnerPlayer;
    }
}
