package calculator;

public interface NumberValue {
    /**
     *  Generique abstraction to represented one number.
     *  allow to supported more numeriques domains :
     *  - integer
     *  - rationnals
     *  - real
     *  - complexes
     *  each impl
     */

        calculator.NumberValue add(calculator.NumberValue other);

        calculator.NumberValue sub(calculator.NumberValue other);

        calculator.NumberValue mul(calculator.NumberValue other);

        calculator.NumberValue div(calculator.NumberValue other);

        calculator.NumberValue pow(calculator.NumberValue other);


}
