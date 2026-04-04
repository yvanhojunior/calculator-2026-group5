package calculator;

/**
 * Representation of one integer in the new model muti-domain.
 * This class encapsulates the arithmetic behaviour of integer domain.
 *
 * it constitutes the first concrete implemenatation of NumberValue,
 * in order to gradually migrate the project without breaking the current support
 * whole expressions.
 */

public class IntegerValue implements NumberValue {

        private final int value;

        public IntegerValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public NumberValue add(NumberValue other) {
            if (other instanceof IntegerValue o) {
                return new IntegerValue(this.value + o.value);
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).add(other);
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue sub(NumberValue other) {
            if (other instanceof IntegerValue o) {
                return new IntegerValue(this.value - o.value);
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).sub(other);
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue mul(NumberValue other) {
            if (other instanceof IntegerValue o) {
                return new IntegerValue(this.value * o.value);
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).mul(other);
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue div(NumberValue other) {
            if (other instanceof IntegerValue o) {
                if (o.value == 0) {
                    if (this.value == 0) return RealValue.NAN;
                    return this.value > 0 ? RealValue.POS_INF : RealValue.NEG_INF;
                }

                if (this.value % o.value == 0) {
                    return new IntegerValue(this.value / o.value);
                } else {
                    return new RealValue((double) this.value / o.value);
                }
            }

            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).div(other);
            }

            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof IntegerValue)) return false;
        return this.value == ((IntegerValue) o).value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

}
