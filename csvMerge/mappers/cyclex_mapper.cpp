//
// Created by POS4 on 03/10/2025.
//
#include "cyclex_mapper.h"

#include "madison_mapper.h"
#include <vector>
#include <algorithm>
#include <sstream>
#include <set>
#include "../common.h"
#include <iostream>

CyclexMapper::CyclexMapper() {
    InitializeCategoryMaps();
    InitializeBrandMaps();
    InitializeRequiredHeaders();
}


void CyclexMapper::MapFields(const std::vector <std::string> &fields,
                             std::unordered_map <std::string, std::string> &outputProduct) const {

    auto removeCommas = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ','), s.end());
        return s;
    };
    auto removeSpaces = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ' '), s.end());
        return s;
    };
    const std::string photoUrl = "/v/cyclexPhoto/";

    auto splitIds = [](const std::string &ids) {
        std::set <std::string> uniqueIds;
        std::istringstream iss(ids);
        std::string id;
        while (std::getline(iss, id, ',')) {
            if (!id.empty()) uniqueIds.insert(id);
        }
        return uniqueIds;
    };

    auto joinIds = [](const std::set <std::string> &ids) {
        std::ostringstream oss;
        bool first = true;
        for (const auto &id: ids) {
            if (!first) oss << ",";
            oss << id;
            first = false;
        }
        return oss.str();
    };
    const std::string productcode = Common::SafeGet(fields, 2);


    outputProduct["productcode"] = productcode;
    outputProduct["upc_code"] = Common::SafeGet(fields, 3);
    outputProduct["productname"] = (Common::SafeGet(fields, 1) == "") ? productcode
                                                                      : Common::SafeGet(fields, 1);
    outputProduct["productnameshort"] = Common::SafeGet(fields, 1).substr(0, 50);
    outputProduct["google_size"] =
            Common::SafeGet(fields, 8).substr(0, 50) + Common::SafeGet(fields, 9);
    outputProduct["google_gender"] = "Unisex";
    outputProduct["google_color"] = Common::SafeGet(fields, 7);
    outputProduct["inv_lastverified"] = Common::CURRENT_DATE;
    outputProduct["productprice"] = removeCommas(Common::SafeGet(fields, 5));
    int weight = 1;
    if (std::stof(removeCommas(Common::SafeGet(fields, 5))) > 73) {
        weight = 3;
    } else if (std::stof(removeCommas(Common::SafeGet(fields, 5))) > 250) {
        weight = 5;
    }
    outputProduct["productweight"] = std::to_string(weight);
    outputProduct["photourl"] = Common::SafeGet(fields, 10);
    outputProduct["photourl_large"] = Common::SafeGet(fields, 10);
    outputProduct["photourl_small"] = Common::SafeGet(fields, 10);

    // // Photos
    // outputProduct["photourl"] = photoUrl + productcode + '/' + productcode + "_01.jpg";
    // outputProduct["photourl_large"] = photoUrl + productcode + '/' + productcode + "_01.jpg"
    //                                   + photoUrl + productcode + '/' + productcode + "_02.jpg";
    // outputProduct["photourl_small"] = photoUrl + productcode + '/' + productcode + "_01" + ".jpg";

    // Features etc
    outputProduct["productfeatures"] = Common::SafeGet(fields, 18);
    outputProduct["stockstatus"] = "5";
    outputProduct["vat_percentage"] = "23";
    outputProduct["google_product_category"] = Common::SafeGet(fields, 48).substr(0, 254);

    // Category and brand mapping
    const std::string categoryKey = removeSpaces(
            (Common::Trim(Common::SafeGet(fields, 48).substr(0, 4))));
    const std::string brandKey = removeSpaces((Common::Trim(Common::SafeGet(fields, 49))));
    // Get category and brand IDs (with fallbacks)
    std::string categoryIds = (categoryMap_.find(categoryKey) != categoryMap_.end())
                              ? (categoryMap_.at(categoryKey))
                              : "85,257";

    std::string brandIds = (brandMap_.find(brandKey) != brandMap_.end())
                           ? (brandMap_.at(brandKey))
                           : "85,257";

    std::set <std::string> combinedIds = splitIds(categoryIds);
    std::set <std::string> brandIdSet = splitIds(brandIds);
    combinedIds.insert(brandIdSet.begin(), brandIdSet.end());
    outputProduct["categoryids"] = joinIds(combinedIds);
    outputProduct["productmanufacturer"] = Common::SafeGet(fields, 49).substr(0, 49);
    outputProduct["availability"] = "In Stock in warehouse";
    if (Common::SafeGet(fields, 10).empty()) {
        outputProduct["hideproduct"] = "Y";
    } else outputProduct["hideproduct"] = "N";
    outputProduct["taxableproduct"] = "Y";
    outputProduct["freeshippingitem"] = "N";
    outputProduct["estdaystoship"] = "7";
    outputProduct["google_availability"] = "in stock";
}
