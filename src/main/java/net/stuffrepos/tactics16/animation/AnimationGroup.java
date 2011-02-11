package net.stuffrepos.tactics16.animation;

import java.util.Map.Entry;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public interface AnimationGroup {

    public Iterable<Entry<String, SpriteAnimation>> getAnimations();

}
