package net.stuffrepos.tactics16.scenes.battle;

import java.util.Collection;
import java.util.List;
import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.battlegameengine.Map;

/**
 *
 * @author eduardo
 */
public class BattleGameEvents {
    
    private BattleGameEvents() {
    }

    public static interface Event {
    }

    public static class NewTurn implements Event {

        private final int currentTurn;

        public NewTurn(int currentTurn) {
            this.currentTurn = currentTurn;
        }
    }

    public static class PersonsActOrderDefined implements Event {

        private final List<Integer> persons;

        public PersonsActOrderDefined(List<Integer> persons) {
            this.persons = persons;
        }
    }
    
    
    public static class NewTurnNotifiy implements Event {
        
        private final int currentTurn;
        
        public NewTurnNotifiy(int currentTurn) {
            this.currentTurn = currentTurn;
        }
        
        public int getCurrentTurn() {
            return currentTurn;
        }
    }
    
    public static class PersonsActOrderDefinedNotifiy implements Event {
        
        private final List<Integer> persons;
        
        public PersonsActOrderDefinedNotifiy(List<Integer> persons) {
            this.persons = persons;
        }
    }
    
    public static class SelectedPersonNotify implements Event {
        
        private final Integer selectedPerson;
        
        public SelectedPersonNotify(Integer selectedPerson) {
            this.selectedPerson = selectedPerson;
        }
    }
    
    public static class OutOfReachMovimentNotifiy implements Event {
        
        private final Map.MapCoordinate coordinate;
        private final Integer person;
        
        public OutOfReachMovimentNotifiy(Integer person, Map.MapCoordinate coordinate) {
            this.person = person;
            this.coordinate = coordinate;
        }
    }
    
    public static class PersonMovedNotify implements Event {
        
        public PersonMovedNotify(Integer person, Map.MapCoordinate originalPosition, Map.MapCoordinate movimentTarget) {
        }
    }
    
    public static class MovimentCancelledNotifiy implements Event {
        
        public MovimentCancelledNotifiy(Integer person, Map.MapCoordinate originalPosition, Map.MapCoordinate movimentTarget) {
        }
    }
    
    public static class SelectedActionNotify implements Event {
        
        public SelectedActionNotify(Integer person, Action selectedAction) {
        }
    }
    
    public static class NotEnabledActionNotify implements Event {
        
        public NotEnabledActionNotify(Integer person, Action selectedAction) {
        }
    }
    
    public static class ChooseActionCancelled implements Event {
        
        public ChooseActionCancelled(Integer person, Action selectedAction) {
        }
    }
    
    public static class ConfirmCancelledNotify implements Event {
        
        public ConfirmCancelledNotify(Integer person, Action selectedAction, Map.MapCoordinate actionTarget) {
        }
    }
    
    public static class PerformedActionNotify implements Event {
        
        public PerformedActionNotify(Integer agentPerson, Action action, Map.MapCoordinate target, Collection<Map.MapCoordinate> buildActionReachRay, Collection<Integer> affectedPersons, int agentLostSpecialPoints, int agentLostHealthPoints) {
        }
    }
    
    public static class OutOfReachNotify implements Event {
        
        public OutOfReachNotify(Integer person, Action selectedAction) {
        }
    }
    
    public static class ChoosedTarget implements Event {
        
        public ChoosedTarget(Integer person, Map.MapCoordinate actionTarget) {
        }
    }
    
    public static class ActionAffectedPersonNotify implements Event {
        
        public ActionAffectedPersonNotify(Integer affectedPerson, boolean hits, int damage, boolean affectedPersonIsAlive) {
        }
    }
    
    public static class WinningPlayerNotify implements Event {
        
        public WinningPlayerNotify(Integer winnerPlayer) {
        }
    }
}
