#pragma once

#include <algorithm>
#include <iostream>
#include <iterator>
#include <type_traits>
#include <utility>
#include <vector>

namespace tree_details {

template <class T>
constexpr bool IsConst = false;
template <template <bool> class T>
constexpr bool IsConst<T<true>> = true;

} // namespace tree_details

template <class Key, class Value, std::size_t BlockSize = 4096, class Less = std::less<Key>>
class BPTree
{
public:
    template <bool is_const>
    class Iterator;

    using key_type = Key;
    using mapped_type = Value;
    using value_type = std::pair<key_type, mapped_type>; // NB: a digression from std::map
    using reference = value_type &;
    using const_reference = const value_type &;
    using pointer = value_type *;
    using const_pointer = const value_type *;
    using size_type = std::size_t;

    static const size_type size_of_block = ((BlockSize - sizeof(void *)) / (sizeof(key_type) + sizeof(void *)));
    static const size_type min_size = (size_of_block + 1) / 2;

private:
    static constexpr Less cmp_keys{};
    class Internal;
    class Leaf;
    class Node
    {
        friend class Internal;

    protected:
        Internal * parent = nullptr;
        size_type size_of_node = 0;

    public:
        virtual ~Node() = default;
        virtual bool is_leaf() const = 0;
        virtual key_type get_key(size_type ind) = 0;
        virtual std::pair<size_type, bool> find_in_block(key_type key) = 0;
        virtual Leaf * find_leaf(const key_type & key) = 0;
        virtual Leaf * left() = 0;
        size_type & size()
        {
            return size_of_node;
        }
    };
    class Internal : public Node
    {
        friend class Leaf;
        std::array<Node *, size_of_block + 2> child;
        std::array<key_type, size_of_block + 1> keys;

    public:
        Internal()
        {
            child.fill(nullptr);
        }

        ~Internal() override
        {
            for (size_type i = 0; i < child.size(); i++) {
                delete child[i];
            }
        }
        bool is_leaf() const override
        {
            return false;
        }

        key_type get_key(size_type ind) override
        {
            return keys[ind];
        }

        Leaf * find_leaf(const key_type & key) override
        {
            if (cmp_keys(key, keys[0])) {
                return child[0]->find_leaf(key);
            }
            for (size_type i = 0; i < Node::size(); ++i) {
                if (cmp_keys(key, keys[i])) {
                    return child[i]->find_leaf(key);
                }
            }
            return child[Node::size()]->find_leaf(key);
        }

        std::pair<size_type, bool> find_in_block(key_type key) override
        {
            auto it = std::lower_bound(keys.begin(), keys.begin() + Node::size(), key, cmp_keys);
            size_type ind = it - keys.begin();
            if (it >= keys.begin() + Node::size()) {
                return {Node::size(), false};
            }
            if (!cmp_keys(keys[ind], key) && !cmp_keys(key, keys[ind])) {
                return {ind, true};
            }
            else if (!cmp_keys(keys[ind], key)) {
                return {ind, false};
            }

            return {Node::size(), false};
        }

        static bool balance(Internal * left_node, Internal * right_node, Internal * cur, size_type ind)
        {
            if (left_node != nullptr && left_node->size() >= min_size) {
                cur->parent->keys[ind - 1] = left_node->keys[left_node->size() - 1];
                cur->insert_cell(0, cur->parent->keys[ind - 1], left_node->child[left_node->size()]);
                left_node->erase_cell(left_node->size() - 1);
                return true;
            }
            if (right_node != nullptr && right_node->size() >= min_size) {
                cur->parent->keys[ind] = right_node->keys[0];
                cur->insert_cell(cur->size(), cur->parent->keys[ind], right_node->child[0]);
                right_node->erase_cell(0);
                return true;
            }
            return false;
        }

        void erase_cell(size_type ind)
        {
            std::copy(keys.begin() + ind + 1, keys.begin() + Node::size(), keys.begin() + ind);
            std::copy(child.begin() + ind + 2, child.begin() + Node::size() + 1, child.begin() + ind + 1);
            child[Node::size()] = nullptr;
            Node::size()--;
        }

