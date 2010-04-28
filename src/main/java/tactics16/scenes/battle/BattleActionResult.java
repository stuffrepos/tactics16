package tactics16.scenes.battle;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleActionResult {
    private Map<Person, Boolean> personsEvaded =
                    new HashMap<Person, Boolean>();
    private final BattleAction battleAction;

    public BattleActionResult(BattleAction battleAction) {
        this.battleAction = battleAction;
    }

    public void setPersonEvaded(Person person, boolean response) {
        personsEvaded.put(person, response);
    }

    public BattleAction getAction() {
        return battleAction;
    }

    public boolean isPersonEvaded(Person person) {
        return personsEvaded.containsKey(person);
    }

    public void applyResults() {
        getAction().getAgent().decreaseAgilityPoints(getAction().getAgentAgilityPoints());

        for(Person target: battleAction.getPersonsTargets()) {
            if (isPersonEvaded(target)) {
               target.decreaseAgilityPoints(getAction().getAgentAgilityPoints());
            }
            else {
                target.decreaseHealthPoints(getAction().calculateDamage(target));
            }
        }
    }
}
