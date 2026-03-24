package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestRealValue {

    @Test
    void testAddition() {
        RealValue a = new RealValue(1.5);
        RealValue b = new RealValue(2.5);
        assertEquals(new RealValue(4.0), a.add(b));
    }

    @Test
    void testSubtraction() {
        RealValue a = new RealValue(5.0);
        RealValue b = new RealValue(3.0);
        assertEquals(new RealValue(2.0), a.sub(b));
    }

    @Test
    void testMultiplication() {
        RealValue a = new RealValue(2.0);
        RealValue b = new RealValue(3.5);
        assertEquals(new RealValue(7.0), a.mul(b));
    }

    @Test
    void testDivision() {
        RealValue a = new RealValue(7.0);
        RealValue b = new RealValue(2.0);
        assertEquals(new RealValue(3.5), a.div(b));
    }

    @Test
    void testDivisionByZeroGivesInfinity() {
        // En réels, 1.0/0.0 = +Infinity (pas une exception)
        RealValue a = new RealValue(1.0);
        RealValue b = new RealValue(0.0);
        assertEquals(new RealValue(Double.POSITIVE_INFINITY), a.div(b));
    }

    @Test
    void testDivisionZeroByZeroGivesNaN() {
        // 0.0/0.0 = NaN
        RealValue a = new RealValue(0.0);
        RealValue b = new RealValue(0.0);
        assertEquals(new RealValue(Double.NaN), a.div(b));
    }

    @Test
    void testToString() {
        assertEquals("3.14", new RealValue(3.14).toString());
    }

    @Test
    void testEquals() {
        assertEquals(new RealValue(1.5), new RealValue(1.5));
        assertNotEquals(new RealValue(1.5), new RealValue(2.5));
    }
}