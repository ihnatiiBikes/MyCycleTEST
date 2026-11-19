//
// Created by POS4 on 26/08/2026.
//

#ifndef CUBE_MAPPER_H
#define CUBE_MAPPER_H

#include <vector>
#include <string>
#include <unordered_map>

class CubeMapper {
public:
    CubeMapper();

    void MapFields(const std::vector<std::string> &fields,
                   std::unordered_map<std::string, std::string> &outputProduct) const;

private:
    std::unordered_map<std::string, std::string> categoryMap_;
    std::unordered_map<std::string, std::string> brandMap_;
    std::vector<std::string> requiredHeaders_;

    void InitializeCategoryMaps() {
        categoryMap_ = {
                {"Cross", "272,389"},
                {"Fully", "272,273,387"},
                {"Gravel", "272,389"},
                {"Hardtail", "272,273,386,277"},
                {"Hybrid Gravel", "272,389"},
                {"Hybrid MTB", "272,273,277"},
                {"Hybrid Trekking", "272,273,277"},
                {"Hybrid Urban", "272,273,277"},
                {"Kids", "272,276"},
                {"Road", "272,274"},
                {"Trekking", "272,389,277"},
                {"Urban", "272,277"}
        };
    }


    void InitializeRequiredHeaders() {
        requiredHeaders_ = {
                "EAN barcode", "Box scancode", "SKU", "Modelcode", "Family", "Type", "Model short", "Product title",
                "Status", "Model title", "Size", "Colour", " Basic dealer price IRL ", " RRP IRL ",
                " Excluded from dealer discount ", "HS-code", "Product", "Product Description short", "Brand",
                "Product class", "Public Product Type", "Product Group", "Model Family", "Description", "Description Frame",
                "Description Equipment", "Frame", "Color", "Size", "Sizes", "Fork", "Frame shock", "Shock hardware",
                "Headset", "Stem", "Handlebar", "Handlebar Stem Combo", "Aero bar", "Handlebar Tape", "Grips",
                "Rear Derailleur", "Front Derailleur", "Chain Guide", "Shifters", "Shift Break Lever Unit", "Brake Lever",
                "Brakes", "Bottom Bracket", "Crankset", "Cassette", "Hub Sprocket", "Chain", "Belt", "Wheelset", "Rims",
                "Hub Front", "Hub Rear", "Tires", "Tires Front", "Tires Rear", "Pedals", "Saddle", "Seatpost",
                "Seatpost clamp", "Headlight", "Rear Light", "Kickstand", "Mudguard", "Chainguard", "Bell", "Carrier",
                "Motor", "Battery", "Charger", "Remote", "Display", "Weight", "System Weight max.", "Driver max. Weight",
                "Weight max.", "Body Size", "Stride Length", "GpsSystem", "EAN", "Box-Label-Code Org CUBE",
                "Length Packaging-Unit", "Width Packaging-Unit", "Height Packaging-Unit", "Gross Weight", "Net Weight",
                "Country of origin", "Customs tarif number"
        };
    }
};

#endif //CUBE_MAPPER_H