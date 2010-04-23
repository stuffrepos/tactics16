package tactics16.components;

import java.awt.Font;
import tactics16.game.Coordinate;
import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.Layout;
import tactics16.util.Align;
import tactics16.util.SizeConfig;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TextDialog implements Object2D {

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

    public TextDialog() {
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

        g.setColor(this.getBackgroundColor());
        if (flat) {
            g.fillRect(
                    position.getX(),
                    position.getY(),
                    getWidth() - 1,
                    getHeight() - 1);
        } else {
            g.fill3DRect(
                    position.getX(),
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
                listener.onChange(TextDialog.this);
            }
        });
        this.height.addListener(new Listener<SizeConfig>() {

            public void onChange(SizeConfig source) {
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

    public void setHorizontalPosition(Align align) {
        text.setAlign(align);
    }

    public void setVerticalPosition(Align align) {
    }

    public void setFont(Font font) {
        text.setFont(font);
    }
}
