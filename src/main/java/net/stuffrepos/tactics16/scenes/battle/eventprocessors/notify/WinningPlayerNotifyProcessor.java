package net.stuffrepos.tactics16.scenes.battle.eventprocessors.notify;

import net.stuffrepos.tactics16.battleengine.events.WinningPlayerNotify;
import net.stuffrepos.tactics16.components.MessageBox;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.scenes.battle.EventProcessor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class WinningPlayerNotifyProcessor extends EventProcessor<WinningPlayerNotify> {

    private static final int MESSAGE_BOX_TIME_OUT = 3000;

    public WinningPlayerNotifyProcessor(BattleScene battleScene) {
        super(battleScene);
    }

    public Phase init(final WinningPlayerNotify event) {
        return new Phase() {
            MessageBox messageBox;
            private long time;

            @Override
            public void init(GameContainer container, StateBasedGame game) throws SlickException {
                super.init(container, game);
                if (event.getWinnerPlayer() != null) {
                    messageBox = new MessageBox(String.format("Player %d is winner", event.getWinnerPlayer() + 1), getScene().getVisualBattleMap().getVisualMap(), MESSAGE_BOX_TIME_OUT);
                    messageBox.setBackgroundColor(PlayerConfig.getPlayer(event.getWinnerPlayer()).getDefaultColor());
                    messageBox.setForegroundColor(Color.white);
                } else {
                    messageBox = new MessageBox("Draw", getScene().getVisualBattleMap().getVisualMap(), MESSAGE_BOX_TIME_OUT);
                }

                this.time = 0;
            }

            @Override
            public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                super.update(container, game, delta);
                messageBox.update(delta);

                if (messageBox.isFinalized()) {
                    getScene().stopCurrentEventPhase();
                }
            }

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
                super.render(container, game, g);
                messageBox.render(g);
            }
        };
    }

    @Override
    public Class getEventClass() {
        return WinningPlayerNotify.class;
    }
}
