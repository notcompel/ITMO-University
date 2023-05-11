package markup;

import java.util.List;

public class Text implements Markdown {
    private String string;
    public Text (String string) {
        this.string = string;
    }
    public void toMarkdown (StringBuilder sb) {
        sb.append(string);
    }
    public void toBBCode (StringBuilder sb) {
        sb.append(string);
    }
}