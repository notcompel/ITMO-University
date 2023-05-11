package expression;

import java.math.BigInteger;

public class Max extends BinaryOperation{
    public static int priority = 0;
    public Max(Basic object1, Basic object2) {
        super(object1, object2);
        type = "max";
    }

    @Override
    public int eval(int x, int y) {
        return Math.max(x, y);
    }


    @Override
    public BigInteger eval(BigInteger x, BigInteger y) {
        return x.compareTo(y) > 0 ? x : y;
    }

    @Override
    public boolean needSpecialBrackets(String prevOperation, String Side) {
        return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
