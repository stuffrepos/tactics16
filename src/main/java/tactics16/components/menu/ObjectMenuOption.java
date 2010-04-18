package tactics16.components.menu;

import tactics16.GameKey;
import tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class ObjectMenuOption<T extends Nameable> implements MenuOption {

    private T source;

    public ObjectMenuOption(T source) {
        this.source = source;
    }

    public String getText() {
        return source.getName();
    }

    public GameKey getKey() {
        return null;
    }

    public boolean isEnabled() {
        return true;
    }

    public T getSource() {
        return source;
    }
}
