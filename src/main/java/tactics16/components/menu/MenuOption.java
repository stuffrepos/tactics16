package tactics16.components.menu;

import tactics16.GameKey;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface MenuOption {

    public void executeAction();

    public String getText();

    public GameKey getKey();

    public boolean isEnabled();
}
