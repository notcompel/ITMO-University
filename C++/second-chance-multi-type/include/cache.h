#pragma once

#include <algorithm>
#include <list>
#include <ostream>
#include <type_traits>

template <class Key, class KeyProvider, class Allocator>
class Cache
{
public:
    template <class... AllocArgs>
    Cache(const std::size_t cache_size, AllocArgs &&... alloc_args)
        : m_max_size(cache_size)
        , m_alloc(std::forward<AllocArgs>(alloc_args)...)
    {
    }

    std::size_t size() const
    {
        return m_queue.size();
    }

    bool empty() const
    {
        return m_queue.empty();
    }

    template <class T>
    T & get(const Key & key);

    std::ostream & print(std::ostream & strm) const;

    friend std::ostream & operator<<(std::ostream & strm, const Cache & cache)
    {
        return cache.print(strm);
    }

private:
    struct Object
    {
        KeyProvider * key;
        bool flag;

        Object(KeyProvider * key, bool flag = false)
            : key(key)
            , flag(flag)
        {
        }
    };
    const std::size_t m_max_size;
    Allocator m_alloc;
    std::list<Object> m_queue;

    template <class T>
    T & shift(const Key & key)
    {
        if (m_max_size <= size()) {
            while (m_queue.front().flag) {
                m_queue.push_back(m_queue.front().key);
                m_queue.pop_front();
            }
            m_alloc.template destroy<KeyProvider>(m_queue.front().key);
            m_queue.pop_front();
        }
        T * value = m_alloc.template create<T>(key);
        m_queue.emplace_back(value);
        return *value;
    }
};

template <class Key, class KeyProvider, class Allocator>
template <class T>
inline T & Cache<Key, KeyProvider, Allocator>::get(const Key & key)
{
    for (auto & cur : m_queue) {
        if (*cur.key == key) {
            cur.flag = true;
            return *static_cast<T *>(cur.key);
        }
    }
    return shift<T>(key);
}

template <class Key, class KeyProvider, class Allocator>
inline std::ostream & Cache<Key, KeyProvider, Allocator>::print(std::ostream & strm) const
{
    bool key = true;
    for (const auto & ptr : m_queue) {
        if (!key) {
            strm << " ";
        }
        else {
            key = false;
        }
        strm << ptr.key << " " << ptr.flag;
    }
    strm << "\n";
    return strm;
}
