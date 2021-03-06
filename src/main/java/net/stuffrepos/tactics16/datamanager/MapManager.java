package net.stuffrepos.tactics16.datamanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONException;
import net.stuffrepos.tactics16.game.Map;
import net.stuffrepos.tactics16.util.DataGroup;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class MapManager implements Iterable<Map> {

    private File saveDirectory;
    private DataGroup<Map> maps = new DataGroup<Map>();

    public MapManager(File saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public void add(Map map) {
        maps.add(map);
    }

    private File getSaveDirectory() {
        return saveDirectory;
    }

    public void save(Map map) throws IOException {
        getSaveDirectory().mkdirs();

        File originalFile = new File(getSaveDirectory(), map.getOriginalName().toLowerCase() + ".json");
        if (originalFile.exists() && originalFile.isFile()) {
            originalFile.delete();
        }

        File file = new File(getSaveDirectory(), map.getName().toLowerCase() + ".json");
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(MapJsonLoader.mapToJson(map).toString(4).getBytes());
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } finally {
            fos.close();
        }

        if (!map.getName().equals(map.getOriginalName())) {
            maps.remove(map.getOriginalName());
        }
        map.setOriginalName(map.getName());
        maps.put(map);
    }

    public Map get(String name) {
        return maps.get(name);
    }

    public Iterator<Map> iterator() {
        return maps.iterator();
    }
}
