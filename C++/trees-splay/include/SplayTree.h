#pragma once

#include <vector>

struct Node {
    int value;

    Node *left = nullptr;
    Node *right = nullptr;
    Node *parent = nullptr;

    Node(Node *left, Node *right, Node *parent, int value)
            : value(value), left(left), right(right), parent(parent) {};

    ~Node() {
        delete left;
        delete right;
    }
};

class SplayTree {
private:
    mutable Node *root = nullptr;
    std::size_t m_size = 0;

    Node *getParent(Node *node) const;

    void setValues(std::vector<int> &vector, Node *node) const;

    bool contains(Node *node, int value) const;

    Node *merge(Node *left, Node *right);

    std::pair<Node *, Node *> split(Node *tree, int value) const;

    void rotateLeft(Node *node) const;

    void rotateRight(Node *node) const;

    void splay(Node *&node) const;

    void splay(Node *&node, int value) const;

public:
    bool contains(int value) const;

    bool insert(int value);

    bool remove(int value);

    std::size_t size() const;

    bool empty() const;

    std::vector<int> values() const;

    ~SplayTree();
};
