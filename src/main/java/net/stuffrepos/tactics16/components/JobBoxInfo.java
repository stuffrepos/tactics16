package net.stuffrepos.tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.scenes.battle.Player;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobBoxInfo implements VisualEntity, Object2D {
    
    private Coordinate position = new Coordinate();
    private final Text name;
    private final PropertyBox defense;
    private final PropertyBox evasiveness;
    private static final int MIN_WIDTH = 120;
    private EntitiesBoard board = new EntitiesBoard();
    private AnimationBox jobAnimationBox;    
    private CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            return Math.max(
                    MIN_WIDTH,
                    jobAnimationBox.getWidth() + Layout.OBJECT_GAP * 3 + name.getWidth());
        }
    };
    private final Player player;
    private final Job job;

    public JobBoxInfo(Job job, Player player) {
        assert player != null;
        assert job != null;
        this.player = player;
        this.job = job;
        jobAnimationBox = new AnimationBox(player.getSpriteAnimation(job, Job.GameAction.STOPPED));        
        name = new Text(job.getName());
        defense = new PropertyBox("Defense", Integer.toString(job.getDefense()), getWidth());
        evasiveness = new PropertyBox("Evasiveness", Integer.toString(job.getEvasiveness()), getWidth());

        board.getChildren().add(jobAnimationBox);
        board.getChildren().add(name);
        board.getChildren().add(defense);
        board.getChildren().add(evasiveness);

        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                jobAnimationBox.getPosition().set(source, Layout.OBJECT_GAP, Layout.OBJECT_GAP);
                name.getPosition().setXY(
                        Layout.getRightGap(jobAnimationBox),
                        jobAnimationBox.getTop());
                defense.getPosition().setXY(
                        source.getX(),
                        Layout.getBottom(jobAnimationBox));
                evasiveness.getPosition().setXY(
                        defense.getLeft(),
                        Layout.getBottom(defense));
            }
        });

    }

    public void update(long elapsedTime) {
        board.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        g.setColor(getBoxColor());
        g.fill3DRect(getLeft(), getTop(), getWidth(), getHeight(), true);        
        board.render(g);
    }

    public boolean isFinalized() {
        return false;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return width.getValue();
    }

    public int getHeight() {
        return name.getHeight() + defense.getHeight() + evasiveness.getHeight() + jobAnimationBox.getHeight();
    }

    public Coordinate getPosition() {
        return position;
    }

    private Color getBoxColor() {
        return player.getDefaultColor();
    }
}