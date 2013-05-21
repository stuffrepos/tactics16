package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.image.DrawerUtil;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
    private final PlayerConfig player;    

    public JobBoxInfo(Job job, PlayerConfig player) {
        assert player != null;
        assert job != null;
        this.player = player;        
        jobAnimationBox = new AnimationBox(player.getSpriteAnimation(job, Job.GameAction.STOPPED));
        name = new Text(job.getName());
        defense = new PropertyBox("Defense", Integer.toString(job.getResistence()), getWidth());
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

    public void update(int delta) {
        board.update(delta);
    }

    public void render(Graphics g) {
        g.setColor(getBoxColor());
        DrawerUtil.fill3dRect(g, getLeft(), getTop(), getWidth(), getHeight(), true);
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