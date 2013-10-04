package net.stuffrepos.tactics16;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleGame;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battleconfig.ControllerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.CpuControllerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.HumanControllerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.PersonToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.PlayerToBattle;
import net.stuffrepos.tactics16.scenes.battleconfig.cpuia.NormalCpuIa;
import net.stuffrepos.tactics16.scenes.battleconfig.cpuia.RandomCpuIa;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TestBattleScene extends Phase {

    private static Random random = new Random();
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        super.init(container, game);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        MyGame.getInstance().getPhaseManager().change(buildBattleScene());
    }

    private static BattleScene buildBattleScene() {
        return new BattleScene(battleGame());
    }

    private static BattleGame battleGame() {
        Map map = buildMap();
        return new BattleGame(map, createPlayers(map));
    }

    private static Map buildMap() {
        Map map = new Map("Direct", 8, 8);
        map.setPersonInitialPosition(0, new Coordinate(0, 0));
        map.setPersonInitialPosition(0, new Coordinate(0, 1));
        map.setPersonInitialPosition(0, new Coordinate(1, 0));
        map.setPersonInitialPosition(0, new Coordinate(1, 1));
        map.setPersonInitialPosition(1, new Coordinate(6, 6));
        map.setPersonInitialPosition(1, new Coordinate(6, 7));
        map.setPersonInitialPosition(1, new Coordinate(7, 6));
        map.setPersonInitialPosition(1, new Coordinate(7, 7));
        return map;
    }

    private static List<PlayerToBattle> createPlayers(Map map) {
        List<PlayerToBattle> players = new LinkedList<PlayerToBattle>();
        for (int playerId = 0; playerId < map.getPlayerCount(); ++playerId) {
            ControllerToBattle controller = playerId % 2 == 0
                    ? new HumanControllerToBattle()
                    : new CpuControllerToBattle(RandomCpuIa.getInstance());
            PlayerToBattle player = new PlayerToBattle(PlayerConfig.getPlayer(playerId), controller, "PlayerConfig " + (playerId + 1));
            for (int person = 0; person < map.getPlayerInitialPosition(playerId).size(); ++person) {
                player.addPerson(createPerson(player));
            }
            players.add(player);
        }
        return players;
    }

    private static PersonToBattle createPerson(PlayerToBattle player) {
        return new PersonToBattle(player, "Person " + (1 + (int) (Math.random() * 98)), getJob());
    }

    private static Job getJob() {
        Job[] jobs = MyGame.getInstance().getLoader().getJobs().getValues().toArray(new Job[0]);
        if (jobs.length > 0) {
            return jobs[random.nextInt(jobs.length)];
        } else {
            throw new RuntimeException("No job found");
        }
    }
}
