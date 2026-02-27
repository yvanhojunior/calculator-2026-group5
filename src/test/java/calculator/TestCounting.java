package calculator;

//Import Junit5 libraries for unit testing:
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import visitor.CountingVisitor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;


class TestCounting {

	private int value1, value2;
	private Expression e;
	CountingVisitor cv = new visitor.CountingVisitor();

	@BeforeEach
	void setUp() {
		value1 = 8;
		value2 = 6;
		e = null;
	}

	@Test
	void testNumberCounting() {
		e = new MyNumber(value1);
		e.accept(cv);
		//test whether a number has zero depth (i.e. no nested expressions)
		assertEquals( 0, cv.getDepth());
		//test whether a number contains zero operations
		assertEquals(0, cv.getOps());
		//test whether a number contains 1 number
		assertEquals(1, cv.getNbs());
	}

	@ParameterizedTest
	@ValueSource(strings = {"*", "+", "/", "-"})
	void testOperationCounting(String symbol) {
		List<Expression> params = Arrays.asList(new MyNumber(value1),new MyNumber(value2));
		try {
			//construct another type of operation depending on the input value
			//of the parameterised test
			switch (symbol) {
				case "+"	->	e = new Plus(params);
				case "-"	->	e = new Minus(params);
				case "*"	->	e = new Times(params);
				case "/"	->	e = new Divides(params);
				default		->	fail();
			}
		} catch (IllegalConstruction _) {
			fail();
		}
		//test whether a binary operation has depth 1
		e.accept(cv);
		assertEquals(1, cv.getDepth(),"counting depth of an Operation");
		//test whether a binary operation contains 1 operation
		assertEquals(1, cv.getOps(), "counting operations in an Operation");
		//test whether a binary operation contains 2 numbers
		assertEquals(2, cv.getNbs(), "counting numbers in an Operation");
	}

}
