package tactics16.components.menu;

import tactics16.GameKey;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class CommonMenuOption implements MenuOption {

    private String text;
    private GameKey key;
    private String help;

    public CommonMenuOption(String text) {
        this(text, null, null);
    }

    public CommonMenuOption(String text, GameKey key) {
        this(text, key, null);
    }

    public CommonMenuOption(String text, String help) {
        this(text, null, help);
    }

    public CommonMenuOption(String text, GameKey key, String help) {
        this.text = text;
        this.key = key;
        this.help = help;
    }

    public abstract void executeAction();

    public String getText() {
        return text;
    }

    public GameKey getKey() {
        return key;
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return help;
    }
}
