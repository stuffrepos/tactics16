package net.stuffrepos.tactics16.scenes.mapbuilder;

import net.stuffrepos.tactics16.components.BorderRectangle;
import net.stuffrepos.tactics16.components.Object2D;
import net.stuffrepos.tactics16.components.TextBox;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.util.cursors.Cursor1D;
import net.stuffrepos.tactics16.util.listeners.Listener;
import org.newdawn.slick.Color;
import java.awt.Dimension;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;
import net.stuffrepos.tactics16.util.cursors.ObjectCursor1D;
import net.stuffrepos.tactics16.util.image.ColorUtil;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PlayerPallete implements Object2D {

    private final BorderRectangle visualCursor;
    private final ObjectCursor1D<PlayerBox> cursor;
    private final Coordinate position = new Coordinate();
    private final int playerCount = Map.MAX_PLAYERS;
    private static final int[] playerColors;
    private final Dimension boxSize;

    static {
       playerColors = new int[PlayerConfig.PLAYER_COLORS.size()];

       for(int i=0; i<playerColors.length; ++i) {           
           playerColors[i] = ColorUtil.rgba(PlayerConfig.PLAYER_COLORS.get(i).getDefault());
       }
    }

    public PlayerPallete() {
        cursor = new ObjectCursor1D<PlayerBox>();

        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < playerCount; ++i) {
            PlayerBox box = new PlayerBox(i, 0xFFFF00);
            cursor.getList().add(box);
            if (box.getWidth() > maxWidth) {
                maxWidth = box.getWidth();
            }
            if (box.getHeight() > maxHeight) {
                maxHeight = box.getHeight();
            }
        }

        boxSize = new Dimension(maxWidth, maxHeight);
        visualCursor = new BorderRectangle(boxSize);

        for (PlayerBox box : cursor.getList()) {
            box.nameDialog.setMaxWidth(boxSize.width);
            box.nameDialog.setMinWidth(boxSize.width);
            box.nameDialog.setMaxHeight(boxSize.height);
            box.nameDialog.setMinHeight(boxSize.height);
        }

        position.addListener(new Listener<Coordinate>() {
            public void onChange(Coordinate source) {
                for (PlayerBox box : cursor.getList()) {
                    box.getPosition().setXY(
                            PlayerPallete.this.position.getX() + visualCursor.getBorderSize(),
                            PlayerPallete.this.position.getY()
                            + visualCursor.getBorderSize()
                            + (visualCursor.getBorderSize() + boxSize.height) * box.player);

                }
                visualCursor.wrap(cursor.getSelected());
            }
        });
        cursor.getCursor().addListener(new Listener<Cursor1D>() {
            public void onChange(Cursor1D source) {
                visualCursor.wrap(cursor.getSelected());
            }
        });

        cursor.getCursor().setKeys(GameKey.PREVIOUS, GameKey.NEXT);
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void update(long elapsedTime) {
        for (PlayerBox box : cursor.getList()) {
            box.update(elapsedTime);
        }
        cursor.update(elapsedTime);
        visualCursor.update(elapsedTime);
    }

    public void render(Graphics g) {
        for (PlayerBox box : cursor.getList()) {
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
        return visualCursor.getWidth();
    }

    public int getHeight() {
        return visualCursor.getBorderSize()
                + (visualCursor.getBorderSize() + boxSize.height) * playerCount;
    }

    public Cursor1D getCursor() {
        return cursor.getCursor();
    }

    public int[] getPlayerColors() {
        return playerColors;
    }

    private class PlayerBox implements Object2D {

        private int player;
        private TextBox nameDialog;

        public PlayerBox(int player, int color) {
            this.player = player;
            nameDialog = new TextBox();
            nameDialog.setText("Player " + (player + 1));
            nameDialog.setBackgroundColor(new Color(getColor()));
            nameDialog.setForegroundColor(Color.white);
        }

        public Coordinate getPosition() {
            return nameDialog.getPosition();
        }

        public void update(long elapsedTime) {
            nameDialog.update(player);
        }

        public void render(Graphics g) {
            nameDialog.render(g);
        }

        public int getColor() {
            return playerColors[player];
        }

        public int getTop() {
            return nameDialog.getTop();
        }

        public int getLeft() {
            return nameDialog.getLeft();
        }

        public int getWidth() {
            return nameDialog.getWidth();
        }

        public int getHeight() {
            return nameDialog.getHeight();
        }
    }
}
