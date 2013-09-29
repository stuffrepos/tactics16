package net.stuffrepos.tactics16.battleengine.events;

import java.util.Collection;
import net.stuffrepos.tactics16.battleengine.BattleRequest;
import net.stuffrepos.tactics16.battleengine.EnginePerson;
import net.stuffrepos.tactics16.battleengine.Map;
import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MovimentTargetRequest implements BattleRequest<MapCoordinate> {

    private final MapCoordinate originalPosition;
    private final Collection<MapCoordinate> movimentRange;
    private final Map map;
    private final EnginePerson person;

    public MovimentTargetRequest(EnginePerson person, Map map, MapCoordinate originalPosition, Collection<MapCoordinate> movimentRange) {
        this.person = person;
        this.map = map;
        this.originalPosition = originalPosition;
        this.movimentRange = movimentRange;

    }

    public Collection<MapCoordinate> getMovimentRange() {
        return movimentRange;
    }

    public MapCoordinate getOriginalPosition() {
        return originalPosition;
    }

    public Map getMap() {
        return map;
    }

    public EnginePerson getPerson() {
        return person;
    }

    public int getPlayer() {        
        return person.getPlayerId();
    }
}
