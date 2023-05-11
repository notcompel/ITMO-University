package expression.exceptions;

import expression.BinaryOperation;
import expression.Basic;


import java.math.BigInteger;

public class CheckedMultiply extends BinaryOperation {
    public static int priority = 2;

    public CheckedMultiply(Basic object1, Basic object2) {
        super(object1, object2);
        type = "*";
    }

    public BigInteger eval(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    public int eval(int x, int y) throws OverflowException {
        if (x > 0 && y > 0 && Integer.MAX_VALUE / y < x ||
                x > 0 && y < 0 && Integer.MIN_VALUE / x > y ||
                x < 0 && y > 0 && Integer.MIN_VALUE / y > x ||
                x < 0 && y < 0 && Integer.MAX_VALUE / y > x) {
            throw new OverflowException(x, type, y);
        }
        return x * y;
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return prevOperation.equals("/") && side.equals("right");
    }

    public int getPriority() {
        return 2;
    }
}
