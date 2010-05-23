package tactics16.datamanager;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class JsonKeyIterator {

    public JsonKeyIterator(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();

            while (iterator.hasNext()) {
                iterate(jsonObject, iterator.next());
            }
        }
    }

    public abstract void iterate(JSONObject jsonObject, String key) throws JSONException;
}
