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
            if (other instanceof ComplexValue o) {
                return new ComplexValue(this.value + o.getReal(), o.getImaginary());
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).add(other);
            }
            if (other instanceof RationalValue o) {
                return new RationalValue(this.value * o.getDenominator() + o.getNumerator(), o.getDenominator());
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
    }

        @Override
        public NumberValue sub(NumberValue other) {
            if (other instanceof IntegerValue o) {
                return new IntegerValue(this.value - o.value);
            }
            if (other instanceof ComplexValue o) {
                return new ComplexValue(this.value - o.getReal(), -o.getImaginary());
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).sub(other);
            }
            if (other instanceof RationalValue o) {
                return new RationalValue(this.value * o.getDenominator() - o.getNumerator(), o.getDenominator());
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue mul(NumberValue other) {
            if (other instanceof IntegerValue o) {
                return new IntegerValue(this.value * o.value);
            }
            if (other instanceof ComplexValue o) {
                return new ComplexValue(this.value * o.getReal(), this.value * o.getImaginary());
            }
            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).mul(other);
            }
            if (other instanceof RationalValue o) {
                return new RationalValue(this.value * o.getNumerator(), o.getDenominator());
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

            if (other instanceof ComplexValue o) {
                double denominator = o.getReal() * o.getReal() + o.getImaginary() * o.getImaginary();
                if (denominator == 0) {
                    throw new ArithmeticException("Division by zero complex number.");
                }
                return new ComplexValue(
                        (this.value * o.getReal()) / denominator,
                        (-this.value * o.getImaginary()) / denominator
                );
            }

            if (other instanceof RealValue) {
                return new RealValue(java.math.BigDecimal.valueOf(this.value), RealValue.DEFAULT_PRECISION).div(other);
            }

            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue pow(NumberValue other) {
            if (other instanceof IntegerValue o) {
                if (o.getValue() < 0) {
                    // exposant négatif → résultat réel ex: 2^-1 = 0.5
                    return new RealValue(Math.pow(this.value, o.getValue()));
                }
                return new IntegerValue((int) Math.pow(this.value, o.getValue()));
            }
            if (other instanceof RationalValue o) {
                double exp = (double) o.getNumerator() / o.getDenominator();
                return new RealValue(Math.pow(this.value, exp));
            }
            if (other instanceof RealValue o) {
                return new RealValue(Math.pow(this.value, o.getValue().doubleValue()));
            }
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }

        @Override
        public NumberValue mod(NumberValue other) {
            if (other instanceof IntegerValue o) {
                if (o.value == 0) {
                    throw new ArithmeticException("Modulo by zero is not allowed.");
                }
                int divisor = Math.abs(o.value);
                int result = ((this.value % divisor) + divisor) % divisor;
                return new IntegerValue(result);
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
