package tactics16.components;

import java.awt.Font;
import tactics16.game.Coordinate;
import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.animation.VisualEntity;
import tactics16.util.Align;
import tactics16.util.SizeConfig;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TextBox implements Object2D, VisualEntity {

    private Align horizontalAlign = Align.NEGATIVE;
    private Align verticalAlign = Align.NEGATIVE;
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
    private final Coordinate position = new Coordinate();
    private final SizeConfig width = new SizeConfig() {

        @Override
        protected int calculate() {
            return text.getWidth();
        }
    };
    private final SizeConfig height = new SizeConfig() {

        @Override
        protected int calculate() {
            return text.getHeight();
        }
    };
    private Color backgroundColor;
    private Text text;
    private boolean flat = false;

    public TextBox() {
        text = new Text();
        text.setColor(DEFAULT_FOREGROUND_COLOR);
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                text.getPosition().set(source);
            }
        });
    }

    public void render(Graphics2D g) {
        int x;
        switch (horizontalAlign) {
            case POSITIVE:
                x = position.getX() - getWidth();
                break;

            case NULL:
                x = position.getX() - getWidth() / 2;
                break;

            case NEGATIVE:
            default:
                x = position.getX();
                break;
        }
        int y;
        switch (horizontalAlign) {
            case POSITIVE:
                y = position.getY() - getHeight();
                break;

            case NULL:
                y = position.getY() - getHeight() / 2;
                break;

            case NEGATIVE:
            default:
                y = position.getY();
                break;
        }
        g.setColor(this.getBackgroundColor());
        if (flat) {
            g.fillRect(
                    x,
                    position.getY(),
                    getWidth() - 1,
                    getHeight() - 1);
        } else {
            g.fill3DRect(
                    x,
                    position.getY(),
                    getWidth() - 1,
                    getHeight() - 1,
                    true);
        }
        g.setColor(this.getForegroundColor());
        text.render(g);
    }

    public Coordinate getPosition() {
        return position;
    }

    public Integer getMinHeight() {
        return width.getMin();
    }

    public void setMinHeight(Integer minHeight) {
        this.height.setMin(minHeight);
    }

    public Integer getMaxHeight() {
        return height.getMax();
    }

    public void setMaxHeight(Integer maxHeight) {
        this.height.setMax(maxHeight);
    }

    public Integer getMinWidth() {
        return width.getMin();
    }

    public void setMinWidth(Integer minWidth) {
        width.setMin(minWidth);
    }

    public Integer getMaxWidth() {
        return width.getMax();
    }

    public void setMaxWidth(Integer maxWidth) {
        width.setMax(maxWidth);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return text.getColor();
    }

    public void setForegroundColor(Color foregroundColor) {
        text.setColor(foregroundColor);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.width.clear();
        this.height.clear();
        this.text.setText(text);
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
        this.width.addListener(new Listener<SizeConfig>() {

            public void onChange(SizeConfig source) {
                listener.onChange(TextBox.this);
            }
        });
        this.height.addListener(new Listener<SizeConfig>() {

            public void onChange(SizeConfig source) {
                listener.onChange(TextBox.this);
            }
        });
        this.position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                listener.onChange(TextBox.this);
            }
        });
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public void setHorizontalPosition(Align align) {
        text.setAlign(align);
    }

    public void setVerticalPosition(Align align) {
    }

    public void setFont(Font font) {
        this.width.clear();
        this.height.clear();
        text.setFont(font);
    }

    public Font getFont() {
        return text.getFont();
    }

    public void update(long elapsedTime) {
    }

    public boolean isFinalized() {
        return false;
    }

    public Align getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(Align horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public Align getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(Align verticalAlign) {
        this.verticalAlign = verticalAlign;
    }
}