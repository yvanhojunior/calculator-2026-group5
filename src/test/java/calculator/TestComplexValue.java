package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
}