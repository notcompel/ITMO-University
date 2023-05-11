package expression;

public interface Basic extends BigIntegerExpression, Expression, TripleExpression {

    @Override
    String toString();

    @Override
    int hashCode();

    String makeMiniString();
    String makeString();
    int makeHashCode();
    boolean needSpecialBrackets(String prevOperation, String Side);

    String getType();
    int getPriority();
}