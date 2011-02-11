package net.stuffrepos.tactics16.scenes;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.scenes.battle.BattleScene;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.menu.Menu;
import net.stuffrepos.tactics16.components.menu.MenuOption;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.scenes.battle.BattleGame;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.scenes.battle.Player;
import java.awt.Graphics2D;
import net.stuffrepos.tactics16.components.JobBoxInfo;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.components.PhaseTitle;
import net.stuffrepos.tactics16.components.menu.CommonMenuOption;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.util.cache.CacheableMapValue;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectPersonsScene extends AbstractPhase {

    @Override
    public void onEnter() {
        battleGame.resetPlayers();
        currentPlayer = 0;
    }

    // <editor-fold defaultstate="collapsed" desc="class JobOption">
    private class JobOption implements MenuOption {

        private final Job job;
        private CacheableMapValue<Player, JobBoxInfo> infoBoxes = new CacheableMapValue<Player, JobBoxInfo>() {

            @Override
            protected JobBoxInfo calculate(Player key) {
                return new JobBoxInfo(job, key);
            }
        };

        public JobOption(Job job) {
            this.job = job;
        }

        @Override
        public void executeAction() {
            getCurrentPlayer().getPersons().add(
                    new Person(
                    getCurrentPlayer(),
                    String.format(
                    "Person %d.%d",
                    currentPlayer + 1,
                    getCurrentPlayer().getPersons().size() + 1), getJob()));

            if (getCurrentPlayer().getPersons().size() >=
                    battleGame.getMap().getPlayerInitialPosition(currentPlayer).size()) {
                currentPlayer++;
            }

            if (currentPlayer >= battleGame.getMap().getPlayerCount()) {
                MyGame.getInstance().getPhaseManager().advance(new BattleScene(SelectPersonsScene.this.battleGame));
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

        public JobBoxInfo getJobBoxInfo(Player player) {
            return infoBoxes.getValue(player);
        }
    }// </editor-fold>
    private BattleGame battleGame;
    private int currentPlayer = 0;
    private TextBox statusDialog = new TextBox();
    private PhaseTitle title;
    private JobBoxInfo jobBoxInfo = null;
    private Menu jobSelector = new Menu();

    public BattleGame getGeoGame() {
        return battleGame;
    }

    public SelectPersonsScene(SelectMapScene selectMapScene) {
        this.battleGame = new BattleGame(selectMapScene.getSelectedMap());
    }

    private Player getCurrentPlayer() {        
        if (currentPlayer < battleGame.getMap().getPlayerCount()) {
            return this.battleGame.getPlayer(currentPlayer);
        } else {
            return null;
        }
    }

    @Override
    public void onAdd() {

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

        for (Player player : battleGame.getPlayers()) {
            buffer.append(player.getName());
            buffer.append('\n');
            for (Person person : player.getPersons()) {
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
    public void update(long elapsedTime) {
        jobSelector.update(elapsedTime);
        if (jobBoxInfo != null) {
            jobBoxInfo.update(elapsedTime);
        }
        updateStatusDialog();
    }

    @Override
    public void render(Graphics2D g) {
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
        if (jobSelector.getCursor().getSelected() instanceof JobOption &&
                getCurrentPlayer() != null) {
            jobBoxInfo = ((JobOption) jobSelector.getCursor().getSelected()).getJobBoxInfo(
                    getCurrentPlayer());
            updateJobInfoBoxPosition();
        } else {
            jobBoxInfo = null;
        }
        updateJobInfoBoxPosition();
    }
}
