package tactics16.scenes.mapbuilder;

import tactics16.MyGame;
import tactics16.components.BorderRectangle;
import tactics16.components.Object2D;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import tactics16.util.Cursor1D;
import tactics16.util.listeners.Listener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import tactics16.GameKey;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PlayerPallete implements Object2D {

    public static final int BOX_WIDTH = 60;
    public static final int BOX_GAP = 1;
    private BorderRectangle visualCursor;
    private Cursor1D cursor;
    private Coordinate position = new Coordinate();
    private int playerCount = 2;
    private List<PlayerBox> boxes = new ArrayList<PlayerBox>();
    private final int[] playerColors = new int[]{0x770000, 0x777700, 0x000077, 0x007700};

    public PlayerPallete() {
        visualCursor = new BorderRectangle(new Dimension(BOX_WIDTH, TextDialog.getDefaultHeight()));

        for (int i = 0; i < playerCount; ++i) {
            boxes.add(new PlayerBox(i, 0xFFFF00));
        }

        cursor = new Cursor1D(boxes.size());
        position.addListener(new Listener<Coordinate>() {

            public void onChange(Coordinate source) {
                for (PlayerBox box : boxes) {
                    box.getPosition().setXY(
                            PlayerPallete.this.position.getX() + box.getPlayer() * (BOX_WIDTH + BOX_GAP),
                            PlayerPallete.this.position.getY());

                }
                updateVisualCursor();
            }
        });
        cursor.addListener(new Listener<Cursor1D>() {

            public void onChange(Cursor1D source) {
                updateVisualCursor();
            }
        });

        cursor.setKeys(GameKey.PREVIOUS,GameKey.NEXT);
    }

    private void updateVisualCursor() {
        visualCursor.getPosition().setXY(
                getLeft() + cursor.getCurrent() * (BOX_WIDTH + BOX_GAP), getTop());
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void update(long elapsedTime) {
        for (PlayerBox box : boxes) {
            box.update(elapsedTime);
        }
        cursor.update(elapsedTime);
        visualCursor.update(elapsedTime);
    }

    public void render(Graphics2D g) {
        for (PlayerBox box : boxes) {
            box.render(g);
        }
        visualCursor.render(g);
    }

    public int getTop() {
        return position.getY();

    }

    public int getLeft() {
        return position.getX();
    }

    public int getWidth() {
        return playerCount * (BOX_WIDTH + BOX_GAP);
    }

    public int getHeight() {
        return MyGame.getInstance().getDefaultGraphics2D().getFontMetrics().getHeight();
    }

    public Cursor1D getCursor() {
        return cursor;
    }

    public int[] getPlayerColors() {
        return playerColors;
    }

    private class PlayerBox {

        private int player;
        private TextDialog nameDialog;
        private Coordinate position = new Coordinate();

        public PlayerBox(int player, int color) {
            this.player = player;
            nameDialog = new TextDialog();
            nameDialog.setText("Player " + (player + 1));
            nameDialog.setMaxWidth(BOX_WIDTH);
            nameDialog.setMinWidth(BOX_WIDTH);
            nameDialog.setBackgroundColor(new Color(getColor()));
            nameDialog.setForegroundColor(Color.BLACK);
            this.position.addListener(new Listener<Coordinate>() {

                public void onChange(Coordinate source) {
                    nameDialog.getPosition().set(source);
                }
            });
        }

        public Coordinate getPosition() {
            return position;
        }

        public void update(long elapsedTime) {
        }

        public void render(Graphics2D g) {
            nameDialog.render(g);
        }

        public int getPlayer() {
            return player;
        }

        public int getColor() {
            return playerColors[player];
        }
    }
}
