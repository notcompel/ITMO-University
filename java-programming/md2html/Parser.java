package md2html;

public class Parser {
    private StringBuilder sb;
    private boolean isHeader = false;
    Parser(StringBuilder sb) {
        this.sb = sb;
    }

    private void makeType() {
        String s = sb.toString();
        int i = 0;
        while (s.charAt(i) == '#' && i < s.length()) {
            i++;
        }
        if (i < s.length() && s.charAt(i) == ' ' && i > 0) {
            isHeader = true;
        }
    }

    public void toHtml(StringBuilder text) {
        makeType();
        if (isHeader) {
            String s = sb.toString();
            int i = 0;
            int level = 0;
            while (i < s.length() && s.charAt(i) == '#') {
                i++;
                level++;
            }
            text.append("<h");
            text.append(level);
            text.append(">");
            new TextParser(new StringBuilder(s.substring(level + 1))).toHtml(text, text, "");
            text.append("</h");
            text.append(level);
            text.append(">");
        } else {
            text.append("<p>");
            new TextParser(sb).toHtml(text, text, "");
            text.append("</p>");
        }
    }

}
