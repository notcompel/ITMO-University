package game;

import java.util.Arrays;
import java.util.Map;

public abstract class AbstractBoard implements Board {
    protected static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );

    protected final Cell[][] field;
    protected Cell turn;
    protected final int n;
    protected final int m;
    protected final int k;
    protected int cells;


    public AbstractBoard(int n, int m, int k) {
        field = new Cell[n][m];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        this.n = n;
        this.m = m;
        this.k = k;
        this.cells = n * m;
    }
    public AbstractBoard(int n, int m, int k, final Cell[][] field, Cell turn, int cells){
        this.field = field;
        this.turn = turn;
        this.n = n;
        this.m = m;
        this.k = k;
        this.cells = cells;
    };

    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getCol() && move.getCol() < m
                && field[move.getRow()][move.getCol()] == Cell.E
                && turn == move.getValue();
    }

    public Cell getCell(int row, int column) {
        return field[row][column];
    }

    public Cell getTurn() {
        return turn;
    }
    public int getN() {
        return n;
    }
    public int getM() {
        return m;
    }

    public int getK() {
        return m;
    }
    public int getCells() {
        return cells;
    }
    public Cell[][] getField() {
        return field;
    }
    public abstract GameResult makeMove(Move move);
    public abstract Position getPosition();
}
