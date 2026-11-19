#include "madison_mapper.h"
#include <vector>
#include <algorithm>
#include <sstream>
#include <set>
#include "../common.h"
#include <iostream>

MadisonMapper::MadisonMapper() {
    InitializeCategoryMaps();
    InitializeBrandMaps();
    InitializeRequiredHeaders();
}
//TODO sort the box prices

void MadisonMapper::MapFields(const std::vector<std::string> &fields,
                              std::unordered_map<std::string, std::string> &outputProduct,
                              bool stockstatus) {
    std::string photoPath = "/v/madisonPhoto/1/";

    auto toLower = [](const std::string &str) {
        std::string lowerStr = str;
        std::transform(lowerStr.begin(), lowerStr.end(), lowerStr.begin(),
                       [](unsigned char c) { return std::tolower(c); });
        return lowerStr;
    };


    auto removeCommas = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ','), s.end());
        return s;
    };
    auto removeSpaces = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ' '), s.end());
        return s;
    };

    if (removeCommas(Common::SafeGet(fields, 8)).empty()) return;
    if (removeCommas(Common::SafeGet(fields, 0)).empty()) return;
    const float price = std::stof(removeCommas(Common::SafeGet(fields, 8)));

    if (Common::SafeGet(fields, 17) == "Y" || Common::SafeGet(fields, 17) == "y") {
        return;
    }


    // Lambda function to split comma-separated string into set of unique IDs
    auto splitIds = [](const std::string &ids) {
        std::set<std::string> uniqueIds;
        std::istringstream iss(ids);
        std::string id;
        while (std::getline(iss, id, ',')) {
            if (!id.empty()) uniqueIds.insert(id);
        }
        return uniqueIds;
    };

    // Lambda function to join set into comma-separated string
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

    // Basic product info
    outputProduct["productcode"] = Common::SafeGet(fields, 0);
    outputProduct["upc_code"] = Common::SafeGet(fields, 2);
    outputProduct["productname"] = Common::SafeGet(fields, 12).substr(0, 50);
    outputProduct["productnameshort"] = Common::SafeGet(fields, 1).substr(0, 25);
    outputProduct["productdescription"] = Common::SafeGet(fields, 12) + "<br>Barcode: " + Common::SafeGet(fields, 2);
    outputProduct["productdescriptionshort"] = Common::SafeGet(fields, 12).substr(0, 30);
    outputProduct["google_size"] = Common::SafeGet(fields, 4).substr(0, 50);
    outputProduct["google_gender"] = "Unisex";
    outputProduct["google_color"] = Common::SafeGet(fields, 3);
    outputProduct["inv_lastverified"] = Common::CURRENT_DATE;
    outputProduct["productprice"] = std::to_string(price);
    float weight = 10;
    if (price > 73) {
        weight = 5;
    } else if (price > 250) {
        weight = 3;
    }
    outputProduct["productweight"] = std::to_string(weight);

    // Photos
    outputProduct["photourl"] = photoPath + toLower(Common::SafeGet(fields, 13)) + ".jpg";
    outputProduct["photourl_large"] = photoPath + toLower(Common::SafeGet(fields, 13)) + ".jpg";
    outputProduct["photourl_small"] = photoPath + toLower(Common::SafeGet(fields, 13)) + ".jpg";

    // Features etc
    outputProduct["productfeatures"] = Common::SafeGet(fields, 18);
    outputProduct["stockstatus"] = stockstatus ? "5" : "0";
    outputProduct["vat_percentage"] = Common::SafeGet(fields, 11) == "0" ? "23" : "0";
    outputProduct["google_product_category"] = Common::SafeGet(fields, 12);

    // Category and brand mapping
    const std::string categoryKey = removeSpaces(toLower(Common::Trim(Common::SafeGet(fields, 1).substr(0, 4))));
    const std::string brandKey = removeSpaces(toLower(Common::Trim(Common::SafeGet(fields, 5))));

    std::string categoryIds = (categoryMap_.find(categoryKey) != categoryMap_.end())
                                  ? toLower(categoryMap_[categoryKey])
                                  : "";
    std::string brandIds = (brandMap_.find(brandKey) != brandMap_.end())
                               ? toLower(brandMap_[brandKey])
                               : "";

    std::set<std::string> combinedIds = splitIds(categoryIds);
    std::set<std::string> brandIdSet = splitIds(brandIds);
    combinedIds.insert(brandIdSet.begin(), brandIdSet.end());
    outputProduct["categoryids"] = joinIds(combinedIds);
    outputProduct["productmanufacturer"] = Common::SafeGet(fields, 5);


    // Static fields
    outputProduct["availability"] = stockstatus ? "In Stock in warehouse" : "Not in stock";
    outputProduct["hideproduct"] = "N";
    outputProduct["taxableproduct"] = "Y";
    outputProduct["freeshippingitem"] = "N";
    outputProduct["estdaystoship"] = "7";
    outputProduct["google_availability"] = "in stock";
}
