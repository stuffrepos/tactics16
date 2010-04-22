package tactics16.datamanager;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;
import tactics16.MyGame;
import tactics16.animation.GameImage;
import tactics16.util.CacheableValue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class AbstractJsonObjectLoader<T> {

    private CacheableValue<T> object = new CacheableValue<T>() {

        @Override
        protected T calculate() {
            try {
                return internalLoadObject();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    private final JSONObject rootJson;
    private File file;

    public AbstractJsonObjectLoader(JSONObject rootJson, File file) {
        this.file = file;
        this.rootJson = rootJson;
    }

    public final T loadObject() throws JSONException {
        try {
            return object.getValue();
        } catch (RuntimeException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().equals(JSONException.class)) {
                throw (JSONException) ex.getCause();
            } else {
                throw ex;
            }
        }

    }

    protected abstract T internalLoadObject() throws JSONException;

    protected JSONObject getRootJson() {
        return rootJson;
    }

    protected File getDirectory() {
        return this.getFile().getParentFile();
    }

    protected GameImage loadImage(String imageFileName) {
        return MyGame.getInstance().getImage(new File(getDirectory(), imageFileName));
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
