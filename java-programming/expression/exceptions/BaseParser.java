package expression.exceptions;

public class BaseParser {
    protected String string;
    private int pos = 0;
    private  static final char END = 0;
    private char ch;
    static String messageAboutMissingArgument = "expected argument";
    static String messageAboutMissingOperation = "expected operation";
    static String messageAboutClosingParenthesis = "no closing parenthesis";

    public BaseParser(String string) {
        this.string = string;
        take();
    }

    public boolean hasNext() {
        return pos < string.length();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }


    protected char take() {
        skipWhiteSpace();
        final char result = ch;
        ch = hasNext() ? string.charAt(pos++) : END;
        return result;
    }

    protected String checkNext() throws ParsingException {
        skipWhiteSpace();
        int tmp = pos;
        char was = ch;
        StringBuilder str = new StringBuilder();
        if (take("**")) {
            str.append("**");
        } else if (take("//")) {
            str.append("//");
        } else if (take("+")) {
            str.append("+");
        } else if (take("-")) {
            str.append("-");
        } else if (take("*")) {
            str.append("*");
        } else if (take("/")) {
            str.append("/");
        } else if (take("l0")) {
            str.append("l0");
        } else if (take("t0")) {
            str.append("t0");
        } else if (take("(")) {
            str.append("(");
        } else if (take(")")) {
            str.append(")");
        } else if (between('0', '9')) {
            while (between('0', '9')) {
                str.append(take());
            }
        } else if (Character.isLetter(ch)) {
            str.append(takeWord());
        } else if (!end()) {
            throw new InvalidTokenException(ch, pos);
        }
        ch = was;
        pos = tmp;
        return str.toString();
    }

    protected String takeWord() {
        StringBuilder sb = new StringBuilder();
        char c;
        while (between('a', 'z') || between('A', 'Z')) {
            c = take();
            sb.append(c);
        }
        return sb.toString();
    }

    protected void takeAll() {
        ch = hasNext() ? string.charAt(pos++) : END;
    }

    public void skipWhiteSpace() {
        while (takeWhiteSpaces() || takeAll('\n') || takeAll('\r') ||
                takeAll('\t')) {
            //
        }

    }

    private boolean takeWhiteSpaces() {
        if (Character.isWhitespace(ch)) {
            takeAll();
            return true;
        }
        return false;
    }

    protected boolean takeAll(final char expected) {
        if (test(expected)) {
            takeAll();
            return true;
        } else {
            return false;
        }
    }


    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected boolean take(final String expected) throws ParsingException {
        skipWhiteSpace();
        if (expected.equals("min") || expected.equals("max")) {
            if (Character.isLetterOrDigit(string.charAt(pos - 2))) {
                throw new InvalidOperationException(expected, pos - 1);
            }
        }
        int tmp = pos;
        char was = ch;
        for (final char c : expected.toCharArray()) {
            if (!take(c)) {
                pos = tmp;
                ch = was;
                return false;
            }
        }
        if (expected.equals("min") || expected.equals("max")
                || expected.equals("l0") || expected.equals("t0")
                || expected.equals("abs")) {
            if (Character.isLetterOrDigit(ch)) {
                throw new InvalidOperationException(expected, pos - 1);
            }
        }
        return true;
    }

    public ParsingException error(String message) {
        return new ParsingException(String.format(
                "position %d: %s, rest of input:\"%s\"",
                pos, message, string.substring(pos - 1)
        ));
    }

    protected boolean end() {
        return test(END);
    }

    protected boolean between(final char min, final char max) {
        return min <= ch && ch <= max;
    }
}