        void insert_cell(size_type ind, const key_type & key, Node * ch)
        {
            std::copy_backward(keys.begin() + ind, keys.begin() + Node::size(), keys.begin() + Node::size() + 1);
            std::copy_backward(child.begin() + ind + 1, child.begin() + Node::size() + 1, child.begin() + Node::size() + 2);
            keys[ind] = key;
            child[ind + 1] = ch;
            child[ind + 1]->parent = this;
            Node::size()++;
        }

        key_type split(Internal * internal, const key_type & key, Node * ch)
        {
            insert_cell(find_in_block(key).first, key, ch);
            key_type middle = keys[min_size];
            Node::size() = min_size;
            internal->size() = internal->keys.size() - 1 - Node::size();
            internal->parent = Node::parent;

            for (size_type i = 0, j = Node::size() + 1; i < internal->size() + 1; i++, j++) {
                if (i < internal->size())
                    internal->keys[i] = keys[j];
                internal->child[i] = child[j];
                child[j] = nullptr;
                internal->child[i]->parent = internal;
            }
            return middle;
        }

        void merge(Internal * right, size_type ind)
        {
            keys[Node::size()] = Node::parent->keys[ind - 1];
            for (size_type i = Node::size() + 1, j = 0; j < right->size(); j++, i++) {
                keys[i] = right->keys[j];
            }
            for (size_type i = Node::size() + 1, j = 0; j < right->size() + 1; j++, i++) {
                child[i] = right->child[j];
                child[i]->parent = this;
                right->child[j] = nullptr;
            }
            Node::size() += right->size() + 1;
            right->size() = 0;
        }

        Leaf * left() override
        {
            return child[0]->left();
        }
        Internal * insert_internal(const key_type & key, Node * ch)
        {
            if (Node::size() < size_of_block) {
                size_type i = this->find_in_block(key).first;
                insert_cell(i, key, ch);
                return nullptr;
            }

            Internal * internal = new Internal();
            key_type middle = split(internal, key, ch);
            if (Node::parent == nullptr) {
                Internal * new_root = new Internal();
                new_root->insert_cell(0, middle, internal);
                Node::parent = new_root;
                new_root->child[0] = this;
                return new_root;
            }
            else {
                return Node::parent->insert_internal(middle, internal);
            }
        }

        Node * erase_internal(const key_type & key, Node * ch)
        {
            if (Node::parent == nullptr && Node::size() == 1) {
                Node * tmp = nullptr;
                if (child[1] == ch) {
                    tmp = child[0];
                }
                else if (child[0] == ch) {
                    tmp = child[1];
                }
                tmp->parent = nullptr;
                delete ch;
                child[0] = nullptr;
                child[1] = nullptr;
                delete this;
                return tmp;
            }
            auto [ind, found] = this->find_in_block(key);
            erase_cell(ind);

            if (Node::size() >= min_size - 1 || Node::parent == nullptr) {
                delete ch;
                return nullptr;
            }

            Internal * left_node = nullptr;
            Internal * right_node = nullptr;
            for (ind = 0; ind < Node::parent->size() + 1; ind++) {
                if (Node::parent->child[ind] == this) {
                    if (ind > 0)
                        left_node = dynamic_cast<Internal *>(Node::parent->child[ind - 1]);
                    if (ind < Node::parent->size())
                        right_node = dynamic_cast<Internal *>(Node::parent->child[ind + 1]);
                    break;
                }
            }

            if (Internal::balance(left_node, right_node, this, ind)) {
                // DO NOTHING
            }
            else if (left_node != nullptr) {
                left_node->merge(this, ind);
                delete ch;
                return Node::parent->erase_internal(Node::parent->keys[ind - 1], this);
            }
            else if (right_node != nullptr) {
                this->merge(right_node, ind + 1);
                delete ch;
                return Node::parent->erase_internal(Node::parent->keys[ind], right_node);
            }
            delete ch;
            return nullptr;
        }
    };

    class Leaf : public Node
    {
        std::array<value_type, size_of_block + 1> data;
        Leaf * next = nullptr;

