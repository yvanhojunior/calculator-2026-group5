package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A very simple calculator in Java
 * University of Mons - UMONS
 * Software Engineering Lab
 * Faculty of Sciences
 *
 * @author tommens
 */
public class Main {

	/**
	 * This is the main method of the application.
	 * It provides examples of how to use it to construct and evaluate arithmetic expressions.
	 *
	 * @param args	Command-line parameters are not used in this version
	 */
	public static void main(String[] args) {

	Expression e;
	Calculator c = new Calculator();
	Logger logger = Logger.getLogger(Main.class.getName());

	try{

		e = new MyNumber(8);
		c.print(e);
		c.eval(e);

	    List<Expression> params = new ArrayList<>();
	    Collections.addAll(params, new MyNumber(3), new MyNumber(4), new MyNumber(5));
	    e = new Plus(params, Notation.PREFIX);
		c.printExpressionDetails(e);
		c.eval(e);

		List<Expression> params2 = new ArrayList<>();
		Collections.addAll(params2, new MyNumber(5), new MyNumber(3));
		e = new Minus(params2, Notation.INFIX);
		c.print(e);
		c.eval(e);

		List<Expression> params3 = new ArrayList<>();
		Collections.addAll(params3, new Plus(params), new Minus(params2));
		e = new Times(params3);
		c.printExpressionDetails(e);
		c.eval(e);

		List<Expression> params4 = new ArrayList<>();
		Collections.addAll(params4, new Plus(params), new Minus(params2), new MyNumber(5));
		e = new Divides(params4, Notation.POSTFIX);
		c.print(e);
		c.eval(e);

        List<RationalNumber> numbers = new ArrayList<>();
        numbers.add(new RationalNumber(1, 2));
        numbers.add(new RationalNumber(1, 2));
        numbers.add(new RationalNumber(1, 2));
        numbers.add(new RationalNumber(1, 2));

        // test RationalNumber
        List<String> operators = new ArrayList<>();
        operators.add("*");
        operators.add("+");
        operators.add("*");

        logger.info("result is : " + RationalNumber.compute(numbers, operators));

        // Test RealNumber
        RealNumber r1 = new RealNumber(0.33);
        RealNumber r2 = new RealNumber(3.14);

        logger.info("r1 = " + r1);
        logger.info("r2 = " + r2);
        logger.info("r1 + r2 = " + r1.add(r2));
        logger.info("r1 - r2 = " + r1.subtract(r2));
        logger.info("r1 * r2 = " + r1.multiply(r2));
        logger.info("r1 / r2 = " + r1.divide(r2));

        // Cas spéciaux
        RealNumber zero = new RealNumber(0.0);
        RealNumber one = new RealNumber(1.0);
        RealNumber negOne = new RealNumber(-1.0);

        logger.info("0.0 / 0.0 = " + zero.divide(zero));
        logger.info("1.0 / 0.0 = " + one.divide(zero));
        logger.info("-1.0 / 0.0 = " + negOne.divide(zero));
        logger.info("sqrt(-1.0) = " + negOne.sqrt());
        logger.info("sqrt(4.0) = " + new RealNumber(4.0).sqrt());
        logger.info("log(-1.0) = " + negOne.log());
        logger.info("log(1.0) = " + one.log());

        logger.info("--------------complex Number ---------------");
        // Test ComplexNumber
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = new ComplexNumber(1, 2);

        logger.info("c1 = " + c1);
        logger.info("c2 = " + c2);
        logger.info("c1 + c2 = " + c1.add(c2));
        logger.info("c1 - c2 = " + c1.subtract(c2));
        logger.info("c1 * c2 = " + c1.multiply(c2));
        logger.info("c1 / c2 = " + c1.divide(c2));

        // Cas spéciaux
        ComplexNumber cZero = new ComplexNumber(0, 0);
        ComplexNumber cNegOne = new ComplexNumber(-1, 0);

        logger.info("sqrt(-1+0i) = " + cNegOne.sqrt());
        logger.info("sqrt(2+3i) = " + c1.sqrt());

// Division par zero
        try {
            logger.info("c1 / 0 = " + c1.divide(cZero));
        } catch (ArithmeticException er) {
            logger.info("Division by zero caught: " + er.getMessage());
        }
	}

	catch(IllegalConstruction _) {
		logger.info("cannot create operations without parameters");
		}
 	}

}
