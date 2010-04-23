package tactics16.datamanager;

import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public abstract class AbstractJsonFileLoader<T> extends AbstractJsonObjectLoader<T> {

    public AbstractJsonFileLoader(File file) throws IOException, JSONException {
        super(new JSONObject(tactics16.util.javabasic.FileUtil.fileToString(file)),file);
    }
}
