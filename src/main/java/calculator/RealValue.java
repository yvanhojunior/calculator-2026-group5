package calculator;

/**
 * Representation of a real number (double) in the multi-domain model.
 * Handles special cases: NaN, +infinity, -infinity.
 *
 * Examples:
 *  - 0.0 / 0.0 = NaN
 *  - 1.0 / 0.0 = +Infinity
 *  - -1.0 / 0.0 = -Infinity
 *  - sqrt(-1.0) = NaN (use ComplexValue for imaginary results)
 */
public class RealValue implements NumberValue {

    private final double value;

    public RealValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public NumberValue add(NumberValue other) {
        RealValue o = (RealValue) other;
        return new RealValue(this.value + o.value);
    }

    @Override
    public NumberValue sub(NumberValue other) {
        RealValue o = (RealValue) other;
        return new RealValue(this.value - o.value);
    }

    @Override
    public NumberValue mul(NumberValue other) {
        RealValue o = (RealValue) other;
        return new RealValue(this.value * o.value);
    }

    @Override
    public NumberValue div(NumberValue other) {
        RealValue o = (RealValue) other;
        // Java gère automatiquement NaN et Infinity pour les doubles :
        // 0.0/0.0 = NaN, 1.0/0.0 = Infinity, -1.0/0.0 = -Infinity
        return new RealValue(this.value / o.value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof RealValue)) return false;
        return Double.compare(this.value, ((RealValue) o).value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}