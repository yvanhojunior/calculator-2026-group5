package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.MathContext;

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
    void testModulo() {
        RealValue a = new RealValue(7.0);
        RealValue b = new RealValue(2.0);
        assertEquals(new RealValue(1.0), a.mod(b));
    }

    @Test
    void testNegativeModulo() {
        RealValue a = new RealValue(-7.0);
        RealValue b = new RealValue(2.0);
        assertEquals(new RealValue(1.0), a.mod(b));
    }

    @Test
    void testDivisionByZeroGivesInfinity() {
        // 1.0 / 0.0 = +Infinity
        RealValue a = new RealValue(1.0);
        RealValue b = new RealValue(0.0);
        assertEquals(RealValue.POS_INF, a.div(b));
    }

    @Test
    void testNegativeDivisionByZeroGivesNegInfinity() {
        // -1.0 / 0.0 = -Infinity
        RealValue a = new RealValue(-1.0);
        RealValue b = new RealValue(0.0);
        assertEquals(RealValue.NEG_INF, a.div(b));
    }

    @Test
    void testDivisionZeroByZeroGivesNaN() {
        // 0.0 / 0.0 = NaN
        RealValue a = new RealValue(0.0);
        RealValue b = new RealValue(0.0);
        assertEquals(RealValue.NAN, a.div(b));
    }

    @Test
    void testPiConstant() {
        // Pi doit être 3.141592654 avec 10 chiffres significatifs
        assertNotNull(RealValue.PI);
        assertTrue(RealValue.PI.toString().startsWith("3.14159265"));
    }

    @Test
    void testPrecision() {
        // 1/3 avec précision de 5 chiffres = 0.33333
        RealValue a = new RealValue(new BigDecimal(1), new MathContext(5));
        RealValue b = new RealValue(new BigDecimal(3), new MathContext(5));
        RealValue result = (RealValue) a.div(b);
        assertEquals("0.33333", result.toString());
    }

    @Test
    void testNaNPlusAnything() {
        assertEquals(RealValue.NAN, RealValue.NAN.add(new RealValue(5.0)));
    }

    @Test
    void testInfinityPlusNegInfinity() {
        assertEquals(RealValue.NAN, RealValue.POS_INF.add(RealValue.NEG_INF));
    }

    @Test
    void testToString() {
        assertEquals("3.5", new RealValue(3.5).toString());
        assertEquals("NaN", RealValue.NAN.toString());
        assertEquals("+Infinity", RealValue.POS_INF.toString());
        assertEquals("-Infinity", RealValue.NEG_INF.toString());
    }

    @Test
    void testEquals() {
        assertEquals(new RealValue(1.5), new RealValue(1.5));
        assertNotEquals(new RealValue(1.5), new RealValue(2.5));
        assertEquals(RealValue.NAN, RealValue.NAN);
        assertEquals(RealValue.POS_INF, RealValue.POS_INF);
    }
}