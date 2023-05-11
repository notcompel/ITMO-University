package expression.exceptions;

public class DivisionByZeroException extends ArithmeticException {
    public DivisionByZeroException(int left) {
        super(String.format("division by zero %d / 0", left));
    }
}
