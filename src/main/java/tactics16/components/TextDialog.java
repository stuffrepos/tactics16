package tactics16.components;

import tactics16.MyGame;
import tactics16.game.Coordinate;
import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.util.CacheableValue;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TextDialog implements Object2D {

    private HorizontalAlign horizontalPosition = HorizontalAlign.LEFT;
    private VerticalAlign verticalPosition = VerticalAlign.TOP;
    private static final int MARGIN = 5;
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
    private final Coordinate position = new Coordinate();
    private final Coordinate size = new Coordinate();
    private Integer minHeight;
    private Integer maxHeight;
    private Integer minWidth;
    private Integer maxWidth;
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    private String text = "";
    private boolean flat = false;
    private CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            String[] textLines = text.split("\n");

            int lineMaxWidth = 0;
            for (String line : textLines) {
                int lineWidth = MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().stringWidth(line);
                if (lineWidth > lineMaxWidth) {
                    lineMaxWidth = lineWidth;
                }
            }

            int w = lineMaxWidth + MARGIN * 2;

            if (getMinWidth() != null && w < getMinWidth()) {
                w = getMinWidth();
            } else if (getMaxWidth() != null && w > getMaxWidth()) {
                w = getMaxWidth();
            }

            size.setX(w);

            return w;
        }
    };
    private CacheableValue<Integer> height = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            String[] textLines = text.split("\n");
            int h = MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight() * textLines.length + 2 * MARGIN;

            if (getMinHeight() != null && h < getMinHeight()) {
                h = getMinHeight();
            } else if (getMaxHeight() != null && h > getMaxHeight()) {
                h = getMaxHeight();
            }

            size.setY(h);
            return h;
        }
    };

    public static int getDefaultHeight() {
        return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight() + MARGIN * 2;
    }

    public void render(Graphics2D g) {

        String[] textLines = text.split("\n");

        int w = this.getWidth();
        int h = this.getHeight();

        Coordinate p = getPosition().clone();

        switch (horizontalPosition) {
            case CENTER:
                p.addX(-w / 2);
                break;
            case RIGHT:
                p.addX(-w);
                break;
        }

        switch (verticalPosition) {
            case MIDDLE:
                p.addY(-h / 2);
                break;
            case BOTTOM:
                p.addY(-h);
                break;
        }

        g.setColor(this.getBackgroundColor());
        if (flat) {
            g.fillRect(p.getX(), p.getY(), w - 1, h - 1);
        } else {
            g.fill3DRect(p.getX(), p.getY(), w - 1, h - 1, true);
        }
        g.setColor(this.getForegroundColor());

        int i = 0;
        for (String line : textLines) {
            g.drawString(
                    line,
                    p.getX() + MARGIN,
                    p.getY() + MARGIN + g.getFontMetrics().getHeight() * i + g.getFontMetrics().getAscent() +
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
        this.height.clear();
        this.minHeight = minHeight;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.height.clear();
        this.maxHeight = maxHeight;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.width.clear();
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.width.clear();
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
        this.width.clear();
        this.height.clear();
        this.text = text;
    }

    public int getTop() {
        return this.position.getY();
    }

    public int getLeft() {
        return this.position.getX();
    }

    public int getWidth() {
        return this.width.getValue();
    }

    public int getHeight() {
        return this.height.getValue();
    }

    public void setWidth(int width) {
        this.setMinWidth(width);
        this.setMaxWidth(width);
    }

    public void addGeometryListener(final Listener<Object2D> listener) {
        this.size.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                listener.onChange(TextDialog.this);
            }
        });

        this.position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                listener.onChange(TextDialog.this);
            }
        });
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    // <editor-fold defaultstate="collapsed" desc="alignment">
    public HorizontalAlign getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(HorizontalAlign horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public VerticalAlign getVerticalPosition() {
        return verticalPosition;
    }

    public void setVerticalPosition(VerticalAlign verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    public static enum HorizontalAlign {

        CENTER,
        LEFT,
        RIGHT
    }

    public static enum VerticalAlign {

        MIDDLE,
        TOP,
        BOTTOM
    }
    // </editor-fold>
}
