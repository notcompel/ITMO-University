package game;

import java.util.*;

public class HexBoard extends AbstractBoard {
    Position position;
    public HexBoard(int n, int m, int k) {
        super(n, m, k);
        //TicTacToeBoard board = (TicTacToeBoard) position;
        position = new Position(this);
    }

    public boolean checkDraw() {
        return cells == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        int tmp = String.valueOf(m).length() + 1;
        sb.append(String.format("%" + tmp + "s", " "));
        for (int i = 1; i <= m; i++) {
            sb.append(String.format("%" + tmp + "d", i));
        }
        sb.append(System.lineSeparator());
        for (int r = 0; r < n; r++) {
            sb.append(" ".repeat(r));
            sb.append(String.format("%" + tmp + "d", r + 1));
            for (Cell cell : field[r]) {
                sb.append(String.format("%" + tmp + "s", CELL_TO_STRING.get(cell)));
            }
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }


    public int checkWin(int x, int y, int dx, int dy) {
        int count = 0;
        for (int i = 1; i < k; i++) {
            if (0 <= x + i * dx && x + i * dx < n &&
                    0 <= y + i * dy && y + i * dy < m &&
                    field[x + i * dx][y + i * dy] == turn) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!isValid(move)) {
            return GameResult.LOOSE;
        }

        field[move.getRow()][move.getCol()] = move.getValue();
        cells--;
        int x = move.getRow(), y = move.getCol();
        int row = checkWin(x, y, 0, 1) +
                checkWin(x, y, 0, -1) + 1;
        int col = checkWin(x, y, 1, 0) +
                checkWin(x, y, -1, 0) + 1;
        int diag = checkWin(x, y, 1, -1) +
                checkWin(x, y, -1, 1) + 1;

        if (row >= k || col >= k || diag >= k) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        position = new Position(this);
        return GameResult.UNKNOWN;
    }

}
/*public class HexBoard implements Board {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );

    private final Cell[][] field;
    final Position position;
    private Cell turn;
    private final int n;
    private final int m;
    private final int k;
    private int cells;


    public HexBoard(int n, int m, int k) {
        field = new Cell[n][m];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        this.n = n;
        this.m = m;
        this.k = k;
        this.cells = n * m;
        position = new NowPosition();
    }


    public Position getPosition() {
        return this.position;

    }
    class NowPosition implements Position{
        @Override
        public Cell getCell(int row, int column) {
            return field[row][column];
        }
        @Override
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
            return k;
        }
        public boolean isValid(final Move move) {
            return 0 <= move.getRow() && move.getRow() < n
                    && 0 <= move.getCol() && move.getCol() < m
                    && field[move.getRow()][move.getCol()] == Cell.E
                    && turn == move.getValue();
        }

    }


    public GameResult makeMove(Move move) {
        if (!position.isValid(move)) {
            return GameResult.LOOSE;
        }

        field[move.getRow()][move.getCol()] = move.getValue();
        cells--;

        if (checkWin(move.getRow(), move.getCol())) {
            return GameResult.WIN;
        }

        if (checkDraw()) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;

        return GameResult.UNKNOWN;
    }

    private boolean checkDraw() {
        return cells == 0;
    }

    private boolean checkWin(int x, int y) {
        int col = 0;
        int row = 0;
        int diag = 0;
        for (int i = -k; i <= k; i++) {
            if (0 <= x + i && x + i < n && field[x + i][y] == turn)
                row++;
            if (0 <= y + i && y + i < m && field[x][y + i] == turn)
                col++;

            if ( 0 <= y - i && y - i < m && 0 <= x + i && x + i < n && field[x + i][y - i] == turn)
                diag++;
        }
        return row >= k || col >= k || diag >= k;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        int tmp = String.valueOf(m).length() + 1;
        sb.append(String.format("%" + tmp + "s", " "));
        for (int i = 1; i <= m; i++) {
            sb.append(String.format("%" + tmp + "d", i));
        }
        sb.append(System.lineSeparator());
        for (int r = 0; r < n; r++) {
            sb.append(" ".repeat(r));
            sb.append(String.format("%" + tmp + "d", r + 1));
            for (Cell cell : field[r]) {
                sb.append(String.format("%" + tmp + "s", CELL_TO_STRING.get(cell)));
            }
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }

}

/*int col = 0;
        int row = 0;
        int diag = 0;
        for (int i = x + 1; i <= x + k && i < n && field[i][y] == turn; i++) {
            row++;
        }
        for (int i = x; i >= x - k && i >= 0 && field[i][y] == turn; i--) {
            row++;
        }
        if (row >= k) {
            return true;
        }

        for (int j = y + 1; j <= y + k && j < m && field[x][j] == turn; j++) {
            col++;
        }
        for (int j = y; j >= y - k && j >= 0 && field[x][j] == turn; j--) {
            col++;
        }

        if (col >= k) {
            return true;
        }

        for (int i = x, j = y; i < n && j < m && field[i][j] == turn; i++, j++) {
            diag++;
        }
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && field[i][j] == turn; i--, j--) {
            diag++;
        }
        if (diag >= k) {
            return true;
        }

        return false; */
