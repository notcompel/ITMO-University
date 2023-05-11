package markup;

import java.util.List;

public class Paragraph extends  AbstractText implements ListM {
    private String string;
    public Paragraph (List<Markdown> list) {
        super(list);
    }
    public void toMarkdown (StringBuilder sb) {
        toMarkdown(sb, "");
    }
    public void toBBCode (StringBuilder sb) {
        toBBCode(sb, "", "");
    }
}