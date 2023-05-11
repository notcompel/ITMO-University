package expression;

import java.math.BigInteger;

public class L0 extends UnaryOperation{
    public L0(Basic object) {
        super(object);
        this.type = "l0";
    }

    @Override
    protected int eval(int x) {
        return Integer.numberOfLeadingZeros(x);
    }

    @Override
    protected BigInteger eval(BigInteger x) {
        return null;
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
