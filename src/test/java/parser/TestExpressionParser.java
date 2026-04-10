package parser;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import calculator.Calculator;
import calculator.Expression;
import calculator.IntegerValue;
import calculator.MyNumber;
import calculator.NumberValue;
import calculator.RealValue;
import calculator.parser.ExpressionParser;

public class TestExpressionParser {

	@Test
	void parseEmptyExpressionThrows() {
		Exception exception = assertThrows(Exception.class, () -> ExpressionParser.parse("   "));
		assertTrue(exception.getMessage().contains("Empty expression"));
	}

	@Test
	void parseIntegerLiteralReturnsMyNumber() throws Exception {
		Expression expression = ExpressionParser.parse("42");
		assertInstanceOf(MyNumber.class, expression);
		NumberValue value = ((MyNumber) expression).getValue();
		assertEquals(new IntegerValue(42), value);
	}

	@Test
	void parseRealLiteralReturnsMyNumber() throws Exception {
		Expression expression = ExpressionParser.parse("3.5");
		assertInstanceOf(MyNumber.class, expression);
		NumberValue value = ((MyNumber) expression).getValue();
		assertEquals(new RealValue(3.5), value);
	}

	@Test
	void parseInvalidLiteralThrows() {
		Exception exception = assertThrows(Exception.class, () -> ExpressionParser.parse("abc"));
		assertTrue(exception.getMessage().contains("Invalid number"));
	}

	@Test
	void parseAndEvaluateSimpleOperations() throws Exception {
		Calculator calculator = new Calculator();

		assertEquals(new IntegerValue(5), calculator.eval(ExpressionParser.parse("2+3")));
		assertEquals(new IntegerValue(7), calculator.eval(ExpressionParser.parse("10-3")));
		assertEquals(new IntegerValue(12), calculator.eval(ExpressionParser.parse("3*4")));
		assertEquals(new IntegerValue(5), calculator.eval(ExpressionParser.parse("10/2")));
	}

	@Test
	void parseRespectsOperatorPrecedence() throws Exception {
		Calculator calculator = new Calculator();
		Expression expression = ExpressionParser.parse("2+3*4");
		assertEquals(new IntegerValue(14), calculator.eval(expression));
	}

	@Test
	void parseModuloOperatorAndCalculate() throws Exception {
		Calculator calculator = new Calculator();
        Expression expression = ExpressionParser.parse("10 % 3");
        assertEquals(new IntegerValue(1), calculator.eval(expression));
	}

	@Test
	void parseParenthesizedExpressionThrowsWithCurrentParser() {
		assertThrows(Exception.class, () -> ExpressionParser.parse("(2+3)*4"));
	}

}
