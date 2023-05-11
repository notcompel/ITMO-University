package expression;

import java.math.BigInteger;
import java.util.Objects;

public class Variable extends Element {
    String var;
    public Variable(String var) {
        this.var = var;
        this.type = "var";
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return x;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public String toMiniString() {
        return var;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public int hashCode() {
        if (var.equals("x")) {
            return 17;
        } else if (var.equals("y")) {
            return 17 * 17;
        } else {
            return 17 * 17 * 17;
        }
    }

    @Override
    public String makeMiniString() {
        return var;
    }


    @Override
    public String makeString() {
        return var;
    }

    @Override
    public int makeHashCode() {
        return hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (var.equals("x")) {
            return x;
        }
        if (var.equals("y")) {
            return y;
        }
        return z;
    }
}
