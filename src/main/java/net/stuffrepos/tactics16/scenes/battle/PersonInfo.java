package net.stuffrepos.tactics16.scenes.battle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.animation.TextMovimentAnimation;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.Align;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PersonInfo implements VisualEntity {

    private static final Color BACKGROUND_COLOR = new Color(0x77000000);
    private static final double SPEED = 0.05;
    private TextMovimentAnimation textMovimentAnimation;

    public PersonInfo(Person person, Type type, String text) {
        textMovimentAnimation = new TextMovimentAnimation(
                new Coordinate(person.getPosition(), 0, -Map.TERRAIN_SIZE),
                new Coordinate(person.getPosition(), 0, -Map.TERRAIN_SIZE * 2),
                500,
                SPEED);
        textMovimentAnimation.getDialog().setText(text);
        textMovimentAnimation.getDialog().setHorizontalAlign(Align.NULL);
        textMovimentAnimation.getDialog().setVerticalAlign(Align.NULL);
        textMovimentAnimation.getDialog().setForegroundColor(type.getColor());
        textMovimentAnimation.getDialog().setBackgroundColor(BACKGROUND_COLOR);
        textMovimentAnimation.getDialog().setFlat(true);
    }

    public void update(long elapsedTime) {
        textMovimentAnimation.update(elapsedTime);
    }

    public void render(Graphics g) {
        textMovimentAnimation.render(g);
    }

    public boolean isFinalized() {
        return textMovimentAnimation.isFinalized();
    }

    public static enum Type {

        DAMAGE(Color.red),
        NEUTRAL(Color.white),
        AGILITY(Color.green);
        private final Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
