package md2html;

import java.util.*;

public class TextParser {
    private StringBuilder sb;
    private int pos = 0;
    private boolean noFormat = false;
    private static Map<String, String> beginSymbols = Map.of(
            "*", "<em>",
            "_", "<em>",
            "**", "<strong>",
            "__", "<strong>",
            "--", "<s>",
            "`", "<code>",
            "```", "<pre>"
    );

    private static Map<String, String> endSymbols = Map.of(
            "*", "</em>",
            "_", "</em>",
            "**", "</strong>",
            "__", "</strong>",
            "--", "</s>",
            "`", "</code>",
            "```", "</pre>"
    );

    private final static Map<Character, String> htmlSymbols = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;"
    );

    TextParser(StringBuilder sb) {
        this.sb = sb;
    }

    private boolean isMarkdownSymbol(char c) {
        return c == '*' || c == '_' || c == '\'' || c == '-' || c == '`';
    }

    private boolean isHtmlSymbol(char c) {
        return c == '<' || c == '>' || c == '&';
    }

    public boolean toHtml(StringBuilder text, StringBuilder now, String lastMark) {
        if (lastMark.equals("```")) {
            noFormat = true;
        }
        System.out.println(noFormat);
        while (pos < sb.length()) {
            if (sb.charAt(pos) == '\\' && pos < sb.length() - 1 && isMarkdownSymbol(sb.charAt(pos + 1))) {
                now.append(sb.charAt(pos + 1));
                pos += 2;
                continue;
            }
            if (isMarkdownSymbol(sb.charAt(pos))) {
                String mark = getMark();
                System.out.println("IM HERE " + pos + mark);
                //первый случай: новый марк - продолжаем рекурсию
                if (!mark.equals(lastMark.toString())) {
                    StringBuilder tmp = new StringBuilder();
                    if (!noFormat && toHtml(text, tmp, mark) || pos == sb.length() - 1) {
                        now.append(beginSymbols.get(mark));
                    } else {
                        now.append(mark);
                    }
                    now.append(tmp);
                } else { //второй случай: встретили закрывающий тег - возвращаем рекурсию
                    now.append(endSymbols.get(mark));
                    noFormat = false;
                    return true;
                }
            } else if (isHtmlSymbol(sb.charAt(pos))) {
                now.append(htmlSymbols.get(sb.charAt(pos)));
                pos++;
            } else {
                now.append(sb.charAt(pos));
                pos++;
            }

        }
        return false;
    }

    private String getMark() {
        StringBuilder mark = new StringBuilder();
        mark.append(sb.charAt(pos));
        if (pos < sb.length() - 1 && isMarkdownSymbol(sb.charAt(pos + 1)) && sb.charAt(pos + 1) == sb.charAt(pos)) {
            if (sb.charAt(pos) != '`') {
                mark.append(sb.charAt(pos + 1));
                pos++;
            } else if (pos < sb.length() - 2 && sb.charAt(pos + 1) == '`' && sb.charAt(pos + 2) == '`'){
                mark.append(sb.charAt(pos + 1));
                mark.append(sb.charAt(pos + 2));
                pos += 2;
            }
        }
        pos++;
        return mark.toString();
    }
}




 /*for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\\' && i < sb.length() - 1 && isMarkdownSymbol(sb.charAt(i + 1))) {
                text.append(sb.charAt(i + 1));
                i++;
                continue;
            }
            if (isMarkdownSymbol(sb.charAt(i))) {
                System.out.println("char " + i + " " + sb.charAt(i));
                StringBuilder mark = new StringBuilder();
                mark.append(sb.charAt(i));
                if (i < sb.length() - 1 && isMarkdownSymbol(sb.charAt(i + 1))) {
                    mark.append(sb.charAt(i + 1));
                }
                String tmp = mark.toString();
                System.out.println("mark " + mark);
                if (inds.get(tmp) == -1) {
                    System.out.println("!!!");
                    inds.put(tmp, i);

                } else {
                    //System.out.println("!!!");
                    text.insert(inds.get(tmp), beginSymbols.get(tmp));
                    text.append(endSymbols.get(tmp));
                    inds.put(tmp, -1);
                }
                if (mark.length() == 2) {
                    i++;
                }
                continue;
            }
            text.append(sb.charAt(i));
        } */