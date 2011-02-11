package net.stuffrepos.tactics16.animation.transitioneffect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import net.stuffrepos.tactics16.Layout;
import net.stuffrepos.tactics16.components.Text;
import net.stuffrepos.tactics16.phase.AbstractPhase;
import net.stuffrepos.tactics16.phase.Phase;
import net.stuffrepos.tactics16.phase.PhaseManager;
import net.stuffrepos.tactics16.util.image.ColorUtil;
import net.stuffrepos.tactics16.util.image.PixelImageIterator;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class FadeOut extends AbstractPhase {

    public static final long FADE_TIME = 500L;
    private Phase phase;
    private PhaseManager phaseManager;
    private long elapsedTime = 0;
    private Text status = new Text();

    public FadeOut(PhaseManager phaseManager) {
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
    public void render(Graphics2D g) {
        final BufferedImage image = new BufferedImage(
                Layout.getScreenWidth(),
                Layout.getScreenHeight(),
                Transparency.BITMASK);
        Graphics2D imageGraphics = image.createGraphics();
        phase.render(imageGraphics);
        imageGraphics.dispose();

        final float colorFactor = calculateColorFactor();

        new PixelImageIterator(image) {

            @Override
            public void iterate(int x, int y, int rgb) {
                image.setRGB(x, y, ColorUtil.applyFactor(new Color(rgb), colorFactor).getRGB());
            }
        };

        g.drawImage(image, 0, 0, null);

        //status.render(g);
    }
}
