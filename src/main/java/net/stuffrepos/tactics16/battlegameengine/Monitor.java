package net.stuffrepos.tactics16.battlegameengine;

import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface Monitor {

    public void notify(BattleNotify notify);
    
    public <T> T request(BattleRequest<T> request);
}
