/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tactics16.util;

import tactics16.util.Cursor1D;
import junit.framework.TestCase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Cursor1DTest extends TestCase {

    public Cursor1DTest(String testName) {
        super(testName);
    }

    public void testMove() {
        Cursor1D c = new Cursor1D();
        c.setLength(4);
        
        assertEquals(0, c.getCurrent());
        c.moveNext();
        assertEquals(1, c.getCurrent());
        c.moveNext();
        assertEquals(2, c.getCurrent());
        c.moveNext();
        assertEquals(3, c.getCurrent());
        c.moveNext();
        assertEquals(0, c.getCurrent());

        c.movePrevious();
        assertEquals(3, c.getCurrent());
        c.movePrevious();
        assertEquals(2, c.getCurrent());
        c.movePrevious();
        assertEquals(1, c.getCurrent());
        c.movePrevious();
        assertEquals(0, c.getCurrent());

        c.movePrevious();
        assertEquals(3, c.getCurrent());
        c.setLength(2);
        assertEquals(1, c.getCurrent());
        c.movePrevious();
        assertEquals(0, c.getCurrent());
        c.setLength(1);
        assertEquals(0, c.getCurrent());
        c.setLength(4);
        assertEquals(0, c.getCurrent());
    }
}
