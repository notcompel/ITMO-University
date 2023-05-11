#include "bptree.h"

#include <iostream>
#include <string>

int main()
{
    BPTree<std::string, int> tree;
    std::string line;
    tree.insert("1", 1);
    tree.insert("2", 2);
    tree.insert("3", 3);
    tree.insert("4", 4);
    tree.erase(tree.find("3"), tree.end());
    std::cout << tree.size();
    for (const auto & [key, value] : tree) {
        std::cout << key << " => " << value << "\n";
    }
}