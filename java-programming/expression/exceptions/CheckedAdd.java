package expression.exceptions;

import expression.Basic;
import expression.BinaryOperation;

import java.math.BigInteger;
public class CheckedAdd extends BinaryOperation {

    public static int priority = 1;

    public CheckedAdd(Basic object1, Basic object2) {
        super(object1, object2);
        type = "+";
    }


    public BigInteger eval(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    public int eval(int x, int y) {
        if (y > 0 && Integer.MAX_VALUE - y < x ||
                y < 0 && Integer.MIN_VALUE - y > x) {
            throw new OverflowException(x, type, y);
        }
        return x + y;
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return prevOperation.equals("-") && side.equals("right");
    }

    public int getPriority() {
        return priority;
    }
}
