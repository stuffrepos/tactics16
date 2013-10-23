package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import net.stuffrepos.tactics16.game.Terrain;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class TerrainJsonLoader extends AbstractJsonFileLoader<Terrain>{

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
            terrain.getImages().add(loadImage(getRootJson().getString("image")));                    
        } else if (getRootJson().has("images")) {
            JSONArray images = getRootJson().getJSONArray("images");

            for (int i = 0; i < images.length(); ++i) {
                terrain.getImages().add(
                        loadImage(images.getString(i))
                 );
            }
        } else {
            throw new RuntimeException("Terrain has no images");
        }
        return terrain;
    }

}
