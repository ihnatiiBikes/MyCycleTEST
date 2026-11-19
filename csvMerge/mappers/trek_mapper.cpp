#include "trek_mapper.h"

#include <algorithm>
#include <set>
#include <sstream>
#include <iostream>
#include "../common.h"

TrekMapper::TrekMapper() {
    InitializeCategoryMaps();
    InitializeProductTypeMap();
    InitializeRequiredHeaders();
    InitializeWeightMap();
    InitializeUrlMap();
}

void TrekMapper::MapFields(const std::vector<std::string> &fields,
                           std::unordered_map<std::string, std::string> &outputProduct) {
    if (Common::SafeGet(fields, 11) == "PRE RELEASED") return;

    auto removeCommas = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ','), s.end());
        return s;
    };

    auto removeSpacesAndSpecial = [](std::string s) {
        s.erase(std::remove_if(s.begin(), s.end(), [](unsigned char c) {
            return std::isspace(c) || c == '(' || c == ')' || c == '-';
        }), s.end());
        return s;
    };

    auto splitIds = [](const std::string &ids) {
        std::set<std::string> uniqueIds;
        std::istringstream iss(ids);
        std::string id;
        while (std::getline(iss, id, ',')) {
            if (!id.empty()) uniqueIds.insert(id);
        }
        return uniqueIds;
    };

    auto joinIds = [](const std::set<std::string> &ids) {
        std::ostringstream oss;
        bool first = true;
        for (const auto &id: ids) {
            if (!first) oss << ",";
            oss << id;
            first = false;
        }
        return oss.str();
    };

    auto toComparable = [](const std::string &date) {
        std::string datePart = date.substr(0, 10);
        std::istringstream ss(datePart);
        std::string month, day, year;

        std::getline(ss, month, '/');
        std::getline(ss, day, '/');
        std::getline(ss, year);

        if (month.size() == 1) month = "0" + month;
        if (day.size() == 1) day = "0" + day;

        return year + month + day;
    };

    std::string productTypeByUrl;

    auto extractUrlKey = [&](const std::string &url) -> std::string {
        const std::string prefix = "https://www.trekbikes.com/ie/en_IE/";
        if (url.find(prefix) != 0) {
            productTypeByUrl = "";
            return "";
        }
        const size_t start1 = prefix.length();
        const size_t slash1 = url.find('/', start1);
        if (slash1 == std::string::npos) {
            productTypeByUrl = "";
            return "";
        }

        // Assign the first path segment (slash 1) to productTypeByUrl
        productTypeByUrl = url.substr(start1, slash1 - start1);

        const size_t start2 = slash1 + 1;
        const size_t slash2 = url.find('/', start2);
        if (slash2 == std::string::npos) {
            return url.substr(start2);
        }
        return url.substr(start2, slash2 - start2);
    };

    std::string rawCategory = Common::SafeGet(fields, 12);
    std::string productTypeKey = removeSpacesAndSpecial(rawCategory);
    std::string categoryField = Common::SafeGet(fields, 14);
    std::string urlKey = extractUrlKey(Common::SafeGet(fields, 41));
    const auto categoryIDs = categoryMap_.find(categoryField);
    const auto productTypeIds = productTypeMap_.find(productTypeKey);
    const auto urlIds = urlMap_.find(urlKey);

    if (categoryIDs != categoryMap_.end() && categoryIDs->second == "NON_DISPLAYABLE") {
        outputProduct.clear();
        return;
    }

    if (productTypeIds != productTypeMap_.end() && productTypeIds->second == "NON_DISPLAYABLE") {
        outputProduct.clear();
        return;
    }

    if (urlIds != urlMap_.end() && urlIds->second == "NON_DISPLAYABLE") {
        outputProduct.clear();
        return;
    }

    std::string price = removeCommas(Common::SafeGet(fields, 21));

    std::string salePrice = [&]() -> std::string {
        std::string effDate = Common::SafeGet(fields, 25);
        std::string expDate = Common::SafeGet(fields, 26);

        if (effDate.empty() || expDate.empty()) return "";

        std::string currentComp = toComparable(Common::CURRENT_DATE);
        std::string effComp = toComparable(effDate);
        std::string expComp = toComparable(expDate);

        return (currentComp >= effComp && currentComp <= expComp)
                   ? removeCommas(Common::SafeGet(fields, 24))
                   : "";
    }();

    std::string aggregatedDescription;
    for (size_t i = 46; i <= 89; ++i) {
        const std::string value = Common::SafeGet(fields, i);
        if (!value.empty()) {
            if (!aggregatedDescription.empty()) aggregatedDescription += ' ';
            aggregatedDescription += value;
        }
    }

    std::string shortDesc = !aggregatedDescription.empty() ? aggregatedDescription.substr(0, 30) : "";
    std::string fullDesc = !aggregatedDescription.empty() ? aggregatedDescription : Common::SafeGet(fields, 6);

    std::set<std::string> combinedIds;
    if (categoryIDs != categoryMap_.end() && categoryIDs->second != "NON_DISPLAYABLE" && categoryIDs->second != "0") {
        auto ids = splitIds(categoryIDs->second);
        combinedIds.insert(ids.begin(), ids.end());
    }
    if (productTypeIds != productTypeMap_.end() && productTypeIds->second != "NON_DISPLAYABLE" && productTypeIds->second
        != "0") {
        auto ids = splitIds(productTypeIds->second);
        combinedIds.insert(ids.begin(), ids.end());
    }
    if (urlIds != urlMap_.end() && urlIds->second != "NON_DISPLAYABLE" && urlIds->second != "0") {
        auto ids = splitIds(urlIds->second);
        combinedIds.insert(ids.begin(), ids.end());
    }
    std::string categoryIdsStr = joinIds(combinedIds);
    const auto weightIt = weightMap_.find(rawCategory);
    std::string weightVal = (weightIt != weightMap_.end()) ? weightIt->second : "10";

    std::string photoUrl = Common::SafeGet(fields, 42);
    std::string hideProductVal = photoUrl.empty() ? "Y" : "N";
    std::string rawUrl = Common::SafeGet(fields, 41);


    outputProduct["stockstatus"] = "1";
    outputProduct["availability"] = "1";
    outputProduct["taxableproduct"] = "Y";
    outputProduct["freeshippingitem"] = "N";
    outputProduct["estdaystoship"] = "7";
    outputProduct["google_availability"] = "in stock";
    outputProduct["ean"] = "";

    outputProduct["productcode"] = Common::SafeGet(fields, 2);
    outputProduct["upc_code"] = Common::SafeGet(fields, 1);
    outputProduct["productname"] = Common::SafeGet(fields, 6);
    outputProduct["productnameshort"] = Common::SafeGet(fields, 7).substr(0, 50);
    outputProduct["productmanufacturer"] = Common::SafeGet(fields, 4);
    outputProduct["google_size"] = Common::SafeGet(fields, 9).substr(0, 50);
    outputProduct["google_gender"] = Common::SafeGet(fields, 10);
    outputProduct["google_color"] = Common::SafeGet(fields, 33);
    outputProduct["productfeatures"] = Common::SafeGet(fields, 101);
    outputProduct["techspecs"] = Common::SafeGet(fields, 102);
    outputProduct["extinfo"] = Common::SafeGet(fields, 100);
    outputProduct["google_product_category"] = rawCategory;

    outputProduct["customfield2"] = "";
    outputProduct["customfield3"] = Common::SafeGet(fields, 8);
    outputProduct["inv_lastverified"] = Common::CURRENT_DATE;

    outputProduct["productprice"] = price;
    outputProduct["saleprice"] = salePrice;

    outputProduct["productdescriptionshort"] = shortDesc;
    outputProduct["productdescription"] = fullDesc;

    outputProduct["photourl_large"] = photoUrl;
    outputProduct["photourl_small"] = photoUrl;
    outputProduct["categoryids"] = categoryIdsStr;
    outputProduct["productweight"] = weightVal;
    outputProduct["hideproduct"] = hideProductVal;
    outputProduct["productpopularity"] = std::to_string(productTypeByUrl == "bikes" ? 1000 : 0);
}
