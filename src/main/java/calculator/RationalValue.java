package calculator;

/**
 * Representation of a rational number (fraction p/q) in the multi-domain model.
 * The fraction is always stored in reduced form (e.g. 4/6 becomes 2/3).
 * The denominator is always positive (e.g. 1/-2 becomes -1/2).
 *
 * Examples:
 *  - 1/3 + 1/6 = 1/2
 *  - 1/3 * 3/1 = 1/1
 *  - 1/0 throws ArithmeticException (division by zero)
 */
public class RationalValue implements NumberValue {

    private final long numerator;
    private final long denominator;

    public RationalValue(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Denominator cannot be zero.");
        }
        // On normalise le signe : le dénominateur est toujours positif
        long sign = denominator < 0 ? -1 : 1;
        long gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = sign * numerator / gcd;
        this.denominator = sign * denominator / gcd;
    }

    public long getNumerator() { return numerator; }
    public long getDenominator() { return denominator; }

    // Calcule le plus grand diviseur commun (algorithme d'Euclide)
    // Permet de réduire la fraction automatiquement
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @Override
    public NumberValue add(NumberValue other) {
        RationalValue o = (RationalValue) other;
        return new RationalValue(
                this.numerator * o.denominator + o.numerator * this.denominator,
                this.denominator * o.denominator
        );
    }

    @Override
    public NumberValue sub(NumberValue other) {
        RationalValue o = (RationalValue) other;
        return new RationalValue(
                this.numerator * o.denominator - o.numerator * this.denominator,
                this.denominator * o.denominator
        );
    }

    @Override
    public NumberValue mul(NumberValue other) {
        RationalValue o = (RationalValue) other;
        return new RationalValue(
                this.numerator * o.numerator,
                this.denominator * o.denominator
        );
    }

    @Override
    public NumberValue div(NumberValue other) {
        RationalValue o = (RationalValue) other;
        if (o.numerator == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return new RationalValue(
                this.numerator * o.denominator,
                this.denominator * o.numerator
        );
    }

    @Override
    public NumberValue pow(NumberValue other) {
        if (other instanceof IntegerValue o) {
            long exp = o.getValue();
            if (exp < 0) {
                return new RealValue(Math.pow((double) numerator / denominator, exp));
            }
            long num = (long) Math.pow(numerator, exp);
            long den = (long) Math.pow(denominator, exp);
            return new RationalValue(num, den);
        }
        return new RealValue(Math.pow((double) numerator / denominator, ((RealValue) other).getValue().doubleValue()));
    }

    @Override
    public String toString() {
        if (denominator == 1) return Long.toString(numerator);
        // Forme mixte: si |numérateur| > dénominateur, afficher "a b/c"
        long absNumerator = Math.abs(numerator);
        if (absNumerator > denominator) {
            long wholePart = numerator / denominator;
            long remainder = Math.abs(numerator % denominator);
            if (remainder == 0) return Long.toString(wholePart);
            return wholePart + " " + remainder + "/" + denominator;
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof RationalValue)) return false;
        RationalValue other = (RationalValue) o;
        return this.numerator == other.numerator
                && this.denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(numerator) * 31 + Long.hashCode(denominator);
    }
}