package net.stuffrepos.tactics16.scenes.battle;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface PlayerControl {

    public static interface Receiver {

        public void onSelectAgent(Person agent);
    }

    public void onSelectAgent(BattleGame battleGame) throws Exception;
}
