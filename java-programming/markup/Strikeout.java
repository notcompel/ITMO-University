package markup;
import java.util.List;

public class Strikeout extends AbstractText implements Markdown{
    private String string;
    public Strikeout (List<Markdown> list) {
        super(list);
    }

    @Override
    public void toMarkdown (StringBuilder sb) {
        toMarkdown(sb, "~");
    }
    public void toBBCode (StringBuilder sb) {
        toBBCode(sb, "[s]", "[/s]");
    }
}