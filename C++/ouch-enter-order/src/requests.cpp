#include "requests.h"

#include <string>

namespace {

void encode_new_order_opt_fields(unsigned char * bitfield_start,
                                 const char time_in_force,
                                 const char capacity)
{
    auto * p = bitfield_start + new_order_bitfield_num;
#define FIELD(name, bitfield_num, bit)                    \
    set_opt_field_bit(bitfield_start, bitfield_num, bit); \
    p = encode_field_##name(p, name);
#include "new_order_opt_fields.inl"
}

void encode_replace_order_opt_fields(unsigned char * bitfield_start,
                                     const char time_in_force)
{
    auto * p = bitfield_start + new_order_bitfield_num;
#define FIELD(name, bitfield_num, bit)                    \
    set_opt_field_bit(bitfield_start, bitfield_num, bit); \
    p = encode_field_##name(p, name);
#include "replace_order_opt_fields.inl"
}

char convert_side(const Side side)
{
    switch (side) {
    case Side::Buy:
        return 'B';
    case Side::Sell:
        return 'S';
    }
    return 0;
}

uint32_t convert_price(OrdType ord_type, double price)
{
    switch (ord_type) {
    case OrdType::Market:
        return 0x7FFFFFFF;
    case OrdType::Limit:
        const double order = 10000;
        const double epsilon = 1e-5;
        return static_cast<uint32_t>(price * order + std::copysign(epsilon, price));
    }
    return 0;
}

char convert_time_in_force(const TimeInForce time_in_force)
{
    switch (time_in_force) {
    case TimeInForce::Day:
        return '0';
    case TimeInForce::IOC:
        return '3';
    }
    return 0;
}

char convert_capacity(const Capacity capacity)
{
    switch (capacity) {
    case Capacity::Agency:
        return '1';
    case Capacity::Principal:
        return '2';
    case Capacity::RisklessPrincipal:
        return '7';
    }
    return 0;
}

uint32_t parse(std::string s)
{
    uint32_t res = 0;
    for (size_t i = 0; i < s.size(); i++) {
        res = res * 10 + s[i] - '0';
    }
    return res;
}

} // namespace

std::vector<unsigned char> create_enter_order_request(
        const std::string & cl_ord_id,
        Side side,
        double volume,
        double price,
        const std::string & symbol,
        OrdType ord_type,
        TimeInForce time_in_force,
        Capacity capacity,
        const std::string & firm,
        const std::string & user)
{
    std::vector<unsigned char> msg(44);

    auto * p = &msg[0];
    p = encode_char(p, static_cast<unsigned char>('O'));
    p = encode_text(p, cl_ord_id, 14);
    p = encode_char(p, static_cast<uint8_t>(convert_side(side)));
    p = encode_binary4(p, static_cast<uint32_t>(volume));
    p = encode_binary4(p, parse(symbol));
    p = encode_binary4(p, convert_price(ord_type, price));
    p = encode_text(p, firm, 4);
    p = encode_text(p, user, 6);
    encode_new_order_opt_fields(p,
                                convert_time_in_force(time_in_force),
                                convert_capacity(capacity));

    return msg;
}

std::vector<unsigned char> create_replace_order_request(
        const std::string & old_cl_ord_id,
        const std::string & new_cl_ord_id,
        const double total_volume,
        double price,
        const TimeInForce time_in_force,
        const std::string & user)
{
    std::vector<unsigned char> msg(48);

    auto * p = &msg[0];
    p = encode_char(p, static_cast<unsigned char>('U'));
    p = encode_text(p, old_cl_ord_id, 14);
    p = encode_text(p, new_cl_ord_id, 14);
    p = encode_binary4(p, static_cast<uint32_t>(total_volume));
    p = encode_binary4(p, convert_price(OrdType::Limit, price));
    p = encode_text(p, user, 6);
    encode_replace_order_opt_fields(p,
                                    convert_time_in_force(time_in_force));

    return msg;
}
