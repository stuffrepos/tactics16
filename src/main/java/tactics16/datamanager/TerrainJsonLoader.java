package tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import tactics16.game.Terrain;

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
        Terrain terrain = new Terrain(getRootJson().getString("name"));
        terrain.setAllowAction(getRootJson().getBoolean("allowAction"));
        terrain.setAllowMoviment(getRootJson().getBoolean("allowMoviment"));
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
