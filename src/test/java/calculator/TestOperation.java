package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import visitor.CountingVisitor;

import java.util.Arrays;
import java.util.List;

class TestOperation {

	private Operation o;
	private Operation o2;
	CountingVisitor cv = new visitor.CountingVisitor();

	@BeforeEach
	void setUp() throws Exception {
		List<Expression> params1 = Arrays.asList(new MyNumber(3), new MyNumber(4), new MyNumber(5));
		List<Expression> params2 = Arrays.asList(new MyNumber(5), new MyNumber(4));
		List<Expression> params3 = Arrays.asList(new Plus(params1), new Minus(params2), new MyNumber(7));
		o = new Divides(params3);
		o2 = new Divides(params3);
	}

	@Test
	void testEquals() {
		assertEquals(o,o2);
	}

	@Test
	void testCountDepth() {
		o.accept(cv);
		assertEquals(2, cv.getDepth());
	}

	@Test
	void testCountOps() {
		o.accept(cv);
		assertEquals(3, cv.getOps());
	}

	@Test
	void testCountNbs() {
		o.accept(cv);
		assertEquals(Integer.valueOf(6), cv.getNbs());
	}

}
