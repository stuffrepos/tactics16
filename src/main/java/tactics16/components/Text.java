package tactics16.components;

import tactics16.util.Align;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import tactics16.MyGame;
import tactics16.game.Coordinate;
import tactics16.util.cache.CacheableValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Text implements Object2D {

    public static final Font MAIN_TITLE_FONT = new Font("purisa", Font.BOLD, 48);
    public static final Font PHASE_TITLE_FONT = new Font("purisa", Font.BOLD, 16);
    public static final int MAX_MARGIN = 10;
    private Align align = Align.NEGATIVE;
    public static final Color DEFAULT_COLOR = Color.WHITE;
    private Font font = MyGame.getInstance().getFont();
    private String text;
    private Coordinate position = new Coordinate();
    private Color color;
    private CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {

            int lineMaxWidth = 0;
            for (String line : textLines.getValue()) {
                int lineWidth = getLineWidth(line);
                if (lineWidth > lineMaxWidth) {
                    lineMaxWidth = lineWidth;
                }
            }

            return lineMaxWidth + getMargin() * 2;
        }
    };
    private CacheableValue<Integer> height = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics(getFont()).getHeight() *
                    textLines.getValue().length + 2 * getMargin();
        }
    };
    private CacheableValue<String[]> textLines = new CacheableValue<String[]>() {

        @Override
        protected String[] calculate() {
            if (text == null) {
                return new String[0];
            } else {
                return text.replace("\t", "   ").split("\n");
            }
        }
    };

    public void setColor(Color color) {
        this.color = color;
    }

    private int getLineWidth(String line) {
        return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics(getFont()).stringWidth(line);
    }

    public void render(Graphics2D g) {

        g.setColor(color);
        g.setFont(getFont());

        int i = 0;
        for (String line : textLines.getValue()) {
            int x = position.getX() + getMargin();
            switch (align) {
                case POSITIVE:
                    x += getWidth() - getLineWidth(line);
                    break;

                case NULL:
                    x += (getWidth() - getLineWidth(line)) / 2;
                    break;

                case NEGATIVE:
                default:
                    break;
            }
            g.drawString(
                    line,
                    x,
                    position.getY() + getMargin() + g.getFontMetrics(getFont()).getHeight() * i + g.getFontMetrics(getFont()).getAscent() +
                    g.getFontMetrics(getFont()).getLeading());
            i++;
        }
    }

    private int getMargin() {
        return Math.min(MAX_MARGIN, MyGame.getInstance().getDefaultGraphics2D().getFontMetrics(font).getHeight() / 2);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textLines.clear();
        width.clear();
        height.clear();
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
        return height.getValue();
    }

    public Color getColor() {
        return color;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        this.width.clear();
        this.height.clear();
    }
}
