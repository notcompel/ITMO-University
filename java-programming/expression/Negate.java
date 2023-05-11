package expression;

import java.math.BigInteger;

public class Negate extends UnaryOperation {
    public Negate(Basic object) {
        super(object);
        this.type = "-";
    }

    @Override
    protected int eval(int x) {
        return -x;
    }

    @Override
    protected BigInteger eval(BigInteger x) {
        return x.negate();
    }

    public boolean needSpecialBrackets (String prevOperation, String side) {
        return false;
    }


    @Override
    public int getPriority() {
        return 4;
    }

}
