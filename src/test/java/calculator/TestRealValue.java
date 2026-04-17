package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import io.cucumber.java.lu.a.as;

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

    @Test
    void testHashCode() {
        assertEquals(new RealValue(1.5).hashCode(), new RealValue(1.5).hashCode());
        assertNotEquals(new RealValue(1.5).hashCode(), new RealValue(2.5).hashCode());
        assertEquals(RealValue.NAN.hashCode(), RealValue.NAN.hashCode());
        assertEquals(RealValue.POS_INF.hashCode(), RealValue.POS_INF.hashCode());
    }

    @Test
    void testAdditionWithNaN() {
        assertEquals(RealValue.NAN, new RealValue(1.0).add(RealValue.NAN));
        assertEquals(RealValue.NAN, new RealValue(-1.0).add(RealValue.NAN));
    }

    @Test
    void testSubtractionWithNaN() {
        assertEquals(RealValue.NAN, new RealValue(1.0).sub(RealValue.NAN));
        assertEquals(RealValue.NAN, new RealValue(-1.0).sub(RealValue.NAN));
    }

    @Test
    void testMultiplicationWithNaN() {
        assertEquals(RealValue.NAN, new RealValue(2.0).mul(RealValue.NAN));
        assertEquals(RealValue.NAN, new RealValue(-2.0).mul(RealValue.NAN));
    }

    @Test
    void testDivisionWithNaN() {
        assertEquals(RealValue.NAN, new RealValue(3.0).div(RealValue.NAN));
        assertEquals(RealValue.NAN, new RealValue(-3.0).div(RealValue.NAN));
    }

    @Test
    void testAdditionWithInfinity() {
        assertEquals(RealValue.POS_INF, new RealValue(1.0).add(RealValue.POS_INF));
        assertEquals(RealValue.NEG_INF, new RealValue(-1.0).add(RealValue.NEG_INF));
    }

    @Test
    void testMultiplicationWithInfinity() {
        assertEquals(RealValue.POS_INF, new RealValue(2.0).mul(RealValue.POS_INF));
        assertEquals(RealValue.NEG_INF, new RealValue(-2.0).mul(RealValue.POS_INF));
    }

    @Test
    void testSubtractionWithInfinity() {
        assertEquals(RealValue.NEG_INF, new RealValue(1.0).sub(RealValue.POS_INF));
        assertEquals(RealValue.POS_INF, new RealValue(-1.0).sub(RealValue.NEG_INF));
    }

    @Test
    void testDivisionWithInfinity() {
        assertEquals(RealValue.NAN, new RealValue(3.0).div(RealValue.POS_INF));
        assertEquals(RealValue.NAN, new RealValue(-3.0).div(RealValue.POS_INF));
    }

    @Test
    void testModuloWithInfinity() {
        assertEquals(RealValue.NAN, new RealValue(3.0).mod(RealValue.POS_INF));
        assertEquals(RealValue.NAN, new RealValue(-3.0).mod(RealValue.POS_INF));
    }

    @Test
    void testModuloWithNaN() {
        assertEquals(RealValue.NAN, new RealValue(3.0).mod(RealValue.NAN));
        assertEquals(RealValue.NAN, new RealValue(-3.0).mod(RealValue.NAN));
    }

    @Test
    void testHandlingSpecialMultiplicationCases() {
        // 0 * Infinity = NaN
        assertEquals(RealValue.NAN, new RealValue(0.0).mul(RealValue.POS_INF));
        assertEquals(RealValue.NAN, new RealValue(0.0).mul(RealValue.NEG_INF));
        // 0 * NaN = NaN
        assertEquals(RealValue.NAN, new RealValue(0.0).mul(RealValue.NAN));
        // Infinity * 0 = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.mul(new RealValue(0.0)));
        assertEquals(RealValue.NAN, RealValue.NEG_INF.mul(new RealValue(0.0)));
        // NaN * 0 = NaN
        assertEquals(RealValue.NAN, RealValue.NAN.mul(new RealValue(0.0)));
        // Infinity * Infinity = Infinity
        assertEquals(RealValue.POS_INF, RealValue.POS_INF.mul(RealValue.POS_INF));
        assertEquals(RealValue.POS_INF, RealValue.NEG_INF.mul(RealValue.NEG_INF));
        assertEquals(RealValue.NEG_INF, RealValue.POS_INF.mul(RealValue.NEG_INF));
        // Infinity * NaN = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.mul(RealValue.NAN));
        assertEquals(RealValue.NAN, RealValue.NEG_INF.mul(RealValue.NAN));
        // NaN * Infinity = NaN
        assertEquals(RealValue.NAN, RealValue.NAN.mul(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NAN.mul(RealValue.NEG_INF));

        // Infinity * -1 = -Infinity
        assertEquals(RealValue.NEG_INF, RealValue.POS_INF.mul(new RealValue(-1.0)));
        assertEquals(RealValue.POS_INF, RealValue.NEG_INF.mul(new RealValue(-1.0)));
    }

    @Test
    void testHandlingSpecialDivisionCases() {
        // Infinity / Infinity = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.div(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NEG_INF.div(RealValue.NEG_INF));
        assertEquals(RealValue.NAN, RealValue.POS_INF.div(RealValue.NEG_INF));
        // NaN / Infinity = NaN
        assertEquals(RealValue.NAN, RealValue.NAN.div(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NAN.div(RealValue.NEG_INF));
        // Infinity / NaN = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.div(RealValue.NAN));
        assertEquals(RealValue.NAN, RealValue.NEG_INF.div(RealValue.NAN));
        // 1.0 / Infinity = NaN
        assertEquals(RealValue.NAN, new RealValue(1.0).div(RealValue.POS_INF));
        assertEquals(RealValue.NAN, new RealValue(1.0).div(RealValue.NEG_INF));
        // -1.0 / Infinity = NaN
        assertEquals(RealValue.NAN, new RealValue(-1.0).div(RealValue.POS_INF));
        assertEquals(RealValue.NAN, new RealValue(-1.0).div(RealValue.NEG_INF));
    }

    @Test
    void testHandlingSpecialAdditionCases() {
        // Infinity + (-Infinity) = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.add(RealValue.NEG_INF));
        // NaN + anything = NaN
        assertEquals(RealValue.NAN, RealValue.NAN.add(new RealValue(1.0)));
        assertEquals(RealValue.NAN, RealValue.NAN.add(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NAN.add(RealValue.NEG_INF));
    }

    @Test
    void testHandlingSpecialSubtractionCases() {
        // Infinity - Infinity = NaN
        assertEquals(RealValue.NAN, RealValue.POS_INF.sub(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NEG_INF.sub(RealValue.NEG_INF));
        // NaN - anything = NaN
        assertEquals(RealValue.NAN, RealValue.NAN.sub(new RealValue(1.0)));
        assertEquals(RealValue.NAN, RealValue.NAN.sub(RealValue.POS_INF));
        assertEquals(RealValue.NAN, RealValue.NAN.sub(RealValue.NEG_INF));
    }

    @Test
    void testAdditionWithMixedTypes() {
        assertEquals(new RealValue(2.0), new RealValue(1.5).add(new RationalValue(1, 2)));
        assertEquals(new RealValue(3.0), new RealValue(1.5).add(new RationalValue(3, 2)));
        assertEquals(new RealValue(3.5), new RealValue(1.5).add(new IntegerValue(2)));
        assertEquals(new RealValue(0.5), new RealValue(1.5).add(new IntegerValue(-1)));
        assertEquals(new ComplexValue(2.0, 0), new RealValue(1.5).add(new ComplexValue(0.5, 0)));
        assertEquals(new ComplexValue(1.5, 0), new RealValue(1.5).add(new ComplexValue(0, 0)));
    }

    @Test
    void testSubtractionWithMixedTypes() {
        assertEquals(new RealValue(1.0), new RealValue(1.5).sub(new RationalValue(1, 2)));
        assertEquals(new RealValue(0.0), new RealValue(1.5).sub(new RationalValue(3, 2)));
        assertEquals(new RealValue(-0.5), new RealValue(1.5).sub(new IntegerValue(2)));
        assertEquals(new RealValue(3.5), new RealValue(1.5).sub(new IntegerValue(-2)));
        assertEquals(new ComplexValue(1.0, 0), new RealValue(1.5).sub(new ComplexValue(0.5, 0)));
        assertEquals(new ComplexValue(1.5, 0), new RealValue(1.5).sub(new ComplexValue(0, 0)));
    }

    @Test
    void testMultiplicationWithMixedTypes() {
        assertEquals(new RealValue(0.75), new RealValue(1.5).mul(new RationalValue(1, 2)));
        assertEquals(new RealValue(2.25), new RealValue(1.5).mul(new RationalValue(3, 2)));
        assertEquals(new RealValue(3.0), new RealValue(1.5).mul(new IntegerValue(2)));
        assertEquals(new RealValue(-1.5), new RealValue(1.5).mul(new IntegerValue(-1)));
        assertEquals(new ComplexValue(0.75, 0), new RealValue(1.5).mul(new ComplexValue(0.5, 0)));
        assertEquals(new ComplexValue(0, 0), new RealValue(1.5).mul(new ComplexValue(0, 0)));
    }

    @Test
    void testDivisionWithMixedTypes() {
        assertEquals(new RealValue(3.0), new RealValue(1.5).div(new RationalValue(1, 2)));
        assertEquals(new RealValue(1.0), new RealValue(1.5).div(new RationalValue(3, 2)));
        assertEquals(new RealValue(0.75), new RealValue(1.5).div(new IntegerValue(2)));
        assertEquals(new RealValue(-1.5), new RealValue(1.5).div(new IntegerValue(-1)));
        assertEquals(new ComplexValue(3.0, 0), new RealValue(1.5).div(new ComplexValue(0.5, 0)));
        assertThrows(ArithmeticException.class, () -> new RealValue(1.5).div(new ComplexValue(0, 0)));
    }

    @Test
    void testPowerWithMixedTypes() {
        assertEquals(new RealValue(1.837117307), new RealValue(1.5).pow(new RationalValue(3, 2)));
        assertEquals(new RealValue(3.375), new RealValue(1.5).pow(new IntegerValue(3)));
        assertEquals(new RealValue(0.6666666666666666), new RealValue(1.5).pow(new IntegerValue(-1)));
        assertThrows(IllegalArgumentException.class, () -> new RealValue(1.5).pow(new ComplexValue(3, 0)));
        assertThrows(IllegalArgumentException.class, () -> new RealValue(1.5).pow(new ComplexValue(Double.POSITIVE_INFINITY, 0)));
    }

    @Test
    void testEqualityWithMixedTypes() {
        assertEquals(new RealValue(1.5), new RationalValue(3, 2).toReal());
        assertEquals(new RealValue(1.5), new ComplexValue(1.5, 0).toReal());
        assertEquals(new RealValue(2.0), new IntegerValue(2).toReal());
        assertThrows(IllegalStateException.class, () -> new ComplexValue(1.5, 1).toReal());
        assertNotEquals(new RealValue(1.5), new RationalValue(3, 2).toReal().add(new RealValue(0.0001)));
    }

}