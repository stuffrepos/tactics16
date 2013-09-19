package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.MenuOption;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectControllerScene extends Phase {

    private final int playerIndex;
    private final ControllerToBattle[] controllers;
    private PlayerToBattle selectedPlayer;
    private Runnable callback;
    private PhaseTitle title;
    private Menu controllerSelector;

    public SelectControllerScene(int playerIndex, ControllerToBattle[] controls) {
        this.playerIndex = playerIndex;
        this.controllers = controls;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {
        title = new PhaseTitle("Select controller for " + getPlayerName());
        controllerSelector = new Menu();
        for (final ControllerToBattle controller : controllers) {
            controllerSelector.addOption(new MenuOption() {
                public void executeAction() {
                    selectedPlayer = new PlayerToBattle(PlayerConfig.getPlayer(playerIndex), controller, getPlayerName());
                    callback.run();

                }

                public String getText() {
                    return controller.getName();
                }

                public GameKey getKey() {
                    return null;
                }

                public boolean isEnabled() {
                    return true;
                }
            });
        }

        controllerSelector.addOption(new CommonMenuOption("Cancel", GameKey.CANCEL) {
            @Override
            public void executeAction() {
                selectedPlayer = null;
                callback.run();
            }
        });

        controllerSelector.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));
    }

    public PlayerToBattle getSelectedPlayer() {
        return selectedPlayer;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        controllerSelector.update(delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        title.render(g);
        controllerSelector.render(g);
    }

    private String getPlayerName() {
        return "Player " + (playerIndex + 1);
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
