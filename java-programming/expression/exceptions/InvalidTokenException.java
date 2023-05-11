package expression.exceptions;

public class InvalidTokenException extends ParsingException {
    public InvalidTokenException(char token, int pos) {
        super("unknown token '" + token + "' at position " + pos);
    }
}
