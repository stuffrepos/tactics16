package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.animation.EntitiesBoard;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.BattleEngine;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.scenes.battle.Person;
import net.stuffrepos.tactics16.util.image.DrawerUtil;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ActionBoxInfo implements VisualEntity, Object2D {

    private Coordinate position = new Coordinate();
    private final Text name;
    private final PropertyBox properties;
    private final EntitiesBoard board = new EntitiesBoard();
    private final int width;
    private final Person targetPerson;
    private final Action action;

    public ActionBoxInfo(BattleEngine engine, Action action, Person targetPerson, int width) {
        assert action != null;
        assert targetPerson != null;
        assert width >= 1;
        this.targetPerson = targetPerson;
        this.width = width;
        this.action = action;

        name = new Text(action.getName());
        board.getChildren().add(name);

        String damage = Integer.toString(engine.calculateDamage(
                action,
                targetPerson.getEnginePerson()));
        String hit = engine.hitProbability(
                action,
                targetPerson.getEnginePerson()) + "%";
        properties = new PropertyBox(
                new String[]{"Damage", "Hit"},
                new String[]{damage, hit},
                width);

        board.getChildren().add(properties);

        position.addListener(new Listener<Coordinate>() {
            public void onChange(Coordinate source) {
                name.getPosition().setXY(
                        Layout.getCentralizedLeft(name, ActionBoxInfo.this),
                        source.getDoubleY() + Layout.OBJECT_GAP);

                properties.getPosition().setXY(
                        source.getX(),
                        Layout.getBottom(name));
            }
        });

    }

    public void update(int delta) {
        board.update(delta);
    }

    public void render(Graphics g) {
        g.setColor(targetPerson.getPlayer().getPlayerConfig().getDefaultColor());
        DrawerUtil.fill3dRect(
                g,
                getLeft(), getTop(), getWidth(), getHeight(),
                true);
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
        return this.width;
    }

    public int getHeight() {
        return Layout.OBJECT_GAP + name.getHeight() + properties.getHeight();
    }

    public Coordinate getPosition() {
        return position;
    }

    public Person getPerson() {
        return targetPerson;
    }

    public Action getAction() {
        return action;
    }
}