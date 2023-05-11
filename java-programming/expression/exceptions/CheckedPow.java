package expression.exceptions;

import expression.BinaryOperation;
import expression.Basic;

import java.math.BigInteger;

public class CheckedPow extends BinaryOperation {
    static int priority = 3;
    public CheckedPow(Basic object1, Basic object2) {
        super(object1, object2);
        type = "**";
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
        int res = 1;
        if (y < 0 || (x == 0 && y == 0)) {
            throw new IllegalArgumentException(x + " pow " + y);
        }
        if (x == 1) {
            return 1;
        }
        if (x == -1) {
            return y % 2 == 0 ? 1 : -1;
        }
        while (y > 0) {
            if (x > 0 && res > 0 && Integer.MAX_VALUE / res < x ||
                    x > 0 && res < 0 && Integer.MIN_VALUE / x > res ||
                    x < 0 && res > 0 && Integer.MIN_VALUE / res > x ||
                    x < 0 && res < 0 && Integer.MAX_VALUE / res > x) {
                throw new OverflowException(x, type, y);
            }
            res *= x;
            y--;
        }
        return res;
    }

    @Override
    protected BigInteger eval(BigInteger x, BigInteger y) {
        return null;
    }
}
