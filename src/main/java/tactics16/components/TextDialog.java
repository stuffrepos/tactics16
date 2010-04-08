package tactics16.components;

import tactics16.MyGame;
import tactics16.game.Coordinate;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TextDialog implements Object2D {

    private static final int H_MARGIN = 5;
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLUE;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final Coordinate position = new Coordinate();
    private Integer minHeight;
    private Integer maxHeight;
    private Integer minWidth;
    private Integer maxWidth;
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    private String text = "";

    public static int getDefaultHeight() {
        return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight();
    }

    public int getWidth() {
        String[] textLines = text.split("\n");

        int lineMaxWidth = 0;
        for (String line : textLines) {
            int lineWidth = MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().stringWidth(line);
            if (lineWidth > lineMaxWidth) {
                lineMaxWidth = lineWidth;
            }
        }

        int w = lineMaxWidth + H_MARGIN * 2;

        if (getMinWidth() != null && w < getMinWidth()) {
            w = getMinWidth();
        } else if (getMaxWidth() != null && w > getMaxWidth()) {
            w = getMaxWidth();
        }

        return w;
    }

    public int getHeight() {
        String[] textLines = text.split("\n");
        int h = MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight() * textLines.length;

        if (getMinHeight() != null && h < getMinHeight()) {
            h = getMinHeight();
        } else if (getMaxHeight() != null && h > getMaxHeight()) {
            h = getMaxHeight();
        }
        return h;
    }

    public void render(Graphics2D g) {

        String[] textLines = text.split("\n");

        int w = this.getWidth();
        int h = this.getHeight();

        g.setColor(this.getBackgroundColor());
        g.fillRect(getPosition().getX(), getPosition().getY(), w - 1, h - 1);
        g.setColor(this.getForegroundColor());
        g.drawRect(getPosition().getX(), getPosition().getY(), w - 1, h - 1);

        int i = 0;
        for (String line : textLines) {
            g.drawString(
                    line,
                    getPosition().getX() + H_MARGIN,
                    getPosition().getY() + g.getFontMetrics().getHeight() * i + g.getFontMetrics().getAscent() +
                    g.getFontMetrics().getLeading());
            i++;
        }

    }

    public Coordinate getPosition() {
        return position;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }
    
    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTop() {
        return this.position.getY();
    }

    public int getLeft() {
        return this.position.getX();
    }

    public void setWidth(int width) {
        this.setMinWidth(width);
        this.setMaxWidth(width);
    }
}
