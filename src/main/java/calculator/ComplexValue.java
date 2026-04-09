package calculator;

/**
 * Representation of a complex number (a + bi) in the multi-domain model.
 * A complex number has a real part and an imaginary part.
 *
 * Examples:
 *  - (2+3i) + (1+2i) = 3+5i
 *  - (2+3i) * (1+2i) = (2-6) + (4+3)i = -4+7i
 *  - (2+3i) / (1+2i) = (2+3i)(1-2i) / (1+4) = (8-i) / 5 = 8/5 - 1/5 i
 *  - division by (0+0i) throws ArithmeticException
 */
public class ComplexValue implements NumberValue {

    private final double real;
    private final double imaginary;

    public ComplexValue(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() { return real; }
    public double getImaginary() { return imaginary; }

    @Override
    public NumberValue add(NumberValue other) {
        ComplexValue o = (ComplexValue) other;
        return new ComplexValue(this.real + o.real, this.imaginary + o.imaginary);
    }

    @Override
    public NumberValue sub(NumberValue other) {
        ComplexValue o = (ComplexValue) other;
        return new ComplexValue(this.real - o.real, this.imaginary - o.imaginary);
    }

    @Override
    public NumberValue mul(NumberValue other) {
        ComplexValue o = (ComplexValue) other;
        // (a+bi)(c+di) = (ac-bd) + (ad+bc)i
        return new ComplexValue(
                this.real * o.real - this.imaginary * o.imaginary,
                this.real * o.imaginary + this.imaginary * o.real
        );
    }

    @Override
    public NumberValue div(NumberValue other) {
        ComplexValue o = (ComplexValue) other;
        // (a+bi)/(c+di) = (a+bi)(c-di) / (c²+d²)
        double denominator = o.real * o.real + o.imaginary * o.imaginary;
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero complex number.");
        }
        return new ComplexValue(
                (this.real * o.real + this.imaginary * o.imaginary) / denominator,
                (this.imaginary * o.real - this.real * o.imaginary) / denominator
        );
    }

    @Override
    public NumberValue pow(NumberValue other) {
        if (other instanceof IntegerValue o) {
            int exp = o.getValue();
            if (exp == 0) return new ComplexValue(1, 0);
            // Binary exponentiation using cartesian multiplication
            ComplexValue result = new ComplexValue(1, 0);
            ComplexValue base = new ComplexValue(this.real, this.imaginary);
            int n = exp;
            while (n > 0) {
                if ((n & 1) == 1) {
                    result = (ComplexValue) result.mul(base);
                }
                base = (ComplexValue) base.mul(base);
                n >>= 1;
            }
            // Clean small numerical errors
            double threshold = 1e-7;
            double cleanedReal = Math.abs(result.real) < threshold ? 0.0 : result.real;
            double cleanedImaginary = Math.abs(result.imaginary) < threshold ? 0.0 : result.imaginary;
            return new ComplexValue(cleanedReal, cleanedImaginary);
        }
        throw new IllegalArgumentException("Unsupported exponent type for ComplexValue");
    }


    @Override
    public String toString() {
        if (imaginary == 0) return Double.toString(real);
        if (real == 0) return imaginary + "i";
        if (imaginary < 0) return real + " - " + Math.abs(imaginary) + "i";
        return real + " + " + imaginary + "i";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof ComplexValue)) return false;
        ComplexValue other = (ComplexValue) o;
        return Double.compare(this.real, other.real) == 0
                && Double.compare(this.imaginary, other.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(real) * 31 + Double.hashCode(imaginary);
    }
}