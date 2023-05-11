package expression.exceptions;

import expression.UnaryOperation;
import expression.Basic;

import java.math.BigInteger;

public class CheckedNegate extends UnaryOperation {
    public CheckedNegate(Basic object) {
        super(object);
        this.type = "-";
    }

    protected int eval(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException(type, x);
        }
        return -x;
    }

    protected BigInteger eval(BigInteger x) {
        return x.negate();
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return false;
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