    public:
        bool is_leaf() const override
        {
            return true;
        }

        key_type get_key(size_type ind) override
        {
            return data[ind].first;
        }
        value_type & get_value(size_type ind)
        {
            return data[ind];
        }
        Leaf *& get_next()
        {
            return next;
        }
        Leaf * find_leaf(const key_type &) override
        {
            return this;
        }
        std::pair<size_type, bool> find_in_block(key_type key) override
        {
            auto cmp = [](const value_type & a, const value_type & b) { return cmp_keys(a.first, b.first); };
            auto it = std::lower_bound(data.begin(), data.begin() + Node::size(), value_type{key, mapped_type{}}, cmp);
            if (it >= data.begin() + Node::size()) {
                return {Node::size(), false};
            }
            size_type ind = it - data.begin();
            if (!cmp_keys(data[ind].first, key) && !cmp_keys(key, data[ind].first)) {
                return {ind, true};
            }
            else if (!cmp_keys(data[ind].first, key)) {
                return {ind, false};
            }

            return {Node::size(), false};
        }

        static bool balance_leaves(Leaf * left_node, Leaf * right_node, Leaf * cur, size_type ind)
        {
            if (left_node != nullptr && left_node->size() >= min_size + 1) {
                cur->insert_cell(0, left_node->data[left_node->size() - 1].first, std::move(left_node->data[left_node->size() - 1].second));
                left_node->erase_cell(left_node->size() - 1);
                cur->parent->keys[ind - 1] = cur->data[0].first;
                return true;
            }
            if (right_node != nullptr && right_node->size() >= min_size + 1) {
                cur->insert_cell(cur->size(), right_node->data[0].first, std::move(right_node->data[0].second));
                right_node->erase_cell(0);
                cur->parent->keys[ind] = right_node->data[0].first;
                return true;
            }
            return false;
        }

        void erase_cell(size_type ind)
        {
            std::move(data.begin() + ind + 1, data.begin() + Node::size(), data.begin() + ind);
            Node::size()--;
        }
        void insert_cell(size_type ind, const key_type & key, mapped_type && value)
        {
            std::move_backward(data.begin() + ind, data.begin() + Node::size(), data.begin() + Node::size() + 1);
            data[ind] = {key, std::move(value)};
            Node::size()++;
        }
        key_type split_leaf(Leaf * leaf, const key_type & key, mapped_type && value)
        {
            insert_cell(find_in_block(key).first, key, std::move(value));
            Node::size() = min_size;
            leaf->size() = leaf->data.size() - Node::size();
            Leaf * p = next;
            next = leaf;
            leaf->next = p;
            leaf->parent = Node::parent;
            std::move(data.begin() + Node::size(), data.begin() + Node::size() + leaf->size(), leaf->data.begin());
            return leaf->data[0].first;
        }

        Node * insert(const key_type & key, mapped_type && value)
        {
            if (Node::size() < size_of_block) {
                size_type i = this->find_in_block(key).first;
                this->insert_cell(i, key, std::move(value));
                return nullptr;
            }

            Leaf * leaf = new Leaf();
            key_type middle = this->split_leaf(leaf, key, std::move(value));
            if (Node::parent == nullptr) {
                Internal * new_root = new Internal();
                new_root->insert_cell(0, middle, leaf);
                Node::parent = new_root;
                new_root->child[0] = this;
                return new_root;
            }
            return Node::parent->insert_internal(middle, leaf);
        }

        void merge(Leaf * right)
        {
            std::move(right->data.begin(), right->data.begin() + right->size(), data.begin() + Node::size());
            Node::size() += right->size();
            next = right->next;
        }

        Leaf * left() override
        {
            return this;
        }

        Leaf * get_next_cell(size_type & i)
        {
            if (i < Node::size() - 1) {
                ++i;
                return this;
            }
            else {
                i = 0;
                return next;
            }
        }

