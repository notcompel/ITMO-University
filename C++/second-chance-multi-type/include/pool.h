#pragma once

#include <algorithm>
#include <cstddef>
#include <initializer_list>
#include <new>
#include <vector>

class PoolAllocator
{
    struct Block
    {
        std::size_t m_obj_size;
        std::vector<bool> m_used_map;
        std::byte * m_begin;

        void * allocate();

        void deallocate(const std::byte * b_ptr);

        Block(const std::size_t block_size, const std::size_t obj_size, std::byte * begin)
            : m_obj_size(obj_size)
            , m_used_map(block_size / obj_size)
            , m_begin(begin)
        {
        }
    };

    const std::size_t m_block_size;
    std::vector<std::byte> m_storage;
    std::vector<Block> m_blocks;

public:
    PoolAllocator(const std::size_t block_size, std::initializer_list<std::size_t> sizes)
        : m_block_size(block_size)
        , m_storage(block_size * sizes.size())
    {
        std::byte * ptr = m_storage.data();
        for (const auto cur : sizes) {
            m_blocks.emplace_back(block_size, cur, ptr);
            ptr += block_size;
        }
        std::sort(m_blocks.begin(), m_blocks.end(), [](const Block & a, const Block & b) { return a.m_obj_size < b.m_obj_size; });
    }
    void * allocate(const std::size_t n);

    void deallocate(const void * ptr);
};
