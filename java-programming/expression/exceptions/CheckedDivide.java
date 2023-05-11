package expression.exceptions;

import expression.Basic;
import expression.BinaryOperation;

import java.math.BigInteger;

public class CheckedDivide extends BinaryOperation {
    public static int priority = 2;

    public CheckedDivide(Basic object1, Basic object2) {
        super(object1, object2);
        type = "/";
    }

    public BigInteger eval(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    public int eval(int x, int y) throws ArithmeticException {
        if (y == 0) {
            throw new DivisionByZeroException(x);
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(x, type, y);
        }
        return x / y;
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return (prevOperation.equals("*") || prevOperation.equals("/")) && side.equals("right");
    }

    public int getPriority() {
        return priority;
    }
}
