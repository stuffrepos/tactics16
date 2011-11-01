package net.stuffrepos.tactics16.components;

import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.Align;
import net.stuffrepos.tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PropertyBox implements VisualEntity, Object2D {

    private Text propertyName = new Text();
    private Text propertyValue = new Text();
    private Coordinate position = new Coordinate();
    private int width;

    public PropertyBox(String propertyName, String propertyValue, int width) {
        this.propertyName.setText(propertyName);
        this.propertyValue.setText(propertyValue);
        this.propertyValue.setAlign(Align.POSITIVE);
        this.width = width;

        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                PropertyBox.this.propertyName.getPosition().setXY(
                        source.getX(),
                        source.getY());
                PropertyBox.this.propertyValue.getPosition().setXY(
                        source.getX() + PropertyBox.this.width,
                        source.getY());
            }
        });
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return propertyName.getHeight();
    }

    public void update(long elapsedTime) {
        propertyName.update(elapsedTime);
        propertyValue.update(elapsedTime);
    }

    public void render(Graphics g) {
        propertyName.render(g);
        propertyValue.render(g);
    }

    public boolean isFinalized() {
        return false;
    }
}
