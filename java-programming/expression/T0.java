package expression;

import java.math.BigInteger;

public class T0 extends UnaryOperation{
    public T0(Basic object) {
        super(object);
        this.type = "t0";
    }

    @Override
    protected int eval(int x) {
        return Integer.numberOfTrailingZeros(x);
    }

    @Override
    protected BigInteger eval(BigInteger x) {
        int count = 0;
        while (!x.equals(BigInteger.ZERO)) {
            x = x.divide(BigInteger.TWO);
            count++;
        }
        return BigInteger.valueOf(count);
    }

    @Override
    public boolean needSpecialBrackets(String prevOperation, String Side) {
        return false;
    }


    @Override
    public int getPriority() {
        return 4;
    }
}