        Node * erase(key_type key)
        {
            auto [ind, found] = this->find_in_block(key);
            this->erase_cell(ind);
            if (Node::parent == nullptr || Node::size() >= min_size) {
                return nullptr;
            }

            Leaf * left_node = nullptr;
            Leaf * right_node = nullptr;
            for (ind = 0; ind < Node::parent->size() + 1; ind++) {
                if (Node::parent->child[ind] == this) {
                    if (ind > 0)
                        left_node = dynamic_cast<Leaf *>(Node::parent->child[ind - 1]);
                    if (ind < Node::parent->size())
                        right_node = dynamic_cast<Leaf *>(Node::parent->child[ind + 1]);
                    break;
                }
            }

            if (Leaf::balance_leaves(left_node, right_node, this, ind)) {
                return nullptr;
            }

            if (left_node != nullptr) {
                left_node->merge(this);
                return Node::parent->erase_internal(Node::parent->keys[ind - 1], this);
            }
            else {
                this->merge(right_node);
                return Node::parent->erase_internal(Node::parent->keys[ind], right_node);
            }
        }
    };

    Node * m_root = nullptr;
    size_type m_size = 0;

    Leaf * left() const
    {
        if (m_root == nullptr) {
            return nullptr;
        }
        return m_root->left();
    }

public:
    template <bool is_const>
    class Iterator;

    using iterator = Iterator<false>;
    using const_iterator = Iterator<true>;

    BPTree() = default;

    ~BPTree()
    {
        delete m_root;
    }

    BPTree(std::initializer_list<std::pair<key_type, mapped_type>> list)
    {
        for (auto it : list) {
            insert(it.first, it.second);
        }
    }

    BPTree(const BPTree & other)
    {
        for (const_iterator it = other.begin(); it != other.cend(); it++) {
            insert(it->first, it->second);
        }
    }

    BPTree(BPTree && other)
        : m_size(other.m_size)
    {
        std::swap(m_root, other.m_root);
        other.m_size = 0;
    }

    BPTree & operator=(const BPTree & other)
    {
        auto tmp = other;
        swap(tmp);
        return *this;
    }

    BPTree & operator=(BPTree && other)
    {
        swap(other);
        return *this;
    }

    void swap(BPTree & other)
    {
        std::swap(m_root, other.m_root);
        std::swap(m_size, other.m_size);
    }

    iterator begin()
    {
        return {0, left(), *this};
    }

    const_iterator cbegin() const
    {
        return {0, left(), *this};
    }

    const_iterator begin() const
    {
        return cbegin();
    }

    iterator end()
    {
        return {0, nullptr, *this};
    }
    const_iterator cend() const
    {
        return {0, nullptr, *this};
    }
    const_iterator end() const
    {
        return cend();
    }

    bool empty() const;
    size_type size() const;
    void clear();

    size_type count(const key_type &) const;
    bool contains(const key_type &) const;
    std::pair<iterator, iterator> equal_range(const key_type &);
    std::pair<const_iterator, const_iterator> equal_range(const key_type &) const;
    iterator lower_bound(const key_type &);
    const_iterator lower_bound(const key_type &) const;
    iterator upper_bound(const key_type &);
    const_iterator upper_bound(const key_type &) const;
    iterator find(const key_type & key);
    const_iterator find(const key_type & key) const;

    // 'at' method throws std::out_of_range if there is no such key
    mapped_type & at(const key_type &);
    const mapped_type & at(const key_type &) const;

    // '[]' operator inserts a new element if there is no such key
    mapped_type & operator[](const key_type &);

    std::pair<iterator, bool> insert(const key_type &, const mapped_type &); // NB: a digression from std::map
    std::pair<iterator, bool> insert(const key_type &, mapped_type &&);      // NB: a digression from std::map
    template <class ForwardIt>
    void insert(ForwardIt begin, ForwardIt end);
    void insert(std::initializer_list<value_type>);
    iterator erase(const_iterator);
    iterator erase(const_iterator, const_iterator);
    size_type erase(const key_type &);
};

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
template <bool is_const>
class BPTree<key_type, mapped_type, BlockSize, Less>::Iterator
{
public:
    using difference_type = std::ptrdiff_t;
    using value_type = std::conditional_t<is_const, const value_type, value_type>;
    using pointer = value_type *;
    using reference = value_type &;
    using iterator_category = std::forward_iterator_tag;

