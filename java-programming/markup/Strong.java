package markup;
import java.util.List;

public class Strong extends AbstractText implements Markdown{
    private String string;
    public Strong (List<Markdown> list) {
        super(list);
    }
    @Override
    public void toMarkdown (StringBuilder sb) {
        toMarkdown(sb, "__");
    }
    public void toBBCode (StringBuilder sb) {
        toBBCode(sb, "[b]", "[/b]");
    }
}