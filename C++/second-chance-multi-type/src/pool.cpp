#include "pool.h"

#include <cassert>
#include <functional>

void * PoolAllocator::Block::allocate()
{
    auto it = std::find(m_used_map.begin(), m_used_map.end(), false);
    if (it != m_used_map.end()) {
        *it = true;
        return m_begin + (it - m_used_map.begin()) * m_obj_size;
    }
    throw std::bad_alloc{};
}

void PoolAllocator::Block::deallocate(const std::byte * b_ptr)
{
    const std::size_t offset = (b_ptr - m_begin) / m_obj_size;
    assert(((b_ptr - m_begin) % m_obj_size) == 0);
    m_used_map[offset] = false;
}

void * PoolAllocator::allocate(const std::size_t n)
{
    auto it = std::lower_bound(m_blocks.begin(), m_blocks.end(), n, [](const Block & a, const std::size_t value) { return a.m_obj_size < value; });
    if (it->m_obj_size == n) {
        return it->allocate();
    }
    throw std::bad_alloc{};
}

void PoolAllocator::deallocate(const void * ptr)
{
    auto b_ptr = static_cast<const std::byte *>(ptr);
    const auto begin = m_storage.data();
    std::less<const std::byte *> cmp;
    if (!cmp(b_ptr, begin) && cmp(b_ptr, begin + m_storage.size())) {
        const std::size_t number_of_block = (b_ptr - begin) / m_block_size;
        m_blocks[number_of_block].deallocate(b_ptr);
    }
}
