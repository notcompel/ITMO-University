package game;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int n = -1, m = -1, k = -1, p = 0;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter three integer numbers \"n m k\", which > 0 and < 1000000000:");
        while (true) {
            try {
                n = in.nextInt();
                m = in.nextInt();
                k = in.nextInt();
                if (n > 0 && m > 0 && k > 0) {
                    break;
                } else {
                    System.out.println("Enter correct n, m, k, which > 0:");
                }
            } catch (Exception e) {
                System.out.println("Enter correct n, m, k:");
                in.nextLine();
            }
        }

        System.out.println("Enter integer number of players, which >= 2:");
        while (true) {
            try {
                p = in.nextInt();
                if (p > 1) {
                    break;
                } else {
                    System.out.println("Enter correct p >= 2:");
                }
            } catch (Exception e) {
                System.out.println("Enter correct number of players:");
                in.nextLine();
            }
        }
        List<Player> players = new ArrayList<Player>();
        System.out.println("Enter types of players (one symbol): (h - human/s - sequential/r - random)");
        for (int i = 0; i < p; i++) {
            String s = in.next();
            if (s.equals("h")) {
                players.add(new HumanPlayer(in));
            } else if (s.equals("s")) {
                players.add(new SequentialPlayer());
            } else if (s.equals("r")) {
                players.add(new RandomPlayer());
            } else {
                System.out.println("Enter correct type of player:");
                i--;
            }
        }
        System.out.println("GAME IS STARTING!");
        int[] result = new TournirGame(
                players, n, m, k
        ).play(true);
        for (int i = 0; i < p; i++) {
            System.out.println("Player №" + i  + " - " + result[i] + " goals");
        }

    }
}


/*final int result = new TwoPlayerGame(
                new TicTacToeBoard(n, m, k),
                new RandomPlayer(),
                //new RandomPlayer()
                //new CheatingPlayer()
                //new HumanPlayer(new Scanner(System.in)),
                new HumanPlayer(new Scanner(System.in))
        ).play(true);
        switch (result) {
            case 1:
                System.out.println("First player won");
                break;
            case 2:
                System.out.println("Second player won");
                break;
            case 0:
                System.out.println("Draw");
                break;
            default:
                throw new AssertionError("Unknown result " + result);
        } */