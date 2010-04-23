package tactics16.components;

import java.awt.Color;
import java.awt.Graphics2D;
import tactics16.GameKey;
import tactics16.MyGame;
import tactics16.animation.VisualEntity;
import tactics16.game.Coordinate;
import tactics16.util.image.ColorUtil;
import tactics16.util.javabasic.MathUtil;
import tactics16.util.SizeConfig;
import tactics16.util.listeners.Listener;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class Button implements VisualEntity, Object2D {

    private static final Color DISABLED_FOREGROUND_COLOR = Color.GRAY;
    private static final Color DISABLED_BACKGROUND_COLOR = Color.DARK_GRAY;
    private static final Color DEFAULT_ENABLED_FOREGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_ENABLED_BACKGROUND_COLOR = new Color(0x000077);
    public static final int BUTTON_GAP = 1;
    private Colors colors = new Colors();
    private boolean selected;
    private boolean enabled;
    private Text text = new Text();
    private long elapsedTime;
    private Coordinate position = new Coordinate();
    private SizeConfig width = new SizeConfig() {

        @Override
        protected int calculate() {
            return text.getWidth();
        }
    };
    private SizeConfig height = new SizeConfig() {

        @Override
        protected int calculate() {
            return text.getHeight();
        }
    };

    public Button() {
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                updateTextPosition();
            }
        });

        setSelected(false);
        setEnabled(true);
    }

    private void updateTextPosition() {
        text.getPosition().setXY(
                position.getX() + (width.getValue() - text.getWidth()) / 2,
                position.getY() +(height.getValue() - text.getHeight()) / 2);

    }

    public void setForegroundColor(Color foregroundColor) {
        text.setColor(foregroundColor);
    }

    public void setBackgroundColor(Color backgroundColor) {
        colors.setBackgroundColor(backgroundColor);
    }

    public void render(Graphics2D g) {
        g.setColor(colors.getCurrentBackgroundColor());
        g.fill3DRect(
                position.getX(),
                position.getY(),
                getWidth(),
                getHeight(),
                true);

        text.render(g);
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;

        if (selected && MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            onButtonPress();
        }
    }

    public boolean isFinalized() {
        return false;
    }

    public SizeConfig getWidthConfig() {
        return width;
    }

    public SizeConfig getHeightConfig() {
        return height;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.text.setColor(colors.getCurrentForegroundColor());
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
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

    public Coordinate getPosition() {
        return position;
    }

    private class Colors {

        private Color foregroundColor;
        private Color backgroundColor;
        private Color[] selectedBackgroundColors;
        private static final int SELECTED_BACKGROUND_COLORS_COUNT = 8;
        private static final int CHANGE_FRAME_INTERVAL = 100;

        private Colors() {
            setForegroundColor(DEFAULT_ENABLED_FOREGROUND_COLOR);
            setBackgroundColor(DEFAULT_ENABLED_BACKGROUND_COLOR);
        }

        public Color getCurrentBackgroundColor() {
            if (isEnabled()) {
                if (isSelected()) {
                    return getSelectedBackgroundColor();
                } else {
                    return backgroundColor;
                }
            } else {
                return DISABLED_BACKGROUND_COLOR;
            }
        }

        private Color getSelectedBackgroundColor() {
            return selectedBackgroundColors[(int) MathUtil.getLoopCurrentIndex(
                    selectedBackgroundColors.length,
                    elapsedTime,
                    CHANGE_FRAME_INTERVAL)];
        }

        private void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;

            selectedBackgroundColors = new Color[SELECTED_BACKGROUND_COLORS_COUNT];

            Color beginColor = ColorUtil.light(this.backgroundColor);
            Color endColor = ColorUtil.light(beginColor);

            for (int i = 0; i < selectedBackgroundColors.length - 1; ++i) {
                selectedBackgroundColors[i] = ColorUtil.getBetweenColor(
                        beginColor, endColor,
                        (float) i / (selectedBackgroundColors.length - 1));
            }

            selectedBackgroundColors[selectedBackgroundColors.length - 1] = endColor;
        }

        public Color getForegroundColor() {
            return foregroundColor;
        }

        public void setForegroundColor(Color foregroundColor) {
            this.foregroundColor = foregroundColor;
        }

        private Color getCurrentForegroundColor() {
            if (enabled) {
                return foregroundColor;
            } else {
                return DISABLED_FOREGROUND_COLOR;
            }
        }
    }

    public abstract void onButtonPress();
}
