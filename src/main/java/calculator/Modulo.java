package calculator;
import java.util.List;


/** This class represents the arithmetic modulo operation "%".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Times
 * @see Plus
 * @see Divides
 */
public final class Modulo extends Operation {

    /**
     * Class constructor specifying a number of Expressions to apply the modulo operation to.
     *
     * @param elist The list of Expressions to apply the modulo operation to
     * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
     * @see #Modulo(List<Expression>,Notation)
     */
    public Modulo(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Class constructor specifying a number of Expressions to apply the modulo operation to,
     * as well as the notation used to represent the operation.
     *
     * @param elist The list of Expressions to apply the modulo operation to
     * @param n The Notation to be used to represent the operation
     * @throws IllegalConstruction  If an empty list of expressions if passed as parameter
     * @see #Modulo(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
    public Modulo(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist,n);
        symbol = "%";
        neutral = new IntegerValue(0);
    }

    public NumberValue op(NumberValue l, NumberValue r) throws ArithmeticException {
    if (r.equals(new IntegerValue(0))) {
        throw new ArithmeticException("Modulo by zero is not allowed.");
    }
        return l.mod(r);
    }
    
}
