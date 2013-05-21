package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import net.stuffrepos.tactics16.util.image.DrawerUtil;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonBoxInfo implements VisualEntity, Object2D {

    private Coordinate position = new Coordinate();
    private final Text name;
    private final PropertyBox healthPoints;
    private final PropertyBox specialPoints;
    private final PropertyBox speedPoints;
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
    private final Person person;

    public PersonBoxInfo(Person person) {
        assert person != null;
        this.person = person;
        jobAnimationBox = new AnimationBox(person.getPlayer().getPlayerConfig().getSpriteAnimation(person.getJob(), Job.GameAction.STOPPED));
        name = new Text(person.getName());
        healthPoints = new PropertyBox("HP", person.getEnginePerson().getHealthPoints() + "/" + person.getEnginePerson().getMaximumHealthPoints(), getWidth());
        specialPoints = new PropertyBox("SP", person.getEnginePerson().getSpecialPoints() + "/" + person.getEnginePerson().getMaximumSpecialPoints(), getWidth());
        speedPoints = new PropertyBox("SpP", String.format("%.1f/%.1f", person.getEnginePerson().getSpeedPoints(), person.getEnginePerson().getSpeed()), getWidth());

        board.getChildren().add(jobAnimationBox);
        board.getChildren().add(name);
        board.getChildren().add(healthPoints);
        board.getChildren().add(specialPoints);
        board.getChildren().add(speedPoints);

        position.addListener(new Listener<Coordinate>() {
            public void onChange(Coordinate source) {
                jobAnimationBox.getPosition().set(source, Layout.OBJECT_GAP, Layout.OBJECT_GAP);
                name.getPosition().setXY(
                        Layout.getRightGap(jobAnimationBox),
                        jobAnimationBox.getTop());
                healthPoints.getPosition().setXY(
                        source.getX(),
                        Layout.getBottom(jobAnimationBox));
                specialPoints.getPosition().setXY(
                        healthPoints.getLeft(),
                        Layout.getBottom(healthPoints));
                speedPoints.getPosition().setXY(
                        healthPoints.getLeft(),
                        Layout.getBottom(specialPoints));
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
        return Layout.OBJECT_GAP
                + jobAnimationBox.getHeight()
                + healthPoints.getHeight()
                + specialPoints.getHeight()
                + speedPoints.getHeight();
    }

    public Coordinate getPosition() {
        return position;
    }

    private Color getBoxColor() {
        return person.getPlayer().getPlayerConfig().getDefaultColor();
    }

    public Person getPerson() {
        return person;
    }
}