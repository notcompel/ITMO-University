#include "board.h"

#include <algorithm>
#include <iostream>
#include <random>
#include <set>

Board Board::create_goal(const unsigned size)
{
    std::vector<std::vector<unsigned>> table(size, std::vector<unsigned>(size));
    for (std::size_t i = 0; i < size; i++) {
        for (std::size_t j = 0; j < size; j++) {
            table[i][j] = size * i + j + 1;
        }
    }
    if (size != 0)
        table[size - 1][size - 1] = 0;
    return Board(std::move(table));
}

Board Board::create_random(const unsigned size)
{
    if (size == 0) {
        return Board(std::vector<std::vector<unsigned>>{});
    }
    std::vector<unsigned> tmp(size * size);
    for (std::size_t i = 0; i < size * size; i++) {
        tmp[i] = i + 1;
    }
    tmp[size * size - 1] = 0;
    std::shuffle(tmp.begin(), tmp.end(), std::mt19937(std::random_device()()));
    std::vector<std::vector<unsigned>> table(size, std::vector<unsigned>(size));
    for (std::size_t i = 0; i < size; i++) {
        for (std::size_t j = 0; j < size; j++) {
            table[i][j] = tmp[size * i + j];
        }
    }
    return Board(std::move(table));
}

unsigned calculate_hamming(const std::vector<std::vector<unsigned>> & data)
{
    std::size_t count = 0;
    for (std::size_t i = 0; i < data.size(); i++) {
        for (std::size_t j = 0; j < data[i].size(); j++) {
            if (i == data.size() - 1 && j == data.size() - 1)
                continue;
            if (data[i][j] != data.size() * i + j + 1) {
                count++;
            }
        }
    }
    if (!data.empty() && data[data.size() - 1][data.size() - 1] != 0) {
        count++;
    }
    return count;
}

unsigned calculate_hash(const std::vector<std::vector<unsigned>> & data)
{
    std::string str;
    str.reserve(data.size() * data.size() * 4);
    for (std::size_t i = 0; i < data.size(); i++) {
        for (std::size_t j = 0; j < data[i].size(); j++) {
            str += std::to_string(data[i][j]);
            str += " ";
        }
    }
    return std::hash<std::string>()(str);
}

unsigned calculate_linear_conflicts(const std::vector<std::vector<unsigned>> & data)
{
    std::size_t count = 0;
    std::set<unsigned> was;
    std::vector<std::pair<unsigned, unsigned>> conflicts;
    std::vector<unsigned> tmp;
    for (std::size_t i = 0; i < data.size(); i++) {
        conflicts.clear();
        for (std::size_t j = 0; j < data[i].size(); j++) {
            if (data[i][j] == 0)
                continue;
            if (data[i][j] != data.size() * i + j + 1 && data.size() * i < data[i][j] && data[i][j] <= data.size() * (i + 1)) {
                conflicts.push_back({data[i][j], (data[i][j] - 1) % data.size()});
            }
        }
        for (std::size_t j = 0; j < conflicts.size(); j++) {
            if (was.count(conflicts[j].first))
                continue;
            for (std::size_t k = j + 1; k < conflicts.size(); k++) {
                if (was.count(conflicts[k].first))
                    continue;
                if (conflicts[j].second > conflicts[k].second) {
                    was.insert(conflicts[j].first);
                    was.insert(conflicts[k].first);
                    tmp.push_back(conflicts[j].first);
                    tmp.push_back(conflicts[k].first);
                    count += 2;
                }
            }
        }
    }
    was.clear();

    for (std::size_t j = 0; j < data.size(); j++) {
        conflicts.clear();
        for (std::size_t i = 0; i < data.size(); i++) {
            if (data[i][j] == 0)
                continue;
            if (data[i][j] != data.size() * i + j + 1 && (data[i][j] - 1) % data.size() == j) {
                conflicts.push_back({data[i][j], i});
            }
        }
        for (std::size_t i = 0; i < conflicts.size(); i++) {
            if (was.count(conflicts[i].first))
                continue;
            for (std::size_t k = i + 1; k < conflicts.size(); k++) {
                if (was.count(conflicts[k].first))
                    continue;
                if (conflicts[i].second > conflicts[k].second) {
                    tmp.push_back(conflicts[i].first);
                    tmp.push_back(conflicts[k].first);
                    was.insert(conflicts[i].first);
                    was.insert(conflicts[k].first);
                    count += 2;
                }
            }
        }
    }
    return count;
}

unsigned calculate_manhattan(const std::vector<std::vector<unsigned>> & data)
{
    unsigned result = 0;
    for (std::size_t i = 0; i < data.size(); i++) {
        for (std::size_t j = 0; j < data[i].size(); j++) {
            if (data[i][j] == 0)
                continue;
            unsigned x_goal = (data[i][j] - 1) / data.size();
            unsigned y_goal = data[i][j] - data.size() * x_goal - 1;
            result += std::abs(static_cast<int>(x_goal - i)) + std::abs(static_cast<int>(y_goal - j));
        }
    }
    return result;
}

Board::Board(const std::vector<std::vector<unsigned>> & table)
    : data(table)
    , manhatann_number(calculate_manhattan(table))
    , hamming_number(calculate_hamming(table))
    //, linear_conflicts_number(calculate_linear_conflicts(table))
    , hash(calculate_hash(table))
{
    unsigned k = 0, l = 0;
    for (unsigned i = 0; i < data.size(); i++) {
        for (unsigned j = 0; j < data.size(); j++) {
            if (data[i][j] == 0) {
                k = i, l = j;
                break;
            }
        }
    }
    empty_cell = {k, l};
}

Board::Board(std::vector<std::vector<unsigned>> && table)
    : data(std::move(table))
    , manhatann_number(calculate_manhattan(data))
    , hamming_number(calculate_hamming(data))
    , linear_conflicts_number(calculate_linear_conflicts(data))
    , hash(calculate_hash(data))
{
    unsigned k = 0, l = 0;
    for (unsigned i = 0; i < data.size(); i++) {
        for (unsigned j = 0; j < data.size(); j++) {
            if (data[i][j] == 0) {
                k = i, l = j;
                break;
            }
        }
    }
    empty_cell = {k, l};
}

std::size_t Board::size() const
{
    return data.size();
}

bool Board::is_goal() const
{
    return hamming() == 0;
}

unsigned Board::hamming() const
{
    return hamming_number;
}

unsigned Board::linear_conflicts() const
{
    return linear_conflicts_number;
}

unsigned Board::manhattan() const
{
    return manhatann_number;
}

std::string Board::to_string() const
{
    std::string str;
    str.reserve(data.size() * data.size() * 4);
    for (std::size_t i = 0; i < data.size(); i++) {
        for (std::size_t j = 0; j < data[i].size(); j++) {
            str += std::to_string(data[i][j]);
            str += " ";
        }
    }
    return str;
}

bool Board::is_solvable() const
{
    unsigned sum = 0;
    unsigned y = 0;
    for (std::size_t i = 0; i < data.size(); i++) {
        for (std::size_t j = 0; j < data[i].size(); j++) {
            if (data[i][j] == 0) {
                y = i;
                continue;
            }
            unsigned n = 0;
            for (std::size_t k = i; k < data.size(); k++) {
                for (std::size_t l = 0; l < data[k].size(); l++) {
                    if (k == i && l <= j)
                        continue;
                    if (data[k][l] && data[k][l] < data[i][j]) {
                        n++;
                    }
                }
            }
            sum += n;
        }
    }

    if (size() != 0 && size() % 2 == 0)
        sum += y + 1;
    return sum % 2 == 0;
}
