package markup;
import java.util.List;

public class Emphasis extends AbstractText implements Markdown{
    private String string;
    public Emphasis (List<Markdown> list) {
        super(list);
    }
    @Override
    public void toMarkdown (StringBuilder sb) {
        toMarkdown(sb, "*");
    }
    public void toBBCode (StringBuilder sb) {
        toBBCode(sb, "[i]", "[/i]");
    }
}