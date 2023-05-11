package expression.exceptions;

public class InvalidOperationException extends ParsingException {
    public InvalidOperationException(String operation, int pos) {
        super("not correct " + operation + " operation at position " + pos);
    }
}
