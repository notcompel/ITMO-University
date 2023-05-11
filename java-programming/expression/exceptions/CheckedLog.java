package expression.exceptions;

import expression.BinaryOperation;
import expression.Basic;

import java.math.BigInteger;

public class CheckedLog extends BinaryOperation {
    static int priority = 3;
    public CheckedLog(Basic object1, Basic object2) {
        super(object1, object2);
        type = "//";
    }

    @Override
    public boolean needSpecialBrackets(String prevOperation, String side) {
        return (prevOperation.equals("**") || prevOperation.equals("//")) && side.equals("right");
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    protected int eval(int x, int y) {
        if (!(x > 0 && y > 0 && y != 1)) {
            throw new IllegalArgumentException(x + " log " + y);
        }

        int res = 1;
        int count = 0;
        while (res <= x / y) {
            if (Integer.MAX_VALUE / res < y) {
                throw new OverflowException();
            }
            res *= y;
            count++;
        }
        return count;
    }

    @Override
    protected BigInteger eval(BigInteger x, BigInteger y) {
        return null;
    }
}
