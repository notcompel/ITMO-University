package expression.parser;

public class BaseParser {
    protected String string;
    private int pos = 0;
    private  static final char END = 0;
    private char ch;

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

    protected String checkNext() {
        skipWhiteSpace();
        int tmp = pos;
        char was = ch;

        String str = takeWord();
        if (str.equals("")) {
            str = "" + (pos > 0 ? string.charAt(pos - 1) : END);
        }
        ch = was;
        pos = tmp;
        return str;
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

    protected boolean take(final String expected) {
        skipWhiteSpace();
        int tmp = pos;
        char was = ch;
        for (final char c : expected.toCharArray()) {
            if (!take(c)) {
                pos = tmp;
                ch = was;
                return false;
            }
        }

        return true;
    }

    protected void expect(final char expected) {
        skipWhiteSpace();
        if (!take(expected)) {
            throw error(String.format(
                    "Expected '%s', found '%s'",
                    expected, ch != END ? ch : "end-of-input"
            ));
        }
    }

    protected void expect(final String expected) {
        skipWhiteSpace();
        for (final char c : expected.toCharArray()) {
            expect(c);
        }
    }

    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(String.format(
                "%d: %s (rest of input: %s)",
                pos, message, string.substring(pos)
        ));
    }

    protected boolean end() {
        return test(END);
    }

    protected boolean between(final char min, final char max) {
        return min <= ch && ch <= max;
    }
}
