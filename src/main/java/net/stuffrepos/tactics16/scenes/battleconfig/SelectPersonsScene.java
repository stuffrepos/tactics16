package net.stuffrepos.tactics16.scenes.battleconfig;

import net.stuffrepos.tactics16.scenes.battle.BattleGame;
import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.MenuOption;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Job;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.components.JobBoxInfo;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectPersonsScene extends Phase {

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        currentPlayer = 0;
    }

    private class JobOption implements MenuOption {

        private final Job job;
        private CacheableMapValue<PlayerToBattle, JobBoxInfo> infoBoxes = new CacheableMapValue<PlayerToBattle, JobBoxInfo>() {
            @Override
            protected JobBoxInfo calculate(PlayerToBattle player) {
                return new JobBoxInfo(job, player.getPlayerConfig());
            }
        };

        public JobOption(Job job) {
            this.job = job;
        }

        @Override
        public void executeAction() {
            getCurrentPlayer().addPerson(
                    new PersonToBattle(
                    getCurrentPlayer(),
                    String.format(
                    "Person %d.%d",
                    currentPlayer + 1,
                    getCurrentPlayer().getPersons().size() + 1),
                    getJob()));

            if (getCurrentPlayer().getPersons().size()
                    >= map.getPlayerInitialPosition(currentPlayer).size()) {
                currentPlayer++;
            }

            if (currentPlayer >= map.getPlayerCount()) {
                MyGame.getInstance().getPhaseManager().advance(new BattleScene(buildBattleGame()));
            }

            updateStatusDialog();
            updateJobInfoBox();
        }

        public Job getJob() {
            return job;
        }

        public String getText() {
            return job.getName();
        }

        public GameKey getKey() {
            return null;
        }

        public boolean isEnabled() {
            return true;
        }

        public JobBoxInfo getJobBoxInfo(PlayerToBattle player) {
            return infoBoxes.getValue(player);
        }
    }// </editor-fold>
    private int currentPlayer = 0;
    private TextBox statusDialog = new TextBox();
    private PhaseTitle title;
    private JobBoxInfo jobBoxInfo = null;
    private Menu jobSelector = new Menu();
    private final List<PlayerToBattle> players;
    private final Map map;

    public SelectPersonsScene(Map map, List<PlayerToBattle> players) {
        assert map != null;
        this.map = map;
        this.players = players;
    }

    private BattleGame buildBattleGame() {
        return new BattleGame(map, players);
    }

    private PlayerToBattle getCurrentPlayer() {
        if (players != null && currentPlayer < players.size()) {
            return players.get(currentPlayer);
        } else {
            return null;
        }
    }

    @Override
    public void initResources(GameContainer container, StateBasedGame game) {

        title = new PhaseTitle("Select Persons");

        jobSelector.clear();

        for (Job job : MyGame.getInstance().getLoader().getJobs()) {
            jobSelector.addOption(new JobOption(job));
            updateJobInfoBox();
        }

        jobSelector.addOption(new CommonMenuOption("Back", GameKey.CANCEL) {
            @Override
            public void executeAction() {
                MyGame.getInstance().getPhaseManager().back();
            }
        });

        jobSelector.getPosition().setXY(Layout.OBJECT_GAP, Layout.getBottomGap(title));

        statusDialog.setMinWidth(50);

        jobSelector.getCursor().getCursor().addListener(new Listener<Cursor1D>() {
            public void onChange(Cursor1D source) {
                updateJobInfoBox();
            }
        });

        jobSelector.addGeometryListener(new Listener<Object2D>() {
            public void onChange(Object2D source) {
                statusDialog.getPosition().setXY(
                        Layout.getRightGap(source),
                        source.getTop());
            }
        });

        statusDialog.addGeometryListener(new Listener<Object2D>() {
            public void onChange(Object2D source) {
                updateJobInfoBoxPosition();
            }
        });
    }

    private void updateStatusDialog() {
        StringBuffer buffer = new StringBuffer();

        for (PlayerToBattle player : players) {
            buffer.append(player.getName());
            buffer.append('\n');
            for (PersonToBattle person : player.getPersons()) {
                buffer.append("- ");
                buffer.append(person.getName());
                buffer.append(" : ");
                buffer.append(person.getJob().getName());
                buffer.append('\n');
            }
        }
        statusDialog.setText(buffer.toString());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        jobSelector.update(delta);
        if (jobBoxInfo != null) {
            jobBoxInfo.update(delta);
        }
        updateStatusDialog();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        title.render(g);
        statusDialog.render(g);
        jobSelector.render(g);
        if (jobBoxInfo != null) {
            jobBoxInfo.render(g);
        }
    }

    private void updateJobInfoBoxPosition() {
        if (jobBoxInfo != null) {
            jobBoxInfo.getPosition().setXY(
                    Layout.getRightGap(statusDialog),
                    statusDialog.getTop());
        }
    }

    private void updateJobInfoBox() {
        if (jobSelector.getCursor().getSelected() instanceof JobOption
                && getCurrentPlayer() != null) {
            jobBoxInfo = ((JobOption) jobSelector.getCursor().getSelected()).getJobBoxInfo(
                    getCurrentPlayer());
            updateJobInfoBoxPosition();
        } else {
            jobBoxInfo = null;
        }
        updateJobInfoBoxPosition();
    }
}
