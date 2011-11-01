package net.stuffrepos.tactics16.animation.transitioneffect;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import java.awt.image.BufferedImage;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.MyGame;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import net.stuffrepos.tactics16.util.image.ImageUtil;
import net.stuffrepos.tactics16.util.image.PixelImageIterator;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.PBufferGraphics;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class FadeIn extends AbstractPhase {

    public static final long FADE_TIME = 500L;
    private Phase phase;
    private PhaseManager phaseManager;
    private long elapsedTime = 0;
    private Text status = new Text();

    public FadeIn(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
        this.phase = phaseManager.getCurrentPhase();
        this.phaseManager.advance(this);
    }

    private boolean isFinalized() {
        return elapsedTime > FADE_TIME;
    }

    private float calculateColorFactor() {
        return (float) Math.min(FADE_TIME, elapsedTime) / (float) FADE_TIME;
    }

    @Override
    public void onEnter() {
        status.getPosition().setXY(Layout.OBJECT_GAP, Layout.OBJECT_GAP);
    }

    @Override
    public void update(long elapsedTime) {
        this.elapsedTime += elapsedTime;
        this.phase.update(elapsedTime);

        if (isFinalized()) {
            phaseManager.back();
        }

        status.update(elapsedTime);
        status.setText(String.format("%d / %d", this.elapsedTime, FADE_TIME));
    }

    @Override
    public void render(Graphics g) {
        try {
            final Image image = new Image(Layout.getScreenWidth(), Layout.getScreenHeight());            
            Graphics imageGraphics = image.getGraphics();
            imageGraphics.setFont(MyGame.getInstance().getFont());
            phase.render(imageGraphics);
            imageGraphics.destroy();

            final float colorFactor = calculateColorFactor();
            
            final BufferedImage bufferedImage = ImageUtil.slickToAwt(image);

            new PixelImageIterator(bufferedImage) {

                @Override
                public void iterate(int x, int y, int rgb) {
                    bufferedImage.setRGB(x, y, ColorUtil.rgba(ColorUtil.applyFactor(new Color(rgb), colorFactor)));
                }
            };
            
            g.drawImage(ImageUtil.awtToSlick(bufferedImage), 0, 0, null);

            //status.render(g);
        } catch (SlickException ex) {
            throw new RuntimeException(ex);
        }
    }
}
