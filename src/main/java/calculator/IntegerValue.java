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
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value + o.value);
        }

        @Override
        public NumberValue sub(NumberValue other) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value - o.value);
        }

        @Override
        public NumberValue mul(NumberValue other) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value * o.value);
        }

        @Override
        public NumberValue div(NumberValue other) {
            IntegerValue o = (IntegerValue) other;
            return new IntegerValue(this.value / o.value);
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

}
