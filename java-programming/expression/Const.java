package expression;

import java.math.BigInteger;
import java.util.Objects;

public class Const extends Element{
    private final BigInteger bigCurr;
    public Const(int curr) {
        this.bigCurr = BigInteger.valueOf(curr);
        this.type = "const";
    }

    public Const(BigInteger bigCurr) {
        this.bigCurr = bigCurr;
        this.type = "const";
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return bigCurr;
    }

    @Override
    public int evaluate(int x) {
        return bigCurr.intValue();
    }

    @Override
    public String toMiniString() {
        return bigCurr.toString();
    }

    @Override
    public String toString() {
       return bigCurr.toString();
    }


    @Override
    public int hashCode() {
        return bigCurr.intValue();
    }

    @Override
    public String makeMiniString() {
        return bigCurr.toString();
    }


    @Override
    public String makeString() {
        return bigCurr.toString();
    }

    @Override
    public int makeHashCode() {
        return bigCurr.intValue();
    }


    @Override
    public int evaluate(int x, int y, int z) {
        return bigCurr.intValue();
    }
}