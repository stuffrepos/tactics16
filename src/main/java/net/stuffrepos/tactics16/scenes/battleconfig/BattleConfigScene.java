package net.stuffrepos.tactics16.scenes.battleconfig;

import java.util.ArrayList;
import java.util.Arrays;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import net.stuffrepos.tactics16.scenes.MainMenuScene;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleConfigScene extends Phase {

    private SelectMapScene selectMapScene = new SelectMapScene(new Runnable() {
        public void run() {
            onMapSelected(selectMapScene.getSelectedMap());
        }
    }, true);
    private Map map;
    private PlayerToBattle[] players;
    private ControllerToBattle[] controllers;

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        controllers = new ControllerToBattle[]{
            new HumanControllerToBattle(),
            new CpuControllerToBattle()
        };
        MyGame.getInstance().getPhaseManager().advance(selectMapScene);
    }

    private void onMapSelected(Map map) {
        if (map == null) {
            MyGame.getInstance().getPhaseManager().change(MainMenuScene.getInstance());
        } else {
            this.map = map;
            this.players = new PlayerToBattle[map.getPlayerCount()];
            selectNextController(0);
        }
    }

    private void selectNextController(int currentPlayer) {
        if (currentPlayer < players.length) {
            final SelectControllerScene controllerScene = new SelectControllerScene(currentPlayer, controllers);
            controllerScene.setCallback(new Runnable() {
                public void run() {
                    onSelectController(
                            controllerScene);
                }
            });
            MyGame.getInstance().getPhaseManager().advance(controllerScene);
        } else {
            MyGame.getInstance().getPhaseManager().advance(new SelectPersonsScene(map, Arrays.asList(players)));
        }

    }

    private void onSelectController(SelectControllerScene controllerScene) {
        if (controllerScene.getSelectedPlayer() == null) {
            MyGame.getInstance().getPhaseManager().back();
        } else {
            players[controllerScene.getPlayerIndex()] = controllerScene.getSelectedPlayer();
            selectNextController(controllerScene.getPlayerIndex() + 1);
        }
    }
}
