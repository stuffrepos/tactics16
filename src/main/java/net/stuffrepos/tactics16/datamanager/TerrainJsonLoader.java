package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import net.stuffrepos.tactics16.game.Terrain;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TerrainJsonLoader extends AbstractJsonFileLoader<Terrain>{

    private final static int DEFAULT_CHANGE_FRAME_INTERVAL = 300;

    public TerrainJsonLoader(File file) throws IOException, JSONException {
        super(file);
    }
    
    @Override
    protected Terrain fromJson() throws JSONException {
        Terrain terrain = new Terrain(
                Terrain.Layer.valueOf(getRootJson().getString("layer")),
                getRootJson().getString("name"));
        terrain.setBlock(getRootJson().getBoolean("block"));
        if (getRootJson().has("image")) {
            terrain.setSpriteAnimation(buildSpriteAnimationFromImage(getRootJson().getString("image")));
        } else if (getRootJson().has("images")) {
            terrain.setSpriteAnimation(buildSpriteAnimationFromImages(getRootJson().getJSONArray("images")));
        } else if (getRootJson().has("animationGroup")) {
            terrain.setSpriteAnimation(buildSpriteAnimationFromAnimationGroup(getRootJson().getJSONObject("animationGroup")));
        } else {
            throw new RuntimeException("Terrain has no images");
        }
        
        if (getRootJson().has("width")) {
            terrain.setWidth(getRootJson().getInt("width"));
        }
        
        if (getRootJson().has("height")) {
            terrain.setHeight(getRootJson().getInt("height"));
        }
        
        return terrain;
    }

    private SpriteAnimation buildSpriteAnimationFromImage(String image) {
        SpriteAnimation animation = new SpriteAnimation();
        animation.setChangeFrameInterval(DEFAULT_CHANGE_FRAME_INTERVAL);
        animation.addImage(loadImage(image));
        return animation;
    }

    private SpriteAnimation buildSpriteAnimationFromImages(JSONArray images) throws JSONException {
        SpriteAnimation animation = new SpriteAnimation();
        animation.setChangeFrameInterval(DEFAULT_CHANGE_FRAME_INTERVAL);
        for (int i = 0; i < images.length(); ++i) {
            animation.addImage(loadImage(images.getString(i)));
        }
        return animation;
    }

    private SpriteAnimation buildSpriteAnimationFromAnimationGroup(JSONObject animationGroupJson) throws JSONException {
        return new AnimationGroupJsonLoader(animationGroupJson, getFile()).
                fromJson().
                getAnimations().
                iterator().
                next().
                getValue();
    }
}
