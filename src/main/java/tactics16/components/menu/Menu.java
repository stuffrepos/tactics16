package tactics16.components.menu;

import java.awt.Color;
import tactics16.MyGame;
import tactics16.game.Coordinate;
import tactics16.util.ObjectCursor1D;
import java.awt.Graphics2D;
import java.util.Collections;
import tactics16.GameKey;
import tactics16.components.Object2D;
import tactics16.util.CacheableValue;
import tactics16.util.ColorUtil;
import tactics16.util.MathUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Menu implements Object2D {

    private final static int OPTION_GAP = 1;
    private ObjectCursor1D<MenuOption> cursor = new ObjectCursor1D<MenuOption>();
    private Coordinate position = new Coordinate();
    private long elapsedTime = 0;
    private CacheableValue<Integer> width = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    private CacheableValue<Integer> height = new CacheableValue<Integer>() {

        @Override
        protected Integer calculate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public Menu(MenuOption... options) {
        cursor.getCursor().setKeys(GameKey.UP, GameKey.DOWN);
        Collections.addAll(this.cursor.getList(), options);
    }

    public ObjectCursor1D getCursor() {
        return cursor;
    }

    public void render(Graphics2D g) {
        int h = this.getOptionHeight();
        int i = 0;
        for (MenuOption option : this.cursor.getList()) {
            renderOption(
                    option,
                    g,
                    cursor.getCursor().getCurrent() == i,
                    position.getX(),
                    position.getY() + i * (h + OPTION_GAP),
                    this.getOptionWidth(), h);
            i++;
        }
    }

    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
        cursor.update(elapsedTime);
        while (!cursor.getSelected().isEnabled()) {
            if (cursor.getCursor().getLastMove() >= 0) {
                cursor.getCursor().moveNext();
            } else {
                cursor.getCursor().movePrevious();
            }
        }

        if (MyGame.getInstance().isKeyPressed(GameKey.CONFIRM)) {
            this.cursor.getSelected().executeAction();
        }

        for (MenuOption option : this.cursor.getList()) {
            if (option.getKey() != null) {
                if (MyGame.getInstance().isKeyPressed(option.getKey())) {
                    option.executeAction();
                }
            }
        }
    }

    public void addOption(MenuOption option) {
        this.cursor.getList().add(option);
    }

    protected void onChangeSelectedOption() {
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getOptionWidth() {
        int w = 0;

        for (MenuOption option : this.cursor.getList()) {
            int optionW = MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().stringWidth(option.getText());

            if (optionW > w) {
                w = optionW;
            }
        }

        return w + 10;
    }

    public int getOptionHeight() {
        return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight();
    }

    public void clear() {
        this.cursor.clear();
    }

    public int getTop() {
        return this.position.getY();
    }

    public int getLeft() {
        return this.position.getX();
    }

    public int getWidth() {
        return this.getOptionWidth();
    }

    public int getHeight() {
        return this.cursor.getList().size() * (this.getOptionHeight() + OPTION_GAP);
    }

    private void renderOption(MenuOption option, Graphics2D g, boolean selected, int x, int y, int w, int h) {
        int optionW = g.getFontMetrics().stringWidth(option.getText());
        Color backgroundColor;
        if (option.isEnabled()) {
            backgroundColor = selected
                    ? Colors.getSelectedBackgroundColor(elapsedTime)
                    : Colors.getEnabledBackgroundColor();
        } else {
            backgroundColor = Colors.getDisabledBackgroundColor();
        }

        g.setColor(backgroundColor);
        g.fill3DRect(x, y, w, h - 1, true);

        if (option.isEnabled()) {
            g.setColor(Colors.getEnabledForegroundColor());
        } else {
            g.setColor(Colors.getDisabledForegroundColor());
        }

        //g.drawRect(x, y, w, h - 1);
        g.drawString(
                option.getText(),
                x + (w - optionW) / 2,
                y + g.getFontMetrics().getAscent() + g.getFontMetrics().getLeading());
    }

    private static class Colors {

        private static final Color ENABLED_FOREGROUND_COLOR = Color.WHITE;
        private static final Color ENABLED_BACKGROUND_COLOR = new Color(0x000077);
        private static final Color DISABLED_FOREGROUND_COLOR = Color.GRAY;
        private static final Color DISABLED_BACKGROUND_COLOR = Color.DARK_GRAY;
        private static final Color SELECTED_BACKGROUND_COLOR_BEGIN = new Color(0x0000AA);
        private static final Color SELECTED_BACKGROUND_COLOR_END = new Color(0x0000FF);
        private static final Color[] SELECTED_BACKGROUND_COLORS;
        private static final int SELECTED_BACKGROUND_COLORS_COUNT = 8;
        private static final int CHANGE_FRAME_INTERVAL = 100;

        static {
            SELECTED_BACKGROUND_COLORS = new Color[SELECTED_BACKGROUND_COLORS_COUNT];

            for (int i = 0; i < SELECTED_BACKGROUND_COLORS.length - 1; ++i) {
                SELECTED_BACKGROUND_COLORS[i] = ColorUtil.getBetweenColor(
                        SELECTED_BACKGROUND_COLOR_BEGIN, SELECTED_BACKGROUND_COLOR_END,
                        (float) i / (SELECTED_BACKGROUND_COLORS.length - 1));
            }

            SELECTED_BACKGROUND_COLORS[SELECTED_BACKGROUND_COLORS.length - 1] = SELECTED_BACKGROUND_COLOR_END;
        }

        public static Color getEnabledForegroundColor() {
            return ENABLED_FOREGROUND_COLOR;
        }

        public static Color getEnabledBackgroundColor() {
            return ENABLED_BACKGROUND_COLOR;
        }

        public static Color getDisabledForegroundColor() {
            return DISABLED_FOREGROUND_COLOR;
        }

        public static Color getDisabledBackgroundColor() {
            return DISABLED_BACKGROUND_COLOR;
        }

        public static Color getSelectedBackgroundColor(long elapsedTime) {
            return SELECTED_BACKGROUND_COLORS[(int) MathUtil.getLoopCurrentIndex(
                    SELECTED_BACKGROUND_COLORS.length,
                    elapsedTime,
                    CHANGE_FRAME_INTERVAL)];
        }
    }
}
