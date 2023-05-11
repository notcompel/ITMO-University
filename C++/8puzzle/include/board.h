#pragma once

#include <cmath>
#include <string>
#include <vector>

class Board
{
    std::vector<std::vector<unsigned>> data = {};
    unsigned manhatann_number = 0;
    unsigned hamming_number = 0;
    unsigned linear_conflicts_number = 0;
    std::pair<unsigned, unsigned> empty_cell = {0, 0};

public:
    unsigned hash = 0;

    const std::vector<unsigned> & operator[](unsigned i) const
    {
        return data[i];
    }

    static Board create_goal(unsigned size);

    static Board create_random(unsigned size);

    Board() = default;

    Board(std::vector<std::vector<unsigned>> && data);

    explicit Board(const std::vector<std::vector<unsigned>> & data);

    std::size_t size() const;

    bool is_goal() const;

    unsigned hamming() const;

    unsigned manhattan() const;

    unsigned linear_conflicts() const;

    const std::vector<std::vector<unsigned>> & get_data() const
    {
        return data;
    }
    const std::pair<unsigned, unsigned> & get_empty() const
    {
        return empty_cell;
    }

    std::string to_string() const;

    bool is_solvable() const;

    friend bool operator<(const Board & lhs, const Board & rhs)
    {
        return lhs.manhattan() < rhs.manhattan();
    }

    friend bool operator==(const Board & lhs, const Board & rhs)
    {
        return lhs.empty_cell == rhs.empty_cell && lhs.data == rhs.data;
    }

    friend bool operator!=(const Board & lhs, const Board & rhs)
    {
        return !(lhs == rhs);
    }

    friend std::ostream & operator<<(std::ostream & out, const Board & board)
    {
        return out << board.to_string();
    }
};
