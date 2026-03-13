package calculator;
/**
 * Generic interface
 * Implementations: IntegerNumber, RationalNumber, RealNumber, ComplexNumber
 */

public interface NumberType <T>{
    /**
     * Adds this number to another
     */
    T add(T other);

    /**
     * Subtracts another number from this
     */
    T subtract(T other);

    /**
     * Multiplies this number by another
     */
    T multiply(T other);

    /**
     * Divides this number by another
     */
    T divide(T other);

    /**
     * Returns true if this number represents an error state (NaN, Infinity, etc.)
     */
    boolean isError();

    /**
     * Returns an error message if isError() is true
     */
    String getErrorMessage();
}