    Iterator() = default;

    template <class R = Iterator, std::enable_if_t<tree_details::IsConst<R>, int> = 0>
    Iterator(const Iterator<false> & other)
        : m_index(other.m_index)
        , m_current_node(other.m_current_node)
        , m_tree(other.m_tree)
    {
    }

    reference operator*() const
    {
        return m_current_node->get_value(m_index);
    }

    pointer operator->() const
    {
        return &(m_current_node->get_value(m_index));
    }

    friend bool operator==(const Iterator & lhs, const Iterator & rhs)
    {
        return lhs.m_tree == rhs.m_tree && lhs.m_current_node == rhs.m_current_node && lhs.m_index == rhs.m_index;
    }
    friend bool operator!=(const Iterator & lhs, const Iterator & rhs)
    {
        return !(lhs == rhs);
    }

    Iterator & operator++()
    {
        m_current_node = m_current_node->get_next_cell(m_index);
        return *this;
    }

    Iterator operator++(int)
    {
        auto tmp = *this;
        operator++();
        return tmp;
    }

private:
    friend class BPTree;

    Iterator(std::size_t index, Leaf * node, const BPTree & tree)
        : m_index(index)
        , m_current_node(node)
        , m_tree(&tree)
    {
    }

    std::size_t m_index = 0;
    Leaf * m_current_node = nullptr;
    const BPTree * m_tree = nullptr;
};

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::size() const -> size_type
{
    return m_size;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
bool BPTree<key_type, mapped_type, BlockSize, Less>::empty() const
{
    return size() == 0;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::find(const key_type & key) -> iterator
{
    if (m_root == nullptr) {
        return end();
    }
    Leaf * leaf = m_root->find_leaf(key);
    auto [ind, found] = leaf->find_in_block(key);
    return found ? iterator{ind, leaf, *this} : end();
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::find(const key_type & key) const -> const_iterator
{
    if (m_root == nullptr) {
        return cend();
    }
    Leaf * leaf = m_root->find_leaf(key);
    auto [ind, found] = leaf->find_in_block(key);
    return found ? iterator{ind, leaf, *this} : cend();
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::erase(const_iterator begin, const_iterator end) -> iterator
{
    iterator it(begin.m_index, begin.m_current_node, *this);
    key_type end_key{};
    if (end != cend())
        end_key = end->first;
    while ((it != cend() && end == cend()) || (end != cend() && cmp_keys(it->first, end_key))) {
        it = erase(it);
    }
    return it;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::erase(const key_type & key) -> size_type
{
    const_iterator it = find(key);
    if (it != cend()) {
        erase(it);
        return 1;
    }
    return 0;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::erase(const_iterator it) -> iterator
{
    if (m_root == nullptr) {
        return end();
    }
    Leaf * cur = it.m_current_node;
    key_type key = it->first;
    auto [ind, found] = cur->find_in_block(key);
    if (!found) {
        return end();
    }

    m_size--;
    if (cur == m_root && m_size == 0) {
        delete m_root;
        m_root = nullptr;
        return end();
    }
    it++;
    if (it == cend()) {
        auto new_root = cur->erase(key);
        if (new_root != nullptr) {
            m_root = new_root;
        }
        return end();
    }
    key_type next_key = it->first;
    auto new_root = cur->erase(key);
    if (new_root != nullptr) {
        m_root = new_root;
    }
    return find(next_key);
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
template <class ForwardIt>
void BPTree<key_type, mapped_type, BlockSize, Less>::insert(ForwardIt begin, ForwardIt end)
{
    auto it = begin;
    for (; it < end; it++) {
        insert(it->first, it->second);
    }
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::insert(const key_type & key, mapped_type && value) -> std::pair<iterator, bool>
{
    if (m_root == nullptr) {
        delete m_root;
        m_root = new Leaf();
        dynamic_cast<Leaf *>(m_root)->insert_cell(0, key, std::move(value));
        m_size++;
        return {begin(), true};
    }

    iterator it = find(key);
    if (it != end()) {
        return {it, false};
    }
    Leaf * cur = m_root->find_leaf(key);
    Node * new_root = cur->insert(key, std::move(value));
    if (new_root != nullptr) {
        m_root = new_root;
    }
    m_size++;
    return {find(key), true};
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::insert(const key_type & key, const mapped_type & value) -> std::pair<iterator, bool>
{
    mapped_type v(value);
    return insert(key, std::move(v));
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
void BPTree<key_type, mapped_type, BlockSize, Less>::insert(std::initializer_list<value_type> list)
{
    for (auto v : list) {
        insert(v.first, v.second);
    }
}
template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::equal_range(const key_type & key) const -> std::pair<const_iterator, const_iterator>
{
    return {lower_bound(key), upper_bound(key)};
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::equal_range(const key_type & key) -> std::pair<iterator, iterator>
{
    return {lower_bound(key), upper_bound(key)};
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
void BPTree<key_type, mapped_type, BlockSize, Less>::clear()
{
    delete m_root;
    m_root = nullptr;
    m_size = 0;
}
template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::count(const key_type & key) const -> size_type
{
    return contains(key);
}
template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::lower_bound(const key_type & key) const -> const_iterator
{
    if (m_root == nullptr) {
        return cend();
    }
    Leaf * leaf = m_root->find_leaf(key);
    size_type i = leaf->find_in_block(key).first;
    return i == leaf->size() ? cend() : const_iterator(i, leaf, *this);
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::lower_bound(const key_type & key) -> iterator
{
    if (m_root == nullptr) {
        return end();
    }
    Leaf * leaf = m_root->find_leaf(key);
    size_type i = leaf->find_in_block(key).first;
    return i == leaf->size() ? end() : iterator(i, leaf, *this);
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::upper_bound(const key_type & key) -> iterator
{
    if (m_root == nullptr) {
        return end();
    }
    Leaf * leaf = m_root->find_leaf(key);
    for (size_type i = 0; i < leaf->size(); ++i) {
        if (cmp_keys(key, leaf->get_key(i))) {
            return {i, leaf, *this};
        }
        if (i == leaf->size() - 1 && leaf->get_next() != nullptr) {
            return {0, leaf->get_next(), *this};
        }
    }
    return end();
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
auto BPTree<key_type, mapped_type, BlockSize, Less>::upper_bound(const key_type & key) const -> const_iterator
{
    if (m_root == nullptr) {
        return cend();
    }
    Leaf * leaf = m_root->find_leaf(key);
    for (size_type i = 0; i < leaf->size(); ++i) {
        if (cmp_keys(key, leaf->get_key(i))) {
            return {i, leaf, *this};
        }
        if (i == leaf->size() - 1 && leaf->get_next() != nullptr) {
            return {0, leaf->get_next(), *this};
        }
    }
    return cend();
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
bool BPTree<key_type, mapped_type, BlockSize, Less>::contains(const key_type & key) const
{
    if (m_root == nullptr) {
        return false;
    }
    Leaf * leaf = m_root->find_leaf(key);
    if (leaf == nullptr)
        return false;
    auto [i, found] = leaf->find_in_block(key);
    return found;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
mapped_type & BPTree<key_type, mapped_type, BlockSize, Less>::operator[](const key_type & key)
{
    auto it = find(key);
    if (it != end()) {
        return it->second;
    }
    mapped_type v{};
    insert(key, v);
    return (*find(key)).second;
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
mapped_type & BPTree<key_type, mapped_type, BlockSize, Less>::at(const key_type & key)
{
    auto it = find(key);
    if (it != end()) {
        return it->second;
    }
    throw std::out_of_range("out_of_range");
}

template <class key_type, class mapped_type, std::size_t BlockSize, class Less>
const mapped_type & BPTree<key_type, mapped_type, BlockSize, Less>::at(const key_type & key) const
{
    const_iterator it = find(key);
    if (it != cend()) {
        return it->second;
    }
    throw std::out_of_range("out_of_range");
}
