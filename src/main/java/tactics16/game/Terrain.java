package tactics16.game;

import tactics16.util.Nameable;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import tactics16.animation.GameImage;

public class Terrain implements Nameable {

    private List<GameImage> images = new LinkedList<GameImage>();
    private String name;
    private Boolean allowMoviment;
    private Boolean allowAction;

    public Terrain(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getAllowMoviment() {
        return allowMoviment;
    }

    public void setAllowMoviment(boolean allowMoviment) {
        this.allowMoviment = allowMoviment;
    }

    public boolean getAllowAction() {
        return allowAction;
    }

    public void setAllowAction(boolean allowAction) {
        this.allowAction = allowAction;
    }

    public List<GameImage> getImages() {
        return images;
    }

    public void render(Graphics2D g, int x, int y) {
        images.get(0).render(g, x, y);
    }
}
