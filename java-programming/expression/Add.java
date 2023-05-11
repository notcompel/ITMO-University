package expression;

import java.math.BigInteger;

public class Add extends BinaryOperation {
    public static int priority = 1;
    public Add(Basic object1, Basic object2) {
        super(object1, object2);
        type = "+";
    }


    public BigInteger eval(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    public int eval(int x, int y) {
        return x + y;
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return prevOperation.equals("-") && side.equals("right");
    }


    public int getPriority() {
        return priority;
    }
}