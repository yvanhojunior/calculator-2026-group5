package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

class TestEvaluator {

    private Calculator calc;
    private int value1, value2;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
        value1 = 8;
        value2 = 6;
    }

    @Test
    void testEvaluatorMyNumber() {
        // avant: assertEquals(value1, ...) comparait un int avec un NumberValue
        // après: on compare deux IntegerValue
        assertEquals(new IntegerValue(value1), calc.eval(new MyNumber(value1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"*", "+", "/", "-"})
    void testEvaluateOperations(String symbol) {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            switch (symbol) {
                case "+" -> assertEquals(new IntegerValue(value1 + value2), calc.eval(new Plus(params)));
                case "-" -> assertEquals(new IntegerValue(value1 - value2), calc.eval(new Minus(params)));
                case "*" -> assertEquals(new IntegerValue(value1 * value2), calc.eval(new Times(params)));
                case "/" -> assertEquals(new IntegerValue(value1 / value2), calc.eval(new Divides(params)));
                default  -> fail();
            }
        } catch (IllegalConstruction _) {
            fail();
        }
    }
}