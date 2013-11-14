package net.stuffrepos.tactics16;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.stuffrepos.tactics16.util.cache.CacheableValue;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class KeyMapping {

    private final Map<GameKey, List<Integer>> mapping =
            new EnumMap<GameKey, List<Integer>>(GameKey.class);
    private final Set<Integer> pressed = new HashSet<Integer>();
    private final Set<Integer> released = new HashSet<Integer>();
    private final Map<Integer, Long> holdedTime = new TreeMap<Integer, Long>();
    private CacheableValue<Integer[]> allKeys = new CacheableValue<Integer[]>() {
        @Override
        protected Integer[] calculate() {
            List<Integer> keys = new LinkedList<Integer>();
            for (GameKey gameKey : GameKey.values()) {
                keys.addAll(mapping.get(gameKey));
            }
            return keys.toArray(new Integer[0]);
        }
    };

    public KeyMapping() {
        setMapping(GameKey.UP, Input.KEY_UP);
        setMapping(GameKey.DOWN, Input.KEY_DOWN);
        setMapping(GameKey.LEFT, Input.KEY_LEFT);
        setMapping(GameKey.RIGHT, Input.KEY_RIGHT);
        setMapping(GameKey.CONFIRM, Input.KEY_ENTER, Input.KEY_SPACE);
        setMapping(GameKey.CANCEL, Input.KEY_BACK);
        setMapping(GameKey.OPTIONS, Input.KEY_ESCAPE);
        setMapping(GameKey.PREVIOUS, Input.KEY_PRIOR);
        setMapping(GameKey.NEXT, Input.KEY_NEXT);
    }

    public void preUpdate(GameContainer container, int delta) {
        for (Integer key : allKeys.getValue()) {
            pressed.remove(key);
            released.remove(key);
            if (container.getInput().isKeyDown(key)) {
                if (holdedTime.get(key) == null) {
                    pressed.add(key);
                    holdedTime.put(key, 0l);
                } else {
                    holdedTime.put(key, holdedTime.get(key) + delta);
                }
            } else {
                if (holdedTime.get(key) != null) {
                    released.add(key);
                    holdedTime.remove(key);
                }
            }
        }
    }

    public boolean isPressed(GameKey gameKey) {
        for (Integer key : mapping.get(gameKey)) {
            if (pressed.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReleased(GameKey gameKey) {
        for (Integer key : mapping.get(gameKey)) {
            if (released.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHolded(GameKey gameKey, int holdTime) {
        for (Integer key : mapping.get(gameKey)) {
            if (holdedTime.get(key) != null && holdedTime.get(key) >= holdTime) {
                return true;
            }
        }
        return false;
    }

    public boolean isHitted(GameKey gameKey) {
        return isPressed(gameKey);
    }

    public Collection<Integer> getKeys(GameKey gameKey) {
        return mapping.get(gameKey);
    }

    private void setMapping(GameKey gameKey, int... keys) {
        List<Integer> keysList = new ArrayList<Integer>();
        for (int key : keys) {
            keysList.add(key);
        }
        mapping.put(gameKey, keysList);
        allKeys.clear();
    }
}
