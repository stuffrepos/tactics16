package tactics16.util;

import tactics16.util.cursors.ObjectCursor1D;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class ObjectCursor1DTest extends TestCase {

    public ObjectCursor1DTest(String testName) {
        super(testName);
    }

    public void testMove() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }

        ObjectCursor1D<Integer> oc = new ObjectCursor1D<Integer>(list);

        assertEquals(0, oc.getSelected().intValue());
    }
}
