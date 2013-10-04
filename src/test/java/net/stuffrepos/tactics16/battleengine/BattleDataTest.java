package net.stuffrepos.tactics16.battleengine;

import junit.framework.TestCase;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class BattleDataTest extends TestCase {

    public BattleDataTest(String testName) {
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
        assertEquals(-1, BattleData.Math.integerCosine(1, 0));
        assertEquals(0, BattleData.Math.integerCosine(1, 1));
        assertEquals(1, BattleData.Math.integerCosine(1, 2));
        assertEquals(0, BattleData.Math.integerCosine(1, 3));

        assertEquals(-2, BattleData.Math.integerCosine(2, 0));
        assertEquals(-1, BattleData.Math.integerCosine(2, 1));
        assertEquals(0, BattleData.Math.integerCosine(2, 2));
        assertEquals(1, BattleData.Math.integerCosine(2, 3));
        assertEquals(2, BattleData.Math.integerCosine(2, 4));
        assertEquals(1, BattleData.Math.integerCosine(2, 5));
        assertEquals(0, BattleData.Math.integerCosine(2, 6));
        assertEquals(-1, BattleData.Math.integerCosine(2, 7));
    }

    public void testMathIntegerSine() {
        assertEquals(0, BattleData.Math.integerSine(1, 0));
        assertEquals(1, BattleData.Math.integerSine(1, 1));
        assertEquals(0, BattleData.Math.integerSine(1, 2));
        assertEquals(-1, BattleData.Math.integerSine(1, 3));
        
        assertEquals(0, BattleData.Math.integerSine(2, 0));
        assertEquals(1, BattleData.Math.integerSine(2, 1));
        assertEquals(2, BattleData.Math.integerSine(2, 2));
        assertEquals(1, BattleData.Math.integerSine(2, 3));
        assertEquals(0, BattleData.Math.integerSine(2, 4));
        assertEquals(-1, BattleData.Math.integerSine(2, 5));
        assertEquals(-2, BattleData.Math.integerSine(2, 6));
        assertEquals(-1, BattleData.Math.integerSine(2, 7));
    }
}
