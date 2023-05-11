#include "solver.h"

#include <algorithm>
#include <queue>
#include <set>
#include <unordered_map>

struct board_hash
{
    std::size_t operator()(const Board & value) const
    {
        return value.hash;
    }
};

std::vector<Board> reconstruct_path(std::unordered_map<Board, Board, board_hash> & came_from, Board & cur)
{
    std::vector<Board> path;
    path.push_back(cur);
    while (came_from.find(cur) != came_from.end()) {
        cur = came_from[cur];
        path.push_back(cur);
    }

    return path;
}

std::vector<Board> AStar(const Item & start, const Board & goal)
{
    auto cmp = [](const Item & a, const Item & b) {
        if (a.board.size() < 4) {
            return (a.board.manhattan() + a.depth) > (b.board.manhattan() + b.depth);
        }
        else {
            return ((a.board.size() / 2.0) * (a.board.manhattan()) + a.board.hamming() / (a.board.size() / 2.0) + a.depth / 1.15) > ((a.board.size() / 2.0) * (b.board.manhattan()) + b.board.hamming() / (a.board.size() / 2.0) + b.depth / 1.15);
        }
    };
    std::priority_queue<Item, std::vector<Item>, decltype(cmp)> open(cmp);
    std::unordered_map<Board, unsigned, board_hash> closed;
    std::unordered_map<Board, Board, board_hash> came_from;
    open.push(start);
    while (!open.empty()) {
        Item cur = open.top();

        if (cur.board == goal) {
            return reconstruct_path(came_from, cur.board);
        }
        open.pop();
        closed[cur.board] = 0;

        std::vector<Board> neighbours;
        auto [x, y] = cur.board.get_empty();
        auto tmp = cur.board.get_data();

        if (x < cur.board.size() - 1) {
            std::swap(tmp[x][y], tmp[x + 1][y]);
            Board b(tmp);
            neighbours.push_back(b);
            tmp = cur.board.get_data();
        }
        if (x > 0) {
            std::swap(tmp[x][y], tmp[x - 1][y]);
            Board b(tmp);
            neighbours.push_back(b);
            tmp = cur.board.get_data();
        }
        if (y < cur.board.size() - 1) {
            std::swap(tmp[x][y], tmp[x][y + 1]);
            Board b(tmp);
            neighbours.push_back(b);
            tmp = cur.board.get_data();
        }
        if (y > 0) {
            std::swap(tmp[x][y], tmp[x][y - 1]);
            Board b(tmp);
            neighbours.push_back(b);
        }

        for (const auto & neighb : neighbours) {
            unsigned tentative_gscore = cur.depth + 1;
            if (closed.count(neighb) == 0 || tentative_gscore <= closed[neighb]) {
                came_from[neighb] = cur.board;
                if (closed.count(neighb) == 0) {
                    open.push({tentative_gscore, neighb});
                }
                closed[neighb] = tentative_gscore;
            }
        }
    }
    return std::vector<Board>();
}

Solver::Solution Solver::solve(const Board & board)
{
    if (board.size() <= 1 || board.hamming() == 0) {
        return Solution({board});
    }
    if (!board.is_solvable()) {
        return Solution();
    }

    std::vector<Board> moves = AStar({0, board}, Board::create_goal(board.size()));
    reverse(moves.begin(), moves.end());
    return Solution{std::move(moves)};
}
