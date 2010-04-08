package tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class MenuOption {

    private String text;
    private Integer key;

    public MenuOption(String text) {
        this(text, null);
    }

    public MenuOption(String text, Integer key) {
        this.text = text;
        this.key = key;
    }

    public abstract void executeAction();

    public String getText() {
        return text;
    }

    public void render(Graphics2D g, boolean selected, int x, int y, int w, int h) {
        int optionW = g.getFontMetrics().stringWidth(this.getText());
        if (isEnabled()) {
            g.setColor(selected ? Color.BLACK : Color.WHITE);
        } else {
            g.setColor(Color.DARK_GRAY);
        }

        g.fillRect(x, y, w, h - 1);
        if (isEnabled()) {
            g.setColor(selected ? Color.WHITE : Color.BLACK);
        } else {
            g.setColor(Color.GRAY);
        }
        g.drawRect(x, y, w, h - 1);
        g.drawString(
                this.getText(),
                x + (w - optionW) / 2,
                y + g.getFontMetrics().getAscent() + g.getFontMetrics().getLeading());
    }

    public Integer getKey() {
        return key;
    }

    public boolean isEnabled() {
        return true;
    }
}
