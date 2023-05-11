#include "SplayTree.h"

Node *SplayTree::getParent(Node *node) const {
    if (node == nullptr) {
        return nullptr;
    }
    return node->parent;
}

Node *SplayTree::merge(Node *left, Node *right) {
    if (left == nullptr) {
        return right;
    }

    while (left->right != nullptr) {
        left = left->right;
    }
    splay(left);

    left->right = right;
    if (right != nullptr) {
        right->parent = left;
    }
    return left;
}

std::pair<Node *, Node *> SplayTree::split(Node *tree, int value) const {
    if (tree == nullptr) {
        return {nullptr, nullptr};
    }
    splay(tree, value);
    if (tree->value > value) {
        Node *left = tree->left;
        tree->left = nullptr;
        return {left, tree};
    } else {
        Node *right = tree->right;
        tree->right = nullptr;
        return {tree, right};
    }
}


void SplayTree::splay(Node *&node) const {
    if (node != nullptr) {
        while (getParent(node) != nullptr) {
            if (node == getParent(node)->left) {
                if (getParent(getParent(node)) == nullptr) {
                    rotateRight(getParent(node));
                } else if (getParent(node) == getParent(getParent(node))->left) {
                    rotateRight(getParent(getParent(node)));
                    rotateRight(getParent(node));
                } else {
                    rotateRight(getParent(node));
                    rotateLeft(getParent(node));
                }
            } else {
                if (getParent(getParent(node)) == nullptr) {
                    rotateLeft(getParent(node));
                } else if (getParent(node) == getParent(getParent(node))->right) {
                    rotateLeft(getParent(getParent(node)));
                    rotateLeft(getParent(node));
                } else {
                    rotateLeft(getParent(node));
                    rotateRight(getParent(node));
                }
            }
        }
    }
}

void SplayTree::splay(Node *&node, int value) const {
    while (node != nullptr) {
        if (node->value > value && node->left != nullptr) {
            node = node->left;
        } else if (node->value < value && node->right != nullptr) {
            node = node->right;
        } else {
            break;
        }
    }
    splay(node);
}

bool SplayTree::insert(int value) {
    if (contains(value)) {
        return false;
    }
    auto[l, r] = split(root, value);
    root = new Node(l, r, nullptr, value);
    if (root->left != nullptr) {
        root->left->parent = root;
    }
    if (root->right != nullptr) {
        root->right->parent = root;
    }
    m_size++;
    return true;
}

bool SplayTree::remove(int value) {
    if (contains(value)) {
        splay(root, value);
        if (root != nullptr) {
            if (root->left != nullptr) {
                root->left->parent = nullptr;
            }
            if (root->right != nullptr) {
                root->right->parent = nullptr;
            }
            Node *left = root->left;
            Node *right = root->right;
            root->left = nullptr;
            root->right = nullptr;
            delete root;
            root = merge(left, right);
        }
        m_size--;
        return true;
    } else {
        return false;
    }
}

void SplayTree::rotateLeft(Node *node) const {
    if (node == nullptr) {
        return;
    }
    Node *p = getParent(node);
    Node *right = node->right;
    if (p != nullptr) {
        if (p->left == node) {
            p->left = right;
        } else {
            p->right = right;
        }
    }
    Node *tmp = right->left;
    right->left = node;
    node->right = tmp;
    node->parent = right;
    right->parent = p;
    if (node->right != nullptr) {
        node->right->parent = node;
    }
}

void SplayTree::rotateRight(Node *node) const {
    if (node == nullptr) {
        return;
    }
    Node *p = getParent(node);
    Node *left = node->left;
    if (p != nullptr) {
        if (p->right == node) {
            p->right = left;
        } else {
            p->left = left;
        }
    }
    Node *tmp = left->right;
    left->right = node;
    node->left = tmp;
    node->parent = left;
    left->parent = p;
    if (node->left != nullptr) {
        node->left->parent = node;
    }
}

std::size_t SplayTree::size() const {
    return m_size;
}

bool SplayTree::empty() const {
    return size() == 0;
}

void SplayTree::setValues(std::vector<int> &vector, Node *node) const {
    if (node == nullptr) {
        return;
    }
    setValues(vector, node->left);
    vector.push_back(node->value);
    setValues(vector, node->right);
}

std::vector<int> SplayTree::values() const {
    std::vector<int> result;
    setValues(result, root);
    return result;
}

bool SplayTree::contains(Node *node, int value) const {
    while (node != nullptr) {
        if (value < node->value) {
            node = node->left;
        } else if (value > node->value) {
            node = node->right;
        } else {
            return true;
        }
    }
    return false;
}

bool SplayTree::contains(int value) const {
    bool okContains = contains(root, value);
    if (okContains) {
        splay(root, value);
        return true;
    }
    return false;
}


SplayTree::~SplayTree() {
    delete root;
}
