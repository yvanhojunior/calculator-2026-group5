package calculator;

import visitor.Visitor;

import java.util.List;

public class Power extends Operation{
    public Power(List<Expression> params) throws IllegalConstruction {
        super(params);
    }

    @Override
    public NumberValue op(NumberValue l, NumberValue r) {
        return l.pow(r);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
