package expression;

import java.math.BigInteger;

public abstract class UnaryOperation implements Basic {
    protected Basic object;
    protected String type;
    protected String string = "";
    protected String miniString = "";
    public static int priority = 4;

    public UnaryOperation(Basic object) {
        this.object = object;
    }

    public int evaluate(int val) {
        int x = object.evaluate(val);
        return eval(x);
    }

    public int evaluate(int val1, int val2, int val3) {
        int x = object.evaluate(val1, val2, val3);
        return eval(x);
    }

    public BigInteger evaluate(BigInteger val) {
        BigInteger x = object.evaluate(val);
        return eval(x);
    }

    protected abstract int eval(int x);

    protected abstract BigInteger eval(BigInteger x);

    public String toString() {
        if (string.equals("")) {
            string = makeString();
        }
        return string;
    }

    public String toMiniString() {
        if (miniString.equals("")) {
            miniString = makeMiniString();
        }
        return miniString;
    }

    @Override
    public String makeMiniString() {
        if (object instanceof Variable || object instanceof Const || object instanceof UnaryOperation) {
            return type + " " + object.toMiniString();
        } else {
            return type + "(" + object.toMiniString() + ")";
        }
    }

    @Override
    public String makeString() {
        return type + "(" + object.toString() + ")";
    }

    @Override
    public String getType() {
        return "unary";
    }
    @Override
    public int makeHashCode() {
        return 17 * object.makeHashCode();
    }

}
