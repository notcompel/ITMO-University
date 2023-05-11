package expression.exceptions;

public class OverflowException extends ArithmeticException {
    public OverflowException() {
        super("overflow");
    }

    public OverflowException(int x, String operation, int y) {
        super(String.format("overflow: %d %s %d", x, operation, y));
    }

    public OverflowException(String operation, int x) {
        super(String.format("overflow: %s%d", operation, x));
    }
}
