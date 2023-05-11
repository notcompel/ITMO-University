package game;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;

    public HumanPlayer(Scanner in) {
        this.in = in;
    }

    @Override
    public Move makeMove(Position position) {
        System.out.println();
        System.out.println("Current position");
        System.out.println(position);
        System.out.println("Enter you move for " + position.getTurn());
        while (true) {
            try {
                Move move = new Move(in.nextInt() - 1, in.nextInt() - 1, position.getTurn());
                if (position.isValid(move)) {
                    return move;
                } else {
                    System.out.println("Not a valid move, try again:");
                }
            } catch (Exception e) {
                System.out.println("Enter a correct move:");
                in.nextLine();
            }
        }
    }
}
