//
// Created by ignatiys on 7/10/25.
//

#ifndef BROMPTON_MAPPER_H
#define BROMPTON_MAPPER_H

#include <vector>
#include <string>
#include <unordered_map>

class BromptonMapper
{
public:
    BromptonMapper();
    static void MapFields(const std::vector<std::string>& fields,
                          std::unordered_map<std::string, std::string>& outputProduct);

private:
    static const std::unordered_map<std::string, std::string>& GetCategoryMap()
    {
        static const std::unordered_map<std::string, std::string> categoryMap = {
                {"Bike accessories", "185,233"},
                {"C Line", "409,60,186"},
                {"C Line e-Motiq", "413,282,60,186"},
                {"G Line", "410,60,186"},
                {"G Line City + e-Motiq", "414,282,60,186"},
                {"G Line e-Motiq", "414,282,60,186"},
                {"P Line", "411,60,186"},
                {"P Line e-Motiq", "415,282,60,186"},
                {"Parts", "417,185,233"},
                {"T Line", "412,60,186"},
                {"T Line e-Motiq", "416,282,60,186"}
        };
        return categoryMap;
    }

    static const std::unordered_map<std::string, std::string>& GetBrandMap()
    {
        static const std::unordered_map<std::string, std::string> brandMap = {
                {"brompton", "33"}
        };
        return brandMap;
    }

    static const std::vector<std::string>& GetRequiredHeaders()
    {
        static const std::vector<std::string> headers = {
                "SKU - Item code", "EAN 13", "Product name", "Trade Price", "MSRP", "Category 1", "Category 2",
                "Category 3", "Category 4", "Category 5", "Category 6", "Charger plug", "Colour", "Gears", "Handlebar", "Lighting",
                "Rack", "Saddle height", "Tyres", "Saddle", "Picture 1", "Picture 2"
        };
        return headers;
    }

    std::vector<std::string> requiredHeaders_;
};

#endif //BROMPTON_MAPPER_H