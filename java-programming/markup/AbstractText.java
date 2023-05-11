package markup;
import java.util.List;

public abstract class AbstractText implements Node{
    protected List<? extends Node> list;
    protected String string;

    public AbstractText(List<? extends Node> list) {
        this.list = list;
    }

    public void toMarkdown(StringBuilder sb, String mark) {
        sb.append(mark);
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).toMarkdown(sb);
        }
        sb.append(mark);
    }

    public void toBBCode(StringBuilder sb, String mark1, String mark2) {
        sb.append(mark1);
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).toBBCode(sb);
        }
        sb.append(mark2);
    }
}
