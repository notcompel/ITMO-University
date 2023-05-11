package markup;

import java.util.List;

public class OrderedList extends AbstractText implements ListM {
    public OrderedList (List<ListItem> list1) {
        super(list1);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {

    }

    @Override
    public void toBBCode(StringBuilder sb) {
        toBBCode(sb, "[list=1]", "[/list]");
    }
}
