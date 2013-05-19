package net.stuffrepos.tactics16.battlegameengine;

import junit.framework.TestCase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleEngineTest extends TestCase {

    public BattleEngineTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMathIntegerCosine() {
        assertEquals(-1, BattleEngine.Math.integerCosine(1, 0));
        assertEquals(0, BattleEngine.Math.integerCosine(1, 1));
        assertEquals(1, BattleEngine.Math.integerCosine(1, 2));
        assertEquals(0, BattleEngine.Math.integerCosine(1, 3));

        assertEquals(-2, BattleEngine.Math.integerCosine(2, 0));
        assertEquals(-1, BattleEngine.Math.integerCosine(2, 1));
        assertEquals(0, BattleEngine.Math.integerCosine(2, 2));
        assertEquals(1, BattleEngine.Math.integerCosine(2, 3));
        assertEquals(2, BattleEngine.Math.integerCosine(2, 4));
        assertEquals(1, BattleEngine.Math.integerCosine(2, 5));
        assertEquals(0, BattleEngine.Math.integerCosine(2, 6));
        assertEquals(-1, BattleEngine.Math.integerCosine(2, 7));
    }

    public void testMathIntegerSine() {
        assertEquals(0, BattleEngine.Math.integerSine(1, 0));
        assertEquals(1, BattleEngine.Math.integerSine(1, 1));
        assertEquals(0, BattleEngine.Math.integerSine(1, 2));
        assertEquals(-1, BattleEngine.Math.integerSine(1, 3));
        
        assertEquals(0, BattleEngine.Math.integerSine(2, 0));
        assertEquals(1, BattleEngine.Math.integerSine(2, 1));
        assertEquals(2, BattleEngine.Math.integerSine(2, 2));
        assertEquals(1, BattleEngine.Math.integerSine(2, 3));
        assertEquals(0, BattleEngine.Math.integerSine(2, 4));
        assertEquals(-1, BattleEngine.Math.integerSine(2, 5));
        assertEquals(-2, BattleEngine.Math.integerSine(2, 6));
        assertEquals(-1, BattleEngine.Math.integerSine(2, 7));
    }
}
