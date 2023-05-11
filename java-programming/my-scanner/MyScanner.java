import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class MyScanner {
    private Reader br;
    private StringBuilder sb;
    private char[] buffer = new char[1024];
    private int len = 0, pos = 0;
    boolean end = false;
    public boolean firstinline = true;

    public MyScanner(InputStream in) { //консольный ввод
        br = new InputStreamReader(in);
    }

    public MyScanner(File file) throws FileNotFoundException {
        br = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    public MyScanner(String str) throws FileNotFoundException {
        br = new StringReader(str);
    }

    public void throwsException() {
        throw new NoSuchElementException("NoSuchElementException: " + br.toString());
    }

    private void readBuffer() throws IOException { //считываем буффер
        len = br.read(buffer);
        while (len == 0) {
            len = br.read(buffer);
        }
        if (len == -1) {
            end = true;
        }
        pos = 0; //начало нового буффера
    }

    public boolean hasNextChar() {
        if (end) {
            return false;
        }
        if (pos >= len) {
            try {
                readBuffer();
            } catch (IOException io) {
                throwsException();
            }
            return !end; //если не конец файла то есть символы
        } else {
            return true;
        }
    }

    public char nextChar() throws IOException {
        if (pos >= len) {
            readBuffer();
        }

        return buffer[pos++];
    }

    public boolean hasNextLine() {
        return hasNextChar();
    }

    public boolean hasNextToken() {
        return hasNextChar();
    }

    public boolean lastInLine() throws IOException {
        int prev = pos;
        char c = nextChar();
        while (hasNextChar() && !isWord(c) && c != '\n' && c != '\r') {
            c = nextChar();
        }
        if (c == '\n' || c == '\r') {
            //pos++;
            if (c == '\r' && hasNextChar()) {
                c = nextChar();
                if (c == '\n') {
                    return true;
                } else {
                    pos--;
                }

            }
            return true;
        } else {
            pos = prev;
            return false;
        }
    }

    private boolean isWord(char c) {
        return Character.isLetter(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'';
    }

    private boolean isNum(char c) {
        return c >= '0' && c <= '9' || c == '-' || Character.isLetter(c);
    }

    private boolean check(int i, char c) {
        if (i == 1) {
            return isWord(c);
        } else {
            return isNum(c);
        }
    }

    public String nextPart(int val) throws IOException {
        sb = new StringBuilder();
        char c = nextChar();

        while (hasNextChar() && !check(val, c)) {
            if (c == '\n' || c == '\r') {
                if (c == '\r' && hasNextChar()) {
                    c = nextChar();
                    if (c == '\n') {
                        firstinline = true;
                    } else {
                        pos--;
                    }

                }
                firstinline = true;
            }
            c = nextChar();
        }

        while (hasNextChar()) {
            if (check(val, c)) {
                sb.append(c);
            } else {
                break;
            }
            c = nextChar();
        }
        return sb.toString();
    }


    public String nextWord() throws IOException {
        sb = new StringBuilder();
        char c = nextChar();

        while (hasNextChar() && !isWord(c)) {
            if (c == '\n' || c == '\r') {
                if (c == '\r' && hasNextChar()) {
                    c = nextChar();
                    if (c == '\n') {
                        firstinline = true;
                    } else {
                        pos--;
                    }

                }
                firstinline = true;
            }
            c = nextChar();
        }

        while (hasNextChar()) {
            if (isWord(c)) {
                sb.append(c);
            } else {
                break;
            }
            c = nextChar();
        }

        return sb.toString();
    }


    public String nextLine() throws IOException {
        sb = new StringBuilder();
        char c;
        while (hasNextChar()) {
            c = nextChar();
            if (c == '\n') {
                break;
            }
            if (c == '\r' && hasNextChar()) {
                c = nextChar();
                if (c == '\n') {
                    break;
                } else {
                    pos--;
                }
                break;
            }
            if (c != '\r') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void close() throws IOException {
        br.close();
    }
}


/*import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class MyScanner {
    private Reader br;
    private StringBuilder sb;
    private char[] buffer = new char[Integer.MAX_VALUE / 64];
    private int len = 0, pos = 0;
    boolean end = false;
    public boolean lastinline = false;

    public MyScanner(InputStream in) { //консольный ввод
        br = new InputStreamReader(in);
    }

    public MyScanner(File file) throws FileNotFoundException {
        br = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    public MyScanner(String str) throws FileNotFoundException {
        br = new StringReader(str);
    }

    public void throwsException() {
        throw new NoSuchElementException("NoSuchElementException: " + br.toString());
    }

    private void readBuffer() throws IOException { //считываем буффер
        len = br.read(buffer);
        while (len == 0) {
            len = br.read(buffer);
        } 
        if (len == -1) {
            end = true;
        }
        pos = 0; //начало нового буффера
    }

    public boolean hasNextChar() {
        if (end) {
            return false;
        }
        if (pos >= len) {
            try {
                readBuffer();
            } catch (IOException io) {
                throwsException();
            }
            return !end; //если не конец файла то есть символы
        } else {
            return true;
        }
    }

    public char nextChar() throws IOException {
        if (pos >= len) {
            readBuffer();
        }

        return buffer[pos++];
    }

    public boolean hasNextLine() {
        return hasNextChar();
    }

    public boolean hasNextToken() {
        return hasNextChar();
    }

    public boolean lastInLine() throws IOException {
        if (end)
            return true;
        int prev = pos;
        char c = nextChar();
        while (hasNextChar() && !isWord(c) && c != '\n' && c != '\r') {
            c = nextChar();
        }
        if (c == '\n' || c == '\r') {
            //pos++;
            return true;
        } else {
            pos = prev;
            return false;
        }
    }

    private boolean isWord(char c) {
        return Character.isLetter(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'';
    }

    private boolean isNum(char c) {
        return c >= '0' && c <= '9' || c == '-' || Character.isLetter(c);
    }

    private boolean check(int i, char c) {
        if (i == 1) {
            return isWord(c);
        } else {
            return isNum(c);
        }
    }

    public String nextPart(int val) throws IOException {
        sb = new StringBuilder();
        char c = nextChar();
        if (c == '\n' || c == '\r') {
            pos++;
            return "";
        }
        while (hasNextChar() && !check(val, c)) {
            c = nextChar();
        }
        pos--;

        while (hasNextChar()) {
            c = nextChar();
            if (check(val, c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }


    public String nextWord() throws IOException {
        sb = new StringBuilder();
        char c = nextChar();

        while (hasNextChar() && !isWord(c)) {
            c = nextChar();
        }
        if (pos != 0)
            pos--;

        while (hasNextChar()) {
            c = nextChar();
            if (isWord(c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }
    public void close() throws IOException {
        br.close();
    }
}

   /* public String nextLine() throws IOException {
        sb = new StringBuilder();
        char c;
        while (hasNextChar()) {
            c = nextChar();
            if (c == '\n') {
                break;
            }
            if (c != '\r') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void close() throws IOException {
        br.close();
    }
} */

