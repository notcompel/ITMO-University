package expression.exceptions;

import expression.Basic;
import expression.UnaryOperation;

import java.math.BigInteger;

public class CheckedAbs extends UnaryOperation {
    public CheckedAbs(Basic object) {
        super(object);
        type = "abs";
    }

    @Override
    public boolean needSpecialBrackets(String prevOperation, String Side) {
        return false;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    protected int eval(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException(type, x);
        }
        return Math.abs(x);
    }

    @Override
    protected BigInteger eval(BigInteger x) {
        if (x.compareTo(BigInteger.ZERO) > 0) {
            return x;
        } else return x.negate();
    }
}
