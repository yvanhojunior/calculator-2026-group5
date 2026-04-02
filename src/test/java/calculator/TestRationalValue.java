package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestRationalValue {

    @Test
    void testAddition() {
        // 1/3 + 1/6 = 2/6 + 1/6 = 3/6 = 1/2
        RationalValue a = new RationalValue(1, 3);
        RationalValue b = new RationalValue(1, 6);
        assertEquals(new RationalValue(1, 2), a.add(b));
    }

    @Test
    void testSubtraction() {
        // 3/4 - 1/4 = 2/4 = 1/2
        RationalValue a = new RationalValue(3, 4);
        RationalValue b = new RationalValue(1, 4);
        assertEquals(new RationalValue(1, 2), a.sub(b));
    }

    @Test
    void testMultiplication() {
        // 2/3 * 3/4 = 6/12 = 1/2
        RationalValue a = new RationalValue(2, 3);
        RationalValue b = new RationalValue(3, 4);
        assertEquals(new RationalValue(1, 2), a.mul(b));
    }

    @Test
    void testDivision() {
        // 1/2 / 1/4 = 1/2 * 4/1 = 4/2 = 2/1
        RationalValue a = new RationalValue(1, 2);
        RationalValue b = new RationalValue(1, 4);
        assertEquals(new RationalValue(2, 1), a.div(b));
    }

    @Test
    void testReduction() {
        // 4/6 doit être réduit à 2/3
        assertEquals(new RationalValue(2, 3), new RationalValue(4, 6));
    }

    @Test
    void testNegativeSign() {
        // 1/-2 doit devenir -1/2
        assertEquals(new RationalValue(-1, 2), new RationalValue(1, -2));
    }

    @Test
    void testDivisionByZero() {
        // Division par zéro doit lever une exception
        assertThrows(ArithmeticException.class,
                () -> new RationalValue(1, 2).div(new RationalValue(0, 1)));
    }

    @Test
    void testZeroDenominator() {
        // Construire un rationnel avec dénominateur 0 doit lever une exception
        assertThrows(ArithmeticException.class,
                () -> new RationalValue(1, 0));
    }

    @Test
    void testToString() {
        assertEquals("1/3", new RationalValue(1, 3).toString());
        assertEquals("2", new RationalValue(2, 1).toString());
    }

    @Test
    void testEquals() {
        assertEquals(new RationalValue(1, 2), new RationalValue(2, 4));
        assertNotEquals(new RationalValue(1, 2), new RationalValue(1, 3));
    }

    @Test
    void testToStringMixedNumber() {
        // 3/2 doit afficher "1 1/2"
        assertEquals("1 1/2", new RationalValue(3, 2).toString());
        // 7/3 doit afficher "2 1/3"
        assertEquals("2 1/3", new RationalValue(7, 3).toString());
        // 18/12 réduit à 3/2 doit afficher "1 1/2"
        assertEquals("1 1/2", new RationalValue(18, 12).toString());
    }
}