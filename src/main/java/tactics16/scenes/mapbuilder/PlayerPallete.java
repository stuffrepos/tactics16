package tactics16.scenes.mapbuilder;

import tactics16.components.BorderRectangle;
import tactics16.components.Object2D;
import tactics16.components.TextDialog;
import tactics16.game.Coordinate;
import tactics16.util.cursors.Cursor1D;
import tactics16.util.listeners.Listener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import tactics16.GameKey;
import tactics16.scenes.battle.Player;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PlayerPallete implements Object2D {

    public static final int BOX_WIDTH = 80;
    public static final int BOX_HEIGHT = 30;
    public static final int BOX_GAP = 10;
    private BorderRectangle visualCursor;
    private Cursor1D cursor;
    private Coordinate position = new Coordinate();
    private int playerCount = 2;
    private List<PlayerBox> boxes = new ArrayList<PlayerBox>();
    private static final int[] playerColors;

    static {
       playerColors = new int[Player.playersColors.size()];

       for(int i=0; i<playerColors.length; ++i) {
           playerColors[i] = Player.playersColors.get(i).getColor(Player.Color.DARK_0);
       }
    }

    public PlayerPallete() {
        visualCursor = new BorderRectangle(new Dimension(BOX_WIDTH, getHeight()));

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

        cursor.setKeys(GameKey.PREVIOUS, GameKey.NEXT);
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
        return BOX_HEIGHT;
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
            nameDialog.setForegroundColor(Color.WHITE);
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
