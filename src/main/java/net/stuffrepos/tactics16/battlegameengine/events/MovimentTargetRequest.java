package net.stuffrepos.tactics16.battlegameengine.events;

import java.util.Collection;
import net.stuffrepos.tactics16.battlegameengine.BattleRequest;
import net.stuffrepos.tactics16.battlegameengine.Map;
import net.stuffrepos.tactics16.battlegameengine.Map.MapCoordinate;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MovimentTargetRequest implements BattleRequest<MapCoordinate> {

    private final MapCoordinate originalPosition;
    private final Collection<MapCoordinate> movimentRange;
    private final Map map;
    private final Integer person;

    public MovimentTargetRequest(Integer person, Map map, MapCoordinate originalPosition, Collection<MapCoordinate> movimentRange) {
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

    public Integer getPerson() {
        return person;
    }
}
