package calculator;

import visitor.Visitor;

/**
 * MyNumber becomes a wrapper around NumberValue.
 *
 * This abstraction makes it possible to decouple the representation
 * of numbers in their domain (integer, rational, real, complex)
 *
 * This step is essential to allow the expansion of the system without
 * modifying the structure of existing AST nodes.
 *
 * @see Expression
 * @see Operation
 */
public class MyNumber implements Expression
{
    private final NumberValue value;

    /** getter method to obtain the value contained in the object
     *
     * @return The NumberValue contained in the object
     */
    public NumberValue getValue() { return value; }

    /**
     * Constructor method for integer values
     *
     * @param v The integer value to be contained in the object
     */
    public MyNumber(int v) {
        value = new IntegerValue(v);
    }

    /**
     * Constructor method for any NumberValue domain
     *
     * @param v The NumberValue to be contained in the object
     */
    public MyNumber(NumberValue v) {
        value = v;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int countDepth() { return 0; }
    public int countOps()   { return 0; }
    public int countNbs()   { return 1; }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof MyNumber)) return false;
        return this.value.equals(((MyNumber) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}