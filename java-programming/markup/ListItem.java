package markup;
import java.util.List;

public class ListItem extends AbstractText{
    public ListItem (List<ListM> list) {
        super(list);
    }
    public void toMarkdown (StringBuilder sb) { }
    public void toBBCode (StringBuilder sb) {
        toBBCode(sb, "[*]", "");
    }
}
