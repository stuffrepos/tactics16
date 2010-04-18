package tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CommonMenuOption {

    private static final Color ENABLED_FOREGROUND_COLOR = new Color(0x000077);
    private static final Color ENABLED_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DISABLED_FOREGROUND_COLOR = Color.GRAY;
    private static final Color DISABLED_BACKGROUND_COLOR = Color.DARK_GRAY;
    
    private String text;
    private Integer key;

    public CommonMenuOption(String text) {
        this(text, null);
    }

    public CommonMenuOption(String text, Integer key) {
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
            g.setColor(selected ? ENABLED_FOREGROUND_COLOR : ENABLED_BACKGROUND_COLOR);
        } else {
            g.setColor(DISABLED_BACKGROUND_COLOR);
        }

        g.fillRect(x, y, w, h - 1);
        if (isEnabled()) {
            g.setColor(selected ? ENABLED_BACKGROUND_COLOR: ENABLED_FOREGROUND_COLOR);
        } else {
            g.setColor(DISABLED_FOREGROUND_COLOR);
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
