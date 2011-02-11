package net.stuffrepos.tactics16.datamanager;

import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class JsonArrayIterator {

    public JsonArrayIterator(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                iterate(jsonArray, i);
            }
        }
    }

    public abstract void iterate(JSONArray jsonArray, int index) throws JSONException;
}
