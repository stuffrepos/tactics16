package tactics16.components;

import tactics16.MyGame;
import tactics16.game.Coordinate;
import tactics16.util.ObjectCursor1D;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Collections;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Menu implements Object2D {

    private final static int OPTION_GAP = 1;
    private ObjectCursor1D<MenuOption> cursor = new ObjectCursor1D<MenuOption>();
    private Coordinate position = new Coordinate();

    public Menu(MenuOption... options) {
        cursor.getCursor().setKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        Collections.addAll(this.cursor.getList(), options);
    }

    public ObjectCursor1D getCursor() {
        return cursor;
    }

    public void render(Graphics2D g) {
        int h = this.getOptionHeight();
        int i = 0;
        for (MenuOption option : this.cursor.getList()) {
            option.render(
                    g,
                    cursor.getCursor().getCurrent() == i,
                    position.getX(),
                    position.getY() + i * (h + OPTION_GAP),
                    this.getOptionWidth(), h);
            i++;
        }
    }

    public void update(long elapsedTime) {
        cursor.update(elapsedTime);
        while (!cursor.getSelected().isEnabled()) {
            if (cursor.getCursor().getLastMove() >= 0) {
                cursor.getCursor().moveNext();
            } else {
                cursor.getCursor().movePrevious();
            }
        }

        if (MyGame.getInstance().keyPressed(KeyEvent.VK_ENTER) || MyGame.getInstance().keyPressed(KeyEvent.VK_SPACE)) {
            this.cursor.getSelected().executeAction();
        }

        for (MenuOption option : this.cursor.getList()) {
            if (option.getKey() != null) {
                if (MyGame.getInstance().keyPressed(option.getKey())) {
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

}
