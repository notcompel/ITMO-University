package expression;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public abstract class BinaryOperation implements Basic{
    protected Basic object1, object2;
    protected String type;
    protected String miniString = "";
    protected String string = "";
    int hash = 0;
    public static int priority;

    public BinaryOperation(Basic object1, Basic object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public int evaluate(int val) {
        int x = object1.evaluate(val);
        int y = object2.evaluate(val);
        return eval(x, y);
    }

    public int evaluate(int val1, int val2, int val3) {
        int x = object1.evaluate(val1, val2, val3);
        int y = object2.evaluate(val1, val2, val3);
        return eval(x, y);
    }

    public BigInteger evaluate(BigInteger val) {
        BigInteger x = object1.evaluate(val);
        BigInteger y = object2.evaluate(val);
        return eval(x, y);
    }

    protected abstract int eval(int x, int y);

    protected abstract BigInteger eval(BigInteger x, BigInteger y);


    @Override
    public String toString() {
        if (string.equals("")) {
            string = makeString();
        }
        return string;
    }

    public String makeString() {
        return "(" + object1.makeString() + " " + type + " " + object2.makeString() + ")";
    }

    public String makeMiniString() {
        StringBuilder sb = new StringBuilder();
        int prior = getPriority();
        int prior1 = object1.getPriority();
        int prior2 = object2.getPriority();

        boolean brackets = object1.needSpecialBrackets(type, "left");
        if (prior1 < prior || brackets) {
            sb.append("(").append(object1.makeMiniString()).append(")");
        } else {
            sb.append(object1.makeMiniString());
        }

        sb.append(" ").append(type).append(" ");

        brackets = object2.needSpecialBrackets(type, "right");
        if (prior > prior2 || brackets) {
            sb.append("(").append(object2.makeMiniString()).append(")");
        } else {
            sb.append(object2.makeMiniString());
        }
        return sb.toString();
    }
    @Override
     public String toMiniString() {
        if (miniString.equals("")) {
            miniString = makeMiniString();
        }
        return miniString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() == obj.getClass()) {
            final BinaryOperation curr = (BinaryOperation) obj;

             return curr.object1.equals(object1)
                    && curr.object2.equals(object2)
                    && curr.type.equals(type)
                    && curr.hash == hash;
        }
        return false;
    }

    public int makeHashCode() {
        int a = object1.makeHashCode();
        int b = object2.makeHashCode();
        return 17 * (17 * a + b) + type.hashCode();
    }
    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = makeHashCode();
        }
        return hash;
    }

    public String getType() {
        return type;
    }
}
//поменять хэши
//добавить класс унарных
//evaluate разнести по классам




/* public String makeMiniString() {
        StringBuilder sb = new StringBuilder();
        int prior = priority.get(getType());
        int prior1 = priority.get(object1.getType());
        int prior2 = priority.get(object2.getType());

        if (prior1 <= 2 && prior >= 3
                || prior1 <= -1 && prior > -1) {
            sb.append("(").append(object1.makeMiniString()).append(")");
        } else {
            sb.append(object1.makeMiniString());
        }
        sb.append(" ").append(type).append(" ");
        if (type.equals("/") && prior2 < 5
                || type.equals("-") && prior == prior2
                || prior > prior2) {
            sb.append("(").append(object2.makeMiniString()).append(")");
        } else {
            sb.append(object2.makeMiniString());
        }
        return sb.toString();
    } */