package tactics16.util;

import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class StringUtil {

    public static String implode(String separator,List list) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for(Object o: list) {
            if (first) {
                first = false;
            }
            else {
                b.append(separator);
            }

            b.append(o);
        }

        return b.toString();
    }
}