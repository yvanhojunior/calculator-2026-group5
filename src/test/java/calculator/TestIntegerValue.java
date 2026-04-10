package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import io.cucumber.java.lu.a.as;
import io.cucumber.java.sl.In;

public class TestIntegerValue {

    @Test
    void testAddition() {
        assertEquals(new IntegerValue(5), new IntegerValue(2).add(new IntegerValue(3)));
        assertEquals(new IntegerValue(-1), new IntegerValue(2).add(new IntegerValue(-3)));
        assertEquals(new IntegerValue(0), new IntegerValue(0).add(new IntegerValue(0)));
    }

    @Test
    void testSubtraction() {
        assertEquals(new IntegerValue(-1), new IntegerValue(2).sub(new IntegerValue(3)));
        assertEquals(new IntegerValue(5), new IntegerValue(2).sub(new IntegerValue(-3)));
        assertEquals(new IntegerValue(0), new IntegerValue(0).sub(new IntegerValue(0)));
    }

    @Test
    void testMultiplication() {
        assertEquals(new IntegerValue(6), new IntegerValue(2).mul(new IntegerValue(3)));
        assertEquals(new IntegerValue(-6), new IntegerValue(2).mul(new IntegerValue(-3)));
        assertEquals(new IntegerValue(0), new IntegerValue(0).mul(new IntegerValue(5)));
    }

    @Test
    void testDivision() {
        assertEquals(new IntegerValue(2), new IntegerValue(6).div(new IntegerValue(3)));
        assertEquals(new IntegerValue(-2), new IntegerValue(6).div(new IntegerValue(-3)));
    }

    @Test
    void testPower() {
        assertEquals(new IntegerValue(8), new IntegerValue(2).pow(new IntegerValue(3)));
        assertEquals(new IntegerValue(1), new IntegerValue(2).pow(new IntegerValue(0)));
        assertEquals(new IntegerValue(0), new IntegerValue(0).pow(new IntegerValue(5)));
        assertEquals(new IntegerValue(1), new IntegerValue(0).pow(new IntegerValue(0)));
    }

    @Test
    void testPowerWithZeroExponent() {
        assertEquals(new IntegerValue(1), new IntegerValue(5).pow(new IntegerValue(0)));
        assertEquals(new IntegerValue(1), new IntegerValue(-3).pow(new IntegerValue(0)));
        assertEquals(new IntegerValue(1), new IntegerValue(0).pow(new IntegerValue(0)));
    }

    @Test
    void testModulo() {
        assertEquals(new IntegerValue(1), new IntegerValue(5).mod(new IntegerValue(2)));
        assertEquals(new IntegerValue(0), new IntegerValue(4).mod(new IntegerValue(2)));
        assertEquals(new IntegerValue(1), new IntegerValue(-5).mod(new IntegerValue(2)));
        assertThrows(ArithmeticException.class, () -> new IntegerValue(5).mod(new IntegerValue(0)));
    }

    @Test
    void testDivisionByZero() {
        assertEquals(RealValue.POS_INF, new IntegerValue(5).div(new IntegerValue(0)));
        assertEquals(RealValue.NEG_INF, new IntegerValue(-5).div(new IntegerValue(0)));
        assertEquals(RealValue.NAN, new IntegerValue(0).div(new IntegerValue(0)));
    }

    @Test
    void testToString() {
        assertEquals("5", new IntegerValue(5).toString());
        assertEquals("-3", new IntegerValue(-3).toString());
        assertEquals("0", new IntegerValue(0).toString());
    }

    @Test
    void testEquality() {
        assertEquals(new IntegerValue(5), new IntegerValue(5));
        assertNotEquals(new IntegerValue(5), new IntegerValue(-5));
        assertNotEquals(new IntegerValue(5), new RealValue(5.0));
    }

    @Test
    void testAdditionWithMixedTypes() {
        assertEquals(new RealValue(5.5), new IntegerValue(2).add(new RealValue(3.5)));
        assertEquals(new ComplexValue(2, 3), new IntegerValue(2).add(new ComplexValue(0, 3)));
    }

    @Test
    void testSubtractionWithMixedTypes() {
        assertEquals(new RealValue(-1.5), new IntegerValue(2).sub(new RealValue(3.5)));
        assertEquals(new ComplexValue(2, -3), new IntegerValue(2).sub(new ComplexValue(0, 3)));
    }

    @Test
    void testMultiplicationWithMixedTypes() {
        assertEquals(new RealValue(7.0), new IntegerValue(2).mul(new RealValue(3.5)));
        assertEquals(new ComplexValue(0, 6), new IntegerValue(2).mul(new ComplexValue(0, 3)));
    }

    @Test
    void testDivisionWithMixedTypes() {
        assertEquals(new RealValue(0.3333333333333333), new IntegerValue(1).div(new RealValue(3.0)));
    }

    @Test
    void testPowerWithMixedTypes() {
        assertEquals(new RealValue(8.0), new IntegerValue(2).pow(new RealValue(3.0)));
        assertThrows(IllegalArgumentException.class, () -> new IntegerValue(2).pow(new ComplexValue(0, 3)));
    }

     @Test
    void testModuloWithMixedTypes() {
        assertThrows(IllegalArgumentException.class, () -> new IntegerValue(5).mod(new ComplexValue(2, 0)));
    }
}
