//
// Created by ignatiys on 6/30/25.
//


#include "common.h"
#include <algorithm>
#include <cctype>
#include <sstream>
#include <string>

//const std::string Common::CURRENT_DATE = "06/30/2025"; // Update as needed
static std::string toASCII(const std::string &str) {
    std::string cleanStr;
    cleanStr.reserve(str.size()); // Avoid extra allocations

    for (char c : str) {
        // Keeps standard printable ASCII and whitespace (0–127)
        if (static_cast<unsigned char>(c) <= 127) {
            cleanStr += c;
        }
    }
    return cleanStr;
}

std::string Common::Trim(const std::string &str) {
    std::string asciiStr = toASCII(str);
    size_t pos = 0;
    while ((pos = asciiStr.find("\"\"", pos)) != std::string::npos) {
        asciiStr.erase(pos, 2);
    }

    const size_t start = asciiStr.find_first_not_of(" \t\n\r");
    if (start == std::string::npos) return "";

    const size_t end = asciiStr.find_last_not_of(" \t\n\r");

    return asciiStr.substr(start, end - start + 1);
}

std::vector<std::string> Common::SplitCSVLine(const std::string &line, char delimiter) {
    std::vector<std::string> fields;
    fields.reserve(32);

    std::string field;
    field.reserve(64);

    bool inQuotes = false;
    const char *data = line.data();
    const size_t length = line.length();

    for (size_t i = 0; i < length; ++i) {
        const char c = data[i];

        // Handle quote rules: only "," opens/closes quotes
        if (c == '"') {
            // Opening quote: quote at start or after delimiter
            if (!inQuotes && (i == 0 || data[i - 1] == delimiter)) {
                inQuotes = true;
                continue;
            }
            // Closing quote: quote before delimiter
            else if (inQuotes && i + 1 < length && data[i + 1] == delimiter) {
                inQuotes = false;
                continue;
            }
        }

        if (c == delimiter && !inQuotes) {
            // Only apply ASCII check if you really need it
            fields.push_back(std::move(field));
            field.clear();
            field.reserve(64);
        } else {
            field += c;
        }
    }

    fields.push_back(std::move(field));
    return fields;
}

std::string Common::SafeGet(const std::vector<std::string> &fields, size_t index) {
    if (index >= fields.size()) {
        return "";
    }
    std::string val = Trim(fields[index]);
    size_t pos = 0;
    while ((pos = val.find('\n', pos)) != std::string::npos) {
        if (pos > 0 && val[pos - 1] == '\r') {
            val.replace(pos - 1, 2, "<br>");
            pos += 4; // length of "<br>"
        } else {
            val.replace(pos, 1, "<br>");
            pos += 4; // length of "<br>"
        }
    }
    return val;
}

