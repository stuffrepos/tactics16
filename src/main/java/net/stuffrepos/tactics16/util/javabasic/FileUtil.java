package net.stuffrepos.tactics16.util.javabasic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class FileUtil {

    public static String fileToString(File file) throws IOException {
        char[] buffer = new char[1024];
        BufferedReader br = new BufferedReader(
                new FileReader(file));

        StringBuffer stringBuffer = new StringBuffer();
        int readed ;
        while ( (readed = br.read(buffer)) >= 0) {
            stringBuffer.append(buffer, 0, readed);
        }

        return stringBuffer.toString();
        
    }
}
