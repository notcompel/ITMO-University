package markup;

public interface Node {
    void toMarkdown(StringBuilder sb);
    void toBBCode(StringBuilder sb);
}
