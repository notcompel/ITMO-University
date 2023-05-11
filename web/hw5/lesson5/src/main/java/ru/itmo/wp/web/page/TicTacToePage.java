package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    // TODO: Implement it.
    public class State {
        private String[][] cells;
        private int size;
        private String phase;
        private boolean crossesMove = true;
        public State() {
            size = 3;
            cells = new String[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    cells[i][j] = null;
                }
            }
            phase = "RUNNING";
        }
        public int getSize() {
            return size;
        }
        public String[][] getCells() {
            return cells;
        }

        public void move(int i0, int j0) {
            if (!phase.equals("RUNNING") || cells[i0][j0] != null) return;
            if (crossesMove) {
                cells[i0][j0] = "X";
            } else {
                cells[i0][j0] = "O";
            }

            int cnt = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (cells[i][j] != null) cnt++;
                }
            }
            if (cnt == 9) phase = "DRAW";

            //vertical
            cnt = 0;
            for (int j = 0; j < size; j++) {
                if (Objects.equals(cells[i0][j0], cells[i0][j])) {
                    cnt++;
                    if (cnt == size) {
                        phase = "WON_" + cells[i0][j0];
                        break;
                    }
                }
                else {
                    cnt = 0;
                }
            }

            //horizontal
            cnt = 0;
            for (int i = 0; i < size; i++) {
                if (Objects.equals(cells[i0][j0], cells[i][j0])) {
                    cnt++;
                    if (cnt == size) {
                        phase = "WON_" + cells[i0][j0];
                        break;
                    }
                }
                else {
                    cnt = 0;
                }
            }

            //diag1
            cnt = 0;
            for (int d = -size + 1; d < size; d++) {
                if (i0 + d >= 0 && i0 + d < size && j0 + d >= 0 && j0 + d < size
                        && Objects.equals(cells[i0][j0], cells[i0 + d][j0 + d])) {
                    cnt++;
                    if (cnt == size) {
                        phase = "WON_" + cells[i0][j0];
                        break;
                    }
                }
                else {
                    cnt = 0;
                }
            }

            //diag2
            cnt = 0;
            for (int d = -size + 1; d < size; d++) {
                if (i0 - d >= 0 && i0 - d < size && j0 + d >= 0 && j0 + d < size
                        && Objects.equals(cells[i0][j0], cells[i0 - d][j0 + d])) {
                    cnt++;
                    if (cnt == size) {
                        phase = "WON_" + cells[i0][j0];
                        break;
                    }
                }
                else {
                    cnt = 0;
                }
            }


            crossesMove = !crossesMove;
        }
        public String getPhase() {
            return phase;
        }
        public boolean getCrossesMove() {
            return crossesMove;
        }
    }

    private State state = new State();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        state = new State();
        view.put("state", state);
        request.getSession().setAttribute("state", state);
    }
    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        if (request.getSession().getAttribute("state") != null)
            state = (State) request.getSession().getAttribute("state");
        int i0 = 0, j0 = 0;
        for (int i = 0; i < state.size; i++) {
            for (int j = 0; j < state.size; j++) {
                if (request.getParameter("cell_" + i + j) != null) {
                    Object cell = request.getParameter("cell_" + i + j);
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }

        if (i0 < 3 && j0 < 3)
            state.move(i0, j0);
        view.put("state", state);
        request.getSession().setAttribute("state", state);
    }
    private void newGame(HttpServletRequest request, Map<String, Object> view) {
        state = new State();
        view.put("state", state);
        request.getSession().setAttribute("state", state);
    }
    public State getState() {
        return state;
    }
}