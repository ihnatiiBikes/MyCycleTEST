#include "csv_processor.h"
#include "common.h"
#include "mappers/trek_mapper.h"
#include "mappers/brompton_mapper.h"
#include "mappers/cyclex_mapper.h"
#include "mappers/cube_mapper.h"
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <unordered_set>
#include <sstream>
#include "input/inputFileName.h"
#include "output/outputFileManager.h"


namespace {
    bool fileExists(const std::string &filename) {
        std::ifstream f(filename);
        return f.good();
    }
}

CSVProcessor::CSVProcessor(TrekMapper &trekMapper, MadisonMapper &madisonMapper,
                           BromptonMapper &bromptonMapper, CyclexMapper &cyclexMapper, CubeMapper &cubeMapper)
    : trekMapper_(trekMapper), madisonMapper_(madisonMapper),
      bromptonMapper_(bromptonMapper),
      cyclexMapper_(cyclexMapper),
      cubeMapper_(cubeMapper) {
}

void CSVProcessor::ProcessTrekFiles(const std::string &trekFile, OutputFileManager &outputManager) const {
    if (!fileExists(trekFile)) {
        throw std::runtime_error("Trek input file not found: " + trekFile);
    }

    std::ifstream in(trekFile);
    if (!in.is_open()) throw std::runtime_error("Cannot open input: " + trekFile);

    std::string line;
    std::getline(in, line); // skip header
    int errorCount = 1;
    while (std::getline(in, line)) {
        auto fields = Common::SplitCSVLine(line);

        std::ostringstream oss;
        ProcessTrekLine(fields, oss, errorCount); // write line to a string buffer
        outputManager.WriteLine(oss.str()); // ensures file splitting
    }
}


void CSVProcessor::ProcessTrekLine(const std::vector<std::string> &fields,
                                   std::ostream &out, int &errorCount) const {
    std::unordered_map<std::string, std::string> product;
    trekMapper_.MapFields(fields, product);
    auto skuIt = product.find("productcode");
    if (skuIt == product.end()) {
        return;
    }
    std::string cleanSku = Common::Trim(skuIt->second);

    trekMapper_.MapFields(fields, product);
    if (product.find("productname") == product.end()) {
        product["productname"] = ""; // Default empty name
    }

    WriteOutputRow(out, product);
}

void CSVProcessor::ProcessMadisonFiles(const std::string &madisonFile, OutputFileManager &outputManager) const {
    if (!fileExists(madisonFile)) {
        throw std::runtime_error("Madison input file not found: " + madisonFile);
    }

    std::ifstream in(madisonFile);
    if (!in.is_open()) throw std::runtime_error("Cannot open input: " + madisonFile);

    std::string line;
    //std::cout << line<< std::endl;
    std::getline(in, line); // skip header
    std::getline(in, line); // skip header

    while (std::getline(in, line)) {
        auto fields = Common::SplitCSVLine(line, '|');

        std::ostringstream oss;
        ProcessMadisonLine(fields, oss);
        outputManager.WriteLine(oss.str());
    }
}

void CSVProcessor::ProcessCyclexFiles(const std::string &cyclexFile, OutputFileManager &outputManager) const {
    if (!fileExists(cyclexFile)) {
        throw std::runtime_error("Cyclex input file not found: " + cyclexFile);
    }
    std::ifstream in(cyclexFile);
    if (!in.is_open()) throw std::runtime_error("Cannot open input: " + cyclexFile);

    std::string line;
    std::getline(in, line); // skip header
    std::cout << "open file: " << line << std::endl;

    //int i = 0;
    while (std::getline(in, line)) {
        auto fields = Common::SplitCSVLine(line, ',');
        //std::cout << fields[i] << std::endl;
        //i++;
        std::ostringstream oss;
        ProcessCyclexLine(fields, oss);
        outputManager.WriteLine(oss.str());
    }
}

void CSVProcessor::ProcessCubeFiles(const std::string &cubeFile, OutputFileManager &outputManager) const {
    if (!fileExists(cubeFile)) {
        throw std::runtime_error("Cube input file not found: " + cubeFile);
    }
    std::cout << "Cube file found: " << cubeFile << std::endl;

    std::ifstream in(cubeFile);
    if (!in.is_open()) throw std::runtime_error("Cannot open input: " + cubeFile);

    std::string line;
    std::getline(in, line); // skip header

    //int i = 0;
    while (std::getline(in, line)) {
        auto fields = Common::SplitCSVLine(line, ',');
        std::ostringstream oss;
        ProcessCubeLine(fields, oss);
        outputManager.WriteLine(oss.str());
    }
}


void CSVProcessor::ProcessMadisonLine(const std::vector<std::string> &fields,
                                      std::ostream &out) const {
    std::unordered_map<std::string, std::string> product;
    madisonMapper_.MapFields(fields, product, true);
    auto skuIt = product.find("productcode");
    if (skuIt == product.end()) {
        return;
    }
    std::string cleanSku = Common::Trim(skuIt->second);
    //std::cout << cleanSku<<std::endl;

    madisonMapper_.MapFields(fields, product, true);
    //std::cout << product.find("productname") << std::endl;
    if (product.find("productname") == product.end()) {
        product["productname"] = ""; // Default empty name
    }

    WriteOutputRow(out, product);
}

void CSVProcessor::ProcessCyclexLine(const std::vector<std::string> &fields,
                                     std::ostream &out) const {
    std::unordered_map<std::string, std::string> product;
    cyclexMapper_.MapFields(fields, product);

    WriteOutputRow(out, product);
}

void CSVProcessor::ProcessCubeLine(const std::vector<std::string> &fields,
                                     std::ostream &out) const {
    std::unordered_map<std::string, std::string> product;
    cubeMapper_.MapFields(fields, product);

    WriteOutputRow(out, product);
}

void CSVProcessor::ProcessBromptonFiles(const std::string &bromptonFile, OutputFileManager &outputManager) const {
    if (!fileExists(bromptonFile)) {
        throw std::runtime_error("Brompton input file not found: " + bromptonFile);
    }

    std::ifstream in(bromptonFile);
    if (!in.is_open()) throw std::runtime_error("Cannot open input: " + bromptonFile);

    std::string line;
    std::getline(in, line); // skip header

    while (std::getline(in, line)) {
        auto fields = Common::SplitCSVLine(line);

        std::ostringstream oss;
        ProcessBromptonLine(fields, oss);
        outputManager.WriteLine(oss.str()); // Ensures file splitting
    }
}

void CSVProcessor::ProcessBromptonLine(const std::vector<std::string> &fields,
                                       std::ostream &out) const {
    std::unordered_map<std::string, std::string> product;
    BromptonMapper::MapFields(fields, product);
    auto skuIt = product.find("productcode");
    if (skuIt == product.end()) {
        return;
    }
    std::string cleanSku = Common::Trim(skuIt->second);
    if (product.find("productname") == product.end()) {
        product["productname"] = ""; // Default empty name
    }

    WriteOutputRow(out, product);
}

void CSVProcessor::WriteOutputHeader(std::ostream &out) const {
    for (size_t i = 0; i < outputHeaders_.size(); ++i) {
        if (i) out << ',';
        out << outputHeaders_[i];
    }
    out << '\n';
}


void CSVProcessor::WriteOutputRow(std::ostream &out,
                                  const std::unordered_map<std::string, std::string> &product) const {
    for (size_t i = 0; i < outputHeaders_.size(); ++i) {
        if (i) out << ',';
        const auto &h = outputHeaders_[i];
        auto it = product.find(h);
        //std::cout << "output headers: " << h << '\n'; //csvProcessorOutHeadersDisplay
        if (it != product.end()) {
            std::string v = it->second;
            bool needsQuotes = v.find_first_of(",\"\n") != std::string::npos;
            if (needsQuotes) {
                // escape any double quotes
                for (size_t p = 0; (p = v.find('"', p)) != std::string::npos; p += 2)
                    v.replace(p, 1, "\"\"");
                out << '"' << v << '"';
            } else {
                out << v;
            }
        }
    }
    out << '\n';
}

void CSVProcessor::CheckCodes()
{
    std::unordered_set<std::string> trekCodes;
    std::unordered_set<std::string> bromptonCodes;
    std::unordered_set<std::string> madisonCodes;
    std::unordered_set<std::string> cyclexCodes;
    std::unordered_set<std::string> cubeCodes;

    auto loadCodes = [](const std::string& filename, size_t codeIndex, int skipLines, char delimiter = ',') {
        std::unordered_set<std::string> codes;
        std::string actualPath = filename;
        std::ifstream file(actualPath);
        if (!file.is_open()) {
            actualPath = "../" + filename;
            file.open(actualPath);
        }
        if (!file.is_open()) {
            std::cout << "Warning: Could not open file (tried " << filename << " and ../" + filename << ")" << std::endl;
            return codes;
        }

        std::string line;
        for (int i = 0; i < skipLines; ++i) {
            std::getline(file, line);
        }
        while (std::getline(file, line)) {
            auto fields = Common::SplitCSVLine(line, delimiter);
            if (codeIndex < fields.size()) {
                std::string code = Common::Trim(fields[codeIndex]);
                if (!code.empty()) {
                    codes.insert(code);
                }
            }
        }
        return codes;
    };

    std::cout << "Loading input files..." << std::endl;
    trekCodes = loadCodes(inputTrekFile, 2, 1, ',');
    bromptonCodes = loadCodes(inputBromptonFile, 0, 1, ',');
    madisonCodes = loadCodes(inputMadisonFile, 0, 2, '|');
    cyclexCodes = loadCodes(inputCyclexFile, 2, 1, ',');
    cubeCodes = loadCodes(inputCubeFile, 2, 1, ',');

    std::vector<std::string> checkCodesList;
    std::ifstream inputCodes(inputCodesFile);
    if (!inputCodes.is_open()) {
        inputCodes.open(inputCodesFile);
    }
    if (!inputCodes.is_open()) {
        std::cerr << "Error: Could not open checkCodes.csv" << std::endl;
        return;
    }

    std::string line;
    while (std::getline(inputCodes, line)) {
        std::string code = Common::Trim(line);
        if (!code.empty()) {
            checkCodesList.push_back(code);
        }
    }

    std::ofstream outFile(outputCodesFile);

    if (!outFile.is_open()) {
        outFile.open(outputCodesFile);
    }

    if (!outFile.is_open()) {
        std::cerr << "Error: Could not create temp.csv" << std::endl;
        return;
    }

    outFile << "productCode,whereIsCodeFrom\n";

    for (const auto& code : checkCodesList) {
        std::vector<std::string> sources;
        if (trekCodes.count(code)) sources.push_back("Trek");
        if (bromptonCodes.count(code)) sources.push_back("Brompton");
        if (madisonCodes.count(code)) sources.push_back("Madison");
        if (cyclexCodes.count(code)) sources.push_back("Cyclex");
        if (cubeCodes.count(code)) sources.push_back("Cube");

        std::string sourceStr = "";
        if (sources.empty()) {
            sourceStr = "not in list";
        } else {
            for (size_t i = 0; i < sources.size(); ++i) {
                if (i > 0) sourceStr += ";";
                sourceStr += sources[i];
            }
        }

        outFile << code << "," << sourceStr << "\n";
    }

    std::cout << "temp.csv successfully populated at " << outputCodesFile << " with " << checkCodesList.size() << " product codes." << std::endl;
}
