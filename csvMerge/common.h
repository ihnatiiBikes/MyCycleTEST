#pragma once
#include <algorithm>
#include <string>
#include <vector>
#include <unordered_map>
#include <set>
#include <sstream>
#include <iomanip>
#include <ctime>

namespace Common {
    // Shared constants
    const std::string CURRENT_DATE = []() {
        auto now = std::time(nullptr);
        std::tm ltm{};

#if defined(_WIN32)
        localtime_s(&ltm, &now);
#else
        localtime_r(&now, &ltm);
#endif

        std::ostringstream oss;
        oss << std::put_time(&ltm, "%m/%d/%Y %H:%M:%S");
        return oss.str();
    }();



    const std::set<std::string> BRANDS_TO_REMOVE = {
        "adventure", "aeroe", "airpop", "amplitude", "atranvelo", "axa", "basil",
        "bikefitting", "crud products", "dugast", "dynaplug", "elite", "fist handwear",
        "garmin", "gtechniq", "kingston", "lifesystems", "lifeventure", "motion pro",
        "odi", "ogio", "petzl", "profile design", "quad lock", "slime", "smartwasher",
        "storm", "tooo cycling", "trelock", "truflo", "urban iki"
    };

    // Shared utilities
    std::string Trim(const std::string& str);
    std::string SafeGet(const std::vector<std::string>& fields, size_t index);
    std::vector<std::string> SplitCSVLine(const std::string& line, char delimiter = ',');
    inline std::string ToLower(std::string str) {
        std::transform(str.begin(), str.end(), str.begin(),
                       [](unsigned char c) { return std::tolower(c); });
        return str;
    }
    double SafeStod(const std::string& str);
}