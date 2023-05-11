package markup;

import java.util.List;

public class UnorderedList extends AbstractText implements ListM {
    public UnorderedList (List<ListItem> list1) {
        super(list1);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {

    }

    @Override
    public void toBBCode(StringBuilder sb) {
        toBBCode(sb, "[list]", "[/list]");
    }
}
