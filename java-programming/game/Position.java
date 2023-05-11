package game;

public class Position extends AbstractBoard {

    public Position(int n, int m, int k) {
        super(n, m, k);
    }

    public Position(Board board) {
        super(board.getN(), board.getM(), board.getK(), board.getField(), board.getTurn(), board.getCells());
    }

    public GameResult makeMove(Move move) {
        return null;
    }

    @Override
    public Position getPosition() {
        return this;
    }

}
