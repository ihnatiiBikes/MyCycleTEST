#include "mycycle_mapper.h"
#include <algorithm>
#include <iostream>
#include "../common.h"

MyCycleMapper::MyCycleMapper() {
    InitializeRequiredHeaders();
}

void MyCycleMapper::InitializeRequiredHeaders() {
    // This is just a placeholder if needed for validation
}

void MyCycleMapper::MapFields(const std::vector<std::string> &fields,
                             std::unordered_map<std::string, std::string> &outputProduct) const {
    auto safeGet = [&](size_t index) -> std::string {
        return (index < fields.size()) ? Common::Trim(fields[index]) : "";
    };

    auto removeCommas = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ','), s.end());
        return s;
    };

    // Mapping based on the provided input headers
    // productcode: index 0
    // productname: index 2
    // stockstatus: index 6
    // upc_code: index 25
    // productnameshort: index 27
    // productweight: index 28
    // productprice: index 38
    // saleprice: index 40
    // availability: index 45
    // customfield3: index 88
    // productmanufacturer: index 93
    // inv_lastverified: index 101
    // photourl_small: index 106
    // photourl_large: index 107
    // productdescriptionshort: index 127
    // productdescription: index 128
    // productfeatures: index 129
    // techspecs: index 130
    // extinfo: index 131

    auto cleanValue = [](std::string s) {
        if (!s.empty() && s.front() == '"' && s.back() == '"') {
            s = s.substr(1, s.size() - 2);
        }
        return s;
    };

    outputProduct["productcode"] = cleanValue(safeGet(0));
    outputProduct["productname"] = cleanValue(safeGet(2));
    outputProduct["stockstatus"] = cleanValue(safeGet(6));
    outputProduct["upc_code"] = cleanValue(safeGet(25));
    outputProduct["productnameshort"] = cleanValue(safeGet(27));
    outputProduct["productweight"] = cleanValue(safeGet(28));
    outputProduct["productprice"] = removeCommas(cleanValue(safeGet(38)));
    outputProduct["saleprice"] = removeCommas(cleanValue(safeGet(40)));
    outputProduct["availability"] = cleanValue(safeGet(45));
    outputProduct["customfield3"] = cleanValue(safeGet(88));
    outputProduct["productmanufacturer"] = cleanValue(safeGet(93));
    outputProduct["inv_lastverified"] = cleanValue(safeGet(101));
    outputProduct["photourl_small"] = cleanValue(safeGet(106));
    outputProduct["photourl_large"] = cleanValue(safeGet(107));
    outputProduct["productdescriptionshort"] = cleanValue(safeGet(127));
    outputProduct["productdescription"] = cleanValue(safeGet(128));
    outputProduct["productfeatures"] = cleanValue(safeGet(129));
    outputProduct["techspecs"] = cleanValue(safeGet(130));
    outputProduct["extinfo"] = cleanValue(safeGet(131));
}
