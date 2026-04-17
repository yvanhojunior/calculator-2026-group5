package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import io.cucumber.java.lu.a.as;
import io.cucumber.java.sl.In;

class TestComplexValue {

    @Test
    void testAddition() {
        // (2+3i) + (1+2i) = 3+5i
        ComplexValue a = new ComplexValue(2, 3);
        ComplexValue b = new ComplexValue(1, 2);
        assertEquals(new ComplexValue(3, 5), a.add(b));
    }

    @Test
    void testSubtraction() {
        // (5+4i) - (2+1i) = 3+3i
        ComplexValue a = new ComplexValue(5, 4);
        ComplexValue b = new ComplexValue(2, 1);
        assertEquals(new ComplexValue(3, 3), a.sub(b));
    }

    @Test
    void testMultiplication() {
        // (2+3i) * (1+2i) = (2-6) + (4+3)i = -4+7i
        ComplexValue a = new ComplexValue(2, 3);
        ComplexValue b = new ComplexValue(1, 2);
        assertEquals(new ComplexValue(-4, 7), a.mul(b));
    }

    @Test
    void testDivision() {
        // (4+2i) / (2+0i) = 2+1i
        ComplexValue a = new ComplexValue(4, 2);
        ComplexValue b = new ComplexValue(2, 0);
        assertEquals(new ComplexValue(2, 1), a.div(b));
    }

    @Test
    void testDivisionByZero() {
        // Division par (0+0i) doit lever une exception
        assertThrows(ArithmeticException.class,
                () -> new ComplexValue(1, 2).div(new ComplexValue(0, 0)));
    }

    @Test
    void testToStringReal() {
        // Partie imaginaire = 0 -> affiche juste le réel
        assertEquals("2.0", new ComplexValue(2, 0).toString());
    }

    @Test
    void testToStringImaginary() {
        // Partie réelle = 0 -> affiche juste l'imaginaire
        assertEquals("3.0i", new ComplexValue(0, 3).toString());
    }

    @Test
    void testToStringFull() {
        assertEquals("2.0 + 3.0i", new ComplexValue(2, 3).toString());
        assertEquals("2.0 - 3.0i", new ComplexValue(2, -3).toString());
    }

    @Test
    void testEquals() {
        assertEquals(new ComplexValue(2, 3), new ComplexValue(2, 3));
        assertNotEquals(new ComplexValue(2, 3), new ComplexValue(2, 4));
    }

    @Test
    void testImaginaryUnitSquare() {
        // i * i = -1 + 0i
        ComplexValue i = new ComplexValue(0, 1);
        assertEquals(new ComplexValue(-1, 0), i.mul(i));
    }

    @Test
    void testImaginaryUnitPowerTwo() {
        // i^2 = -1 + 0i
        ComplexValue i = new ComplexValue(0, 1);
        assertEquals(new ComplexValue(-1, 0), i.pow(new IntegerValue(2)));
    }

    @Test
    void testImaginaryUnitPowerThree() {
        // i^3 = i^2 * i = -1 * i = -i
        ComplexValue i = new ComplexValue(0, 1);
        assertEquals(new ComplexValue(0, -1), i.pow(new IntegerValue(3)));
    }

    @Test
    void testImaginaryUnitPowerFour() {
        // i^4 = (i^2)^2 = (-1)^2 = 1
        ComplexValue i = new ComplexValue(0, 1);
        assertEquals(new ComplexValue(1, 0), i.pow(new IntegerValue(4)));
    }

    @Test
    void testAdditionWithInteger() {
        // (2+3i) + 4 = 6+3i
        ComplexValue a = new ComplexValue(2, 3);
        IntegerValue b = new IntegerValue(4);
        assertEquals(new ComplexValue(6, 3), a.add(b));
    }

    @Test
    void testSubtractionWithInteger() {
        // (5+4i) - 2 = 3+4i
        ComplexValue a = new ComplexValue(5, 4);
        IntegerValue b = new IntegerValue(2);
        assertEquals(new ComplexValue(3, 4), a.sub(b));
    }

