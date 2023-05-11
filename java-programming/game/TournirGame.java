package game;

import java.util.*;

public class TournirGame {
    List<Player> players;
    int[] goals;
    int n, m, k;

    public TournirGame(List<Player> players, int n, int m, int k) {
        this.players = players;
        this.n = n;
        this.m = m;
        this.k = k;
        goals = new int[players.size()];
    }

    public int[] play(boolean log) {
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size(); j++) {
                if (i == j) {
                    continue;
                }
                int result = new TwoPlayerGame(
                        new HexBoard(n, m, k),
                        players.get(i),
                        players.get(j)
                ).play(log);
                switch (result) {
                    case 1:
                        goals[i] += 3;
                        break;
                    case 2:
                        goals[j] += 3;
                        break;
                    case 0:
                        goals[i]++;
                        goals[j]++;
                        break;
                    default:
                        throw new AssertionError("Unknown result " + result);
                }

            }
        }
        return goals;
    }
}
