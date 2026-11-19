#include "brompton_mapper.h"

#include <algorithm>

#include "../common.h"
#include <iostream>

BromptonMapper::BromptonMapper()
{

}

void BromptonMapper::MapFields(const std::vector<std::string>& fields,
                               std::unordered_map<std::string, std::string>& outputProduct)
{
    auto removeCommas = [](std::string s)
    {
        s.erase(std::remove(s.begin(), s.end(), ','), s.end());
        return s;
    };
    std::string weight = "1";
    if (Common::SafeGet(fields, 5) == "Demo") return;
    outputProduct["productcode"] = Common::SafeGet(fields, 0);
    // 1. Fetch the raw field value
    std::string rawName = Common::SafeGet(fields, 2);
    std::string productName = rawName; // Default fallback if no color code is found

    // 2. Find and remove the color code (e.g., "[#C1D0D7]")
    size_t colorPos = rawName.find(" [#");
    if (colorPos == std::string::npos) {
        colorPos = rawName.find("[#"); // fallback without space
    }

    if (colorPos != std::string::npos) {
        productName = rawName.substr(0, colorPos);
        // Trim any trailing whitespace left behind
        productName.erase(productName.find_last_not_of(" \t") + 1);
    }

    std::string categoryKey = Common::SafeGet(fields, 6);
    const auto& catMap = GetCategoryMap();
    auto it = catMap.find(categoryKey);

    if (it != catMap.end()) {
        outputProduct["categoryids"] = Common::ToLower(it->second);
    } else {
        outputProduct["categoryids"] = "";
    }
    outputProduct["productname"] = productName;
    outputProduct["productprice"] = removeCommas(Common::SafeGet(fields, 4));
    if (std::stof(removeCommas(Common::SafeGet(fields, 4))) > 1300)
    {
        weight = "10";
    } else
    {
        weight = "3";
    }
    outputProduct["productweight"] = weight;
    outputProduct["productdescription"] = Common::SafeGet(fields, 2);


    if (Common::SafeGet(fields, 20).empty())
    {
        outputProduct["hideproduct"] = "Y";
    }
    outputProduct["stockstatus"] = "1";
    outputProduct["google_product_category"] = Common::SafeGet(fields, 5);
    outputProduct["productmanufacturer"] = "Brompton";
    outputProduct["availability"] = "1";
    outputProduct["taxableproduct"] = "Y";
    outputProduct["freeshippingitem"] = "N";
    outputProduct["estdaystoship"] = "42";
    outputProduct["google_availability"] = "in stock";
    std::string firstPhoto = Common::SafeGet(fields, 20);
    std::string secondPhoto = Common::SafeGet(fields, 21);
    outputProduct["photourl_large"] = firstPhoto;
    outputProduct["photourl_small"] = firstPhoto.empty() ? secondPhoto : firstPhoto;

}
