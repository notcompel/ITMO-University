#include <algorithm>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

class SortApp
{
    std::istream & in;
    const bool n_option, f_option;

    static bool parse_num(const std::string & s, long long & num)
    {
        std::size_t i = 0;
        while (i < s.size() && std::isspace(s[i])) {
            i++;
        }
        if (i == s.size()) {
            num = 0;
            return true;
        }
        if (s[i] == '-') {
            i++;
        }
        for (; i < s.size(); ++i) {
            if (!std::isdigit(s[i])) {
                return false;
            }
        }
        num = std::stoll(s);
        return true;
    }

    static bool comp_n(const std::string & a, const std::string & b)
    {
        long long x;
        long long y;
        if (!parse_num(a, x) || !parse_num(b, y)) {
            return a < b;
        }
        if (x == y) {
            return a < b;
        }
        return x < y;
    }

    static bool comp_f(std::string a, std::string b)
    {
        std::transform(a.begin(), a.end(), a.begin(), [](unsigned char c) { return std::tolower(c); });
        std::transform(b.begin(), b.end(), b.begin(), [](unsigned char c) { return std::tolower(c); });
        return a < b;
    }

    static bool comp_default(const std::string & a, const std::string & b)
    {
        return a < b;
    }

    template <class T>
    static void sort(std::vector<std::string> & data, bool (*cmp)(T, T))
    {
        std::sort(data.begin(), data.end(), cmp);
    }

public:
    SortApp(std::istream & in, const bool n_option, const bool f_option)
        : in(in)
        , n_option(n_option)
        , f_option(f_option)
    {
    }

    void run_app()
    {
        std::vector<std::string> data;
        std::string cur;
        while (std::getline(in, cur)) {
            data.push_back(cur);
        }
        if (n_option) {
            sort(data, comp_n);
        }
        else if (f_option) {
            sort(data, comp_f);
        }
        else {
            sort(data, comp_default);
        }
        for (const auto & str : data) {
            std::cout << str << std::endl;
        }
    }
};

int main(int argc, char ** argv)
{
    bool n = false;
    bool f = false;
    const std::string n_name = "-n";
    const std::string n_full_name = "--ignore-case";
    const std::string f_name = "-f";
    const std::string f_full_name = "--numeric-sort";
    const std::string nf_name = "-nf";

    int i;
    for (i = 1; i < argc; i++) {
        if (argv[i] == n_name || argv[i] == n_full_name) {
            n = true;
        }
        else if (argv[i] == f_name || argv[i] == f_full_name) {
            f = true;
        }
        else if (argv[i] == nf_name) {
            f = true;
            n = true;
        }
        else {
            break;
        }
    }

    std::ifstream fin;
    if (i < argc) {
        fin.open(argv[i]);
    }
    std::istream & in(fin.is_open() ? fin : std::cin);
    SortApp app(in, n, f);
    app.run_app();
    fin.close();
    return 0;
}
