package net.stuffrepos.tactics16.components;

import java.util.LinkedList;
import java.util.List;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor2D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class Keyboard implements Object2D, VisualEntity {

    private static final char[] CHARS = new char[]{
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z', '-', '_', '0', '1',
        '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private static final int BUTTON_SIZE = 36;
    private static final int MAX_BUTTON_X = 10;
    private TextBox output = new TextBox();
    private List<PositionableButton> buttons;
    private Coordinate position = new Coordinate();
    private ObjectCursor2D<PositionableButton> cursor = new ObjectCursor2D<PositionableButton>();
    private int buttonPositionCounter = 0;
    private boolean shift = false;
    private boolean capsLock = false;
    private boolean finalized = false;
    private TextBox inputLabel;
    private final int inputMaxLength;

    public Keyboard(String initialText,int inputMaxLength) {
        this.inputMaxLength = inputMaxLength;
        inputLabel = new TextBox();
        inputLabel.setText("Input: ");
        buttons = new LinkedList<PositionableButton>();
        for (char c : CHARS) {
            buttons.add(new CharacterButton(c));
        }

        buttons.add(new FunctionButton("Delete") {

            @Override
            public void onButtonPress() {
                deleteCharacter();
            }
        });
        buttons.add(new FunctionButton("Shift") {

            @Override
            public void onButtonPress() {
                setShift(true);
            }
        });
        buttons.add(new FunctionButton("Space") {

            @Override
            public void onButtonPress() {
                addCharacter(' ');
            }
        });
        buttons.add(new FunctionButton("Caps Lock") {

            @Override
            public void onButtonPress() {
                setCapsLock(!capsLock);
            }
        });
        buttons.add(new FunctionButton("Confirm") {

            @Override
            public void onButtonPress() {
                exit(true);
            }
        });
        buttons.add(new FunctionButton("Cancel") {

            @Override
            public void onButtonPress() {
                exit(false);
            }
        });

        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                for (PositionableButton button : buttons) {
                    inputLabel.getPosition().set(source);
                    output.getPosition().setXY(
                            Layout.getRightGap(inputLabel),
                            inputLabel.getPosition().getY());
                    button.updatePosition();
                }
            }
        });

        cursor.addListener(new Listener<ObjectCursor2D<PositionableButton>>() {

            public void onChange(ObjectCursor2D<PositionableButton> source) {
                if (source.getLastSelected() != null) {
                    source.getLastSelected().setSelected(false);
                }
                source.getSelected().setSelected(true);
            }
        });

        output.setForegroundColor(Color.white);
        output.setBackgroundColor(new Color(0x440000));
        output.setFlat(true);
        output.setWidth(this.getWidth() - (inputLabel.getWidth() + Layout.OBJECT_GAP));
        output.setText(initialText);
    }

    public abstract boolean confirmExit(boolean confirm, String text);

    private void exit(boolean confirm) {
        if (confirmExit(confirm, output.getText())) {
            finalized = true;
        }
    }

    private void setShift(boolean shift) {
        this.shift = shift;
        updateCase();
    }

    private void setCapsLock(boolean capsLock) {
        this.capsLock = capsLock;
        updateCase();
    }

    private void updateCase() {
        for (PositionableButton button : buttons) {
            if (button instanceof CharacterButton) {
                ((CharacterButton) button).updateCase();
            }
        }
    }

    public int getTop() {
        return position.getY();
    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return (BUTTON_SIZE + Button.BUTTON_GAP) * MAX_BUTTON_X - Button.BUTTON_GAP;
    }

    public int getHeight() {
        return ((int) Math.ceil((double) CHARS.length / (double) MAX_BUTTON_X) + Button.BUTTON_GAP) * MAX_BUTTON_X - Button.BUTTON_GAP;
    }

    public void update(int delta) {
        for (PositionableButton button : buttons) {
            button.update(delta);
        }

        if (MyGame.getInstance().isKeyPressed(GameKey.CANCEL)) {
            deleteCharacter();
        }

        cursor.update(delta);
    }

    public void render(Graphics g) {
        inputLabel.render(g);
        for (PositionableButton button : buttons) {
            button.render(g);
        }
        output.render(g);
    }

    public boolean isFinalized() {
        return finalized;
    }

    public Coordinate getPosition() {
        return position;
    }

    private void addCharacter(char character) {
        if (output.getText().length() < inputMaxLength) {
            output.setText(output.getText() + character);
        }
    }

    private void deleteCharacter() {
        if (!output.getText().isEmpty()) {
            output.setText(output.getText().substring(0, output.getText().length() - 1));
        }
    }

    private abstract class PositionableButton extends Button {

        private final int x;
        private final int y;

        public PositionableButton(String text, Color color, int w) {

            this.x = buttonPositionCounter % MAX_BUTTON_X;
            this.y = buttonPositionCounter / MAX_BUTTON_X;

            getHeightConfig().setFixed(BUTTON_SIZE);
            getWidthConfig().setFixed((BUTTON_SIZE + Button.BUTTON_GAP) * w - Button.BUTTON_GAP);
            setText(text);
            setBackgroundColor(color);
            cursor.setObject(this, x, y, w, 1);
            buttonPositionCounter += w;
        }

        public void updatePosition() {
            getPosition().setXY(
                    position.getX() + x * (BUTTON_SIZE + Button.BUTTON_GAP),
                    Layout.getBottomGap(output) + y * (BUTTON_SIZE + Button.BUTTON_GAP));
        }
    }

    private abstract class FunctionButton extends PositionableButton {

        public FunctionButton(String text) {
            super(text, new Color(0x007700), 2);
        }
    }

    private class CharacterButton extends PositionableButton {

        private char character;

        public CharacterButton(final char character) {
            super(Character.toString(character), new Color(0x337755), 1);
            this.character = character;
        }

        @Override
        public void onButtonPress() {
            addCharacter(character);
            setShift(false);
        }

        public void updateCase() {
            if (shift == capsLock) {
                character = Character.toLowerCase(character);
            } else {
                character = Character.toUpperCase(character);
            }
            setText(Character.toString(character));
        }
    }
}
