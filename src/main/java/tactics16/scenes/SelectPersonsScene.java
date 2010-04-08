package tactics16.scenes;

import tactics16.Layout;
import tactics16.scenes.battle.BattleScene;
import tactics16.MyGame;
import tactics16.components.Menu;
import tactics16.components.MenuOption;
import tactics16.components.TextDialog;
import tactics16.scenes.battle.BattleGame;
import tactics16.game.Job;
import tactics16.game.Person;
import tactics16.game.Player;
import tactics16.phase.Phase;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class SelectPersonsScene implements Phase {

    public void onExit() {
    }

    public void onEnter() {
        geoGame.clearPlayers();
        currentPlayer = 0;
    }

    // <editor-fold defaultstate="collapsed" desc="class JobOption">
    private class JobOption extends MenuOption {

        private final Job job;

        public JobOption(Job job) {
            super(job.getName());
            this.job = job;
        }

        @Override
        public void executeAction() {
            getCurrentPlayer().getPersons().add(
                    new Person(
                    String.format(
                    "Person %d.%d",
                    currentPlayer + 1,
                    getCurrentPlayer().getPersons().size() + 1), getJob()));

            if (getCurrentPlayer().getPersons().size() >=
                    geoGame.getMap().getPlayerInitialPosition(currentPlayer).size()) {
                currentPlayer++;
            }

            if (currentPlayer >= geoGame.getMap().getPlayerCount()) {
                MyGame.getInstance().getPhaseManager().advance(new BattleScene(SelectPersonsScene.this.geoGame));
            }

            updateStatusDialog();
        }

        public Job getJob() {
            return job;
        }
    }// </editor-fold>
    private BattleGame geoGame;
    private int currentPlayer = 0;
    private TextDialog statusDialog;
    private TextDialog personStatus;
    private Menu jobSelector = new Menu() {

        @Override
        protected void onChangeSelectedOption() {
            updatePersonStatus();
        }
    };
    private final SelectMapScene selectMapScene;

    public BattleGame getGeoGame() {
        return geoGame;
    }

    public SelectPersonsScene(SelectMapScene selectMapScene) {
        this.selectMapScene = selectMapScene;
        this.geoGame = new BattleGame();
        this.geoGame.setMap(selectMapScene.getSelectedMap());
    }

    private Player getCurrentPlayer() {
        while (this.currentPlayer >= geoGame.getPlayers().size()) {
            geoGame.getPlayers().add(new Player("Player " + (geoGame.getPlayers().size() + 1)));
        }

        return this.geoGame.getPlayers().get(currentPlayer);
    }

    private Job getCurrentJob() {
        if (jobSelector.getCursor().getSelected() instanceof JobOption) {
            return ((JobOption) jobSelector.getCursor().getSelected()).getJob();
        } else {
            return null;
        }
    }

    public void onAdd() {

        jobSelector.clear();

        for (Job job : MyGame.getInstance().getLoader().getJobs()) {
            jobSelector.addOption(new JobOption(job));
        }

        jobSelector.addOption(new MenuOption("Back", KeyEvent.VK_ESCAPE) {

            @Override
            public void executeAction() {
                MyGame.getInstance().getPhaseManager().back();
            }
        });

        jobSelector.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);

        statusDialog = new TextDialog();
        statusDialog.setWidth(200);
        statusDialog.getPosition().setXY(
                Layout.getRightGap(jobSelector),
                jobSelector.getPosition().getX());

        personStatus = new TextDialog();
        personStatus.setWidth(200);
        personStatus.getPosition().setXY(
                Layout.getRightGap(statusDialog),
                jobSelector.getPosition().getX());
    }

    private void updateStatusDialog() {
        StringBuffer buffer = new StringBuffer();

        for (Player player : geoGame.getPlayers()) {
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

    private void updatePersonStatus() {

        Job job = this.getCurrentJob();

        if (job == null) {
            personStatus.setText("-- NO JOB --");
        } else {
            StringBuffer buffer = new StringBuffer();

            buffer.append("Job: " + job.getName() + "\n");
            buffer.append("\tDefense: " + job.getDefense());
            buffer.append('\n');
            buffer.append("\tAgility: " + job.getAgility());

            personStatus.setText(buffer.toString());
        }


    }

    public void onRemove() {
    }

    public void update(long elapsedTime) {
        jobSelector.update(elapsedTime);
        updatePersonStatus();
        updateStatusDialog();
    }

    public void render(Graphics2D g) {

        statusDialog.render(g);
        personStatus.render(g);
        jobSelector.render(g);
    }
}
