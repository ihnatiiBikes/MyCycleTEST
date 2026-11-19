//
// Created by POS4 on 03/10/2025.
//
#include "cube_mapper.h"

#include "madison_mapper.h"
#include <vector>
#include <algorithm>
#include <sstream>
#include <set>
#include "../common.h"
#include <iostream>

CubeMapper::CubeMapper() {
    InitializeCategoryMaps();
    InitializeRequiredHeaders();
}

// there are a stupid fix for kids bikes check
void CubeMapper::MapFields(const std::vector<std::string> &fields,
                           std::unordered_map<std::string, std::string> &outputProduct) const {
    auto removeSpaces = [](std::string s) {
        s.erase(std::remove(s.begin(), s.end(), ' '), s.end());
        return s;
    };
    //const std::string photoUrl = "/v/vspfiles/photos/cubePhotos/";

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

    auto keepOnlyNumeric = [](const std::string &str) {
        std::string result;
        for (char ch: str) {
            if (std::isdigit(static_cast<unsigned char>(ch)) || ch == '.') {
                result += ch;
            }
        }
        return result;
    };


    const std::string productcode = Common::SafeGet(fields, 2);
    // Basic product info
    outputProduct["productcode"] = productcode;
    outputProduct["upc_code"] = Common::SafeGet(fields, 0);
    outputProduct["productname"] = Common::SafeGet(fields, 16);
    outputProduct["productnameshort"] = Common::SafeGet(fields, 17).substr(0, 50);
    outputProduct["google_size"] = Common::SafeGet(fields, 10).substr(0, 50);
    outputProduct["google_gender"] = "Unisex";
    outputProduct["google_color"] = Common::SafeGet(fields, 11).substr(0, 50);
    outputProduct["inv_lastverified"] = Common::CURRENT_DATE;
    outputProduct["productprice"] = keepOnlyNumeric(fields[13]);
    outputProduct["productweight"] = Common::SafeGet(fields, 88);
    outputProduct["productdescription"] = Common::SafeGet(fields, 23);
    outputProduct["productdescriptionshort"] = Common::SafeGet(fields, 20) + ": " + Common::SafeGet(fields, 22);
    // Photos

    //outputProduct["photourl"] = productcode + "-1.jpg";
    //outputProduct["photourl_large"] = " "; //photoUrl  + Common::SafeGet(fields, 3) + "_S_00.jpg";
    //outputProduct["photourl_small"] = " "; // photoUrl + Common::SafeGet(fields, 3) + "_S_00.jpg";

    //outputProduct["techspecs"] = Common::SafeGet(fields, 23);// Array of parameter names starting from index 30
    // Array of parameter names starting from index 30
    const std::vector<std::string> paramNames = {
        "Fork", "Frame shock", "Shock hardware", "Headset", "Stem", "Handlebar",
        "Handlebar Stem Combo", "Aero bar", "Handlebar Tape", "Grips", "Rear Derailleur",
        "Front Derailleur", "Chain Guide", "Shifters", "Shift Break Lever Unit",
        "Brake Lever", "Brakes", "Bottom Bracket", "Crankset", "Cassette", "Hub Sprocket",
        "Chain", "Belt", "Wheelset", "Rims", "Hub Front", "Hub Rear", "Tires",
        "Tires Front", "Tires Rear", "Pedals", "Saddle", "Seatpost", "Seatpost clamp",
        "Headlight", "Rear Light", "Kickstand", "Mudguard", "Chainguard", "Bell",
        "Carrier", "Motor", "Battery", "Charger", "Remote", "Display", "Weight",
        "System Weight max.", "Driver max. Weight", "Weight max.", "Body Size",
        "Stride Length", "GpsSystem"
    };

    std::string techSpecs;

    // Index 26 handling for "Frame"
    std::string frameVal = Common::SafeGet(fields, 26);
    if (!frameVal.empty()) {
        techSpecs += "<strong>Frame:</strong> ";
        techSpecs += frameVal;
        techSpecs += "<br>";
    }

    // Loop starting from index 30
    int currentIndex = 30;
    for (const auto &name: paramNames) {
        std::string val = Common::SafeGet(fields, currentIndex++);
        if (!val.empty()) {
            techSpecs += "<strong>";
            techSpecs += name;
            techSpecs += ":</strong> ";
            techSpecs += val;
            techSpecs += "<br>";
        }
    }

    outputProduct["techspecs"] = techSpecs;
    outputProduct["stockstatus"] = "5";
    outputProduct["vat_percentage"] = "23";
    outputProduct["google_product_category"] = Common::SafeGet(fields, 4).substr(0, 254);

    // Category and brand mapping
    const std::string categoryKey = removeSpaces((Common::Trim(Common::SafeGet(fields, 5))));
    const std::string categoryIds = (categoryMap_.find(categoryKey) != categoryMap_.end())
                                        ? (categoryMap_.at(categoryKey))
                                        : "272";

    outputProduct["categoryids"] = categoryIds;
    outputProduct["productmanufacturer"] = "Cube";


    // Static fields
    outputProduct["availability"] = "Contact for availability";
    outputProduct["hideproduct"] = "N";
    outputProduct["taxableproduct"] = "Y";
    outputProduct["freeshippingitem"] = "N";
    outputProduct["estdaystoship"] = "7";
    outputProduct["google_availability"] = "in stock";
}
