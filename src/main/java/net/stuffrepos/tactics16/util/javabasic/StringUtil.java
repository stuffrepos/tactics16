package net.stuffrepos.tactics16.util.javabasic;

import java.util.List;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class StringUtil {

    public static String implode(String separator, List list) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (Object o : list) {
            if (first) {
                first = false;
            } else {
                b.append(separator);
            }

            b.append(o);
        }

        return b.toString();
    }

    public static String parseString(String s) {

        if (s == null) {
            return null;
        }

        s = s.trim();

        if (s.isEmpty()) {
            return null;
        } else {
            return s;
        }
    }

    public static boolean isEmpty(String text) {
        return parseString(text) == null;
    }

    public static String yesNo(boolean b) {
        return b ? "Yes" : "No";
    }

    public static Double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
