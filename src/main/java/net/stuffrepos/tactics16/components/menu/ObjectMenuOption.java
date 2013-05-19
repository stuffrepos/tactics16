package net.stuffrepos.tactics16.components.menu;

import net.stuffrepos.tactics16.GameKey;
import net.stuffrepos.tactics16.util.Nameable;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class ObjectMenuOption<T extends Nameable> implements MenuOption {

    private T source;
    private boolean enabled;

    public ObjectMenuOption(T source) {
        this(source, true);
    }

    public ObjectMenuOption(T source, boolean enabled) {
        this.source = source;
        this.enabled = enabled;
    }

    public String getText() {
        return source.getName();
    }

    public GameKey getKey() {
        return null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public T getSource() {
        return source;
    }
}