    @Test
    void testMultiplicationWithInteger() {
        // (2+3i) * 2 = 4+6i
        ComplexValue a = new ComplexValue(2, 3);
        IntegerValue b = new IntegerValue(2);
        assertEquals(new ComplexValue(4, 6), a.mul(b));
        IntegerValue zero = new IntegerValue(0);
        assertEquals(new ComplexValue(0, 0), a.mul(zero));
    }

    @Test
    void testDivisionWithInteger() {
        // (4+2i) / 2 = 2+1i
        ComplexValue a = new ComplexValue(4, 2);
        IntegerValue b = new IntegerValue(2);
        assertEquals(new ComplexValue(2, 1), a.div(b));

        // 
        IntegerValue c = new IntegerValue(8);
        assertEquals(new ComplexValue(0.5, 0.25), a.div(c));
    }

    @Test
    void testDivisionByZeroInteger() {
        // Division by zero must throw an exception
        ComplexValue a = new ComplexValue(1, 2);
        IntegerValue zero = new IntegerValue(0);
        assertThrows(ArithmeticException.class, () -> a.div(zero));
    }

    @Test
    void testPowerWithInteger() {
        // (1+2i)^3 = -11 - 2i
        ComplexValue a = new ComplexValue(1, 2);
        IntegerValue b = new IntegerValue(3);
        assertEquals(new ComplexValue(-11, -2), a.pow(b));
    }

    @Test
    void testPowerWithZero() {
        // (2+3i)^0 = 1 + 0i
        ComplexValue a = new ComplexValue(2, 3);
        IntegerValue zero = new IntegerValue(0);
        assertEquals(new ComplexValue(1, 0), a.pow(zero));
    }

    @Test
    void testPowerWithOne() {
        // (2+3i)^1 = 2 + 3i
        ComplexValue a = new ComplexValue(2, 3);
        IntegerValue one = new IntegerValue(1);
        assertEquals(new ComplexValue(2, 3), a.pow(one));
    }

    @Test
    void testAdditionWithReal() {
        // (2+3i) + 1.5 = 3.5 + 3i
        ComplexValue a = new ComplexValue(2, 3);
        RealValue b = new RealValue(1.5);
        assertEquals(new ComplexValue(3.5, 3), a.add(b));
    }

    @Test
    void testSubtractionWithReal() {
        // (5+4i) - 1.5 = 3.5 + 4i
        ComplexValue a = new ComplexValue(5, 4);
        RealValue b = new RealValue(1.5);
        assertEquals(new ComplexValue(3.5, 4), a.sub(b));
    }

    @Test
    void testMultiplicationWithReal() {
        // (2+3i) * 1.5 = 3 + 4.5i
        ComplexValue a = new ComplexValue(2, 3);
        RealValue b = new RealValue(1.5);
        assertEquals(new ComplexValue(3, 4.5), a.mul(b));
    }

    @Test
    void testDivisionWithRealGivesRationalResult() {
        // (4+2i) / 1.5 = 2.666... + 1.333...i
        ComplexValue a = new ComplexValue(2, 2);
        RealValue b = new RealValue(4.0);
        assertEquals(new ComplexValue(0.5, 0.5), a.div(b));
    }

    @Test
    void testDivisionWithRealGivesDecimalResult() {
        // (4+2i) / 1.5 = 2.666... + 1.333...i
        ComplexValue a = new ComplexValue(4, 2);
        RealValue b = new RealValue(1.5);
        assertEquals(new ComplexValue(8.0 / 3, 4.0 / 3), a.div(b));
    }

    @Test
    void testPowerWithReal() {
        // (1+2i)^2.0 = (1+2i)^(2) = -3 + 4i
        ComplexValue a = new ComplexValue(1, 2);
        RealValue b = new RealValue(2.0);
        assertThrows(IllegalArgumentException.class, () -> a.pow(b));
    }

    @Test
    void testPowerWithRealNonInteger() {
        // (1+2i)^(0.5) = sqrt(1+2i) = 1.6741492280355408 + 0.5962847939999438i
        ComplexValue a = new ComplexValue(1, 2);
        RealValue b = new RealValue(0.5);
        assertThrows(IllegalArgumentException.class, () -> a.pow(b));
    }


}