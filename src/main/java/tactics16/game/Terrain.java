package tactics16.game;

import tactics16.util.Nameable;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Terrain implements Nameable {

    private List<BufferedImage> images = new LinkedList<BufferedImage>();
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

    public List<BufferedImage> getImages() {
        return images;
    }

    public void render(Graphics2D g, int x, int y) {
        g.drawImage(images.get(0), x, y, null);
    }
}
