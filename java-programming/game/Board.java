package game;

public interface Board {
    Position getPosition();
    int getN();
    int getM();
    int getK();
    int getCells();
    Cell getTurn();
    Cell[][] getField();
    GameResult makeMove(Move move);
}
