//
// Created by ignatiys on 7/15/25.
//

#ifndef OUTPUTFILEMANAGER_H
#define OUTPUTFILEMANAGER_H

#pragma once

#include <fstream>
#include <string>
#include <functional>
const std::string outputFile = "output/Product.csv";
const std::string outputCodesFile = "output/whereCodesFrom.csv";

class OutputFileManager {
public:
    OutputFileManager(const std::string &baseOutputFile,
                      std::function<void(std::ofstream &)> writeHeaderFunc);

    void WriteLine(const std::string &line);

    void WriteLineDirect(const std::string &line);

    std::ofstream &Stream();

    void Close();

private:
    void OpenNewFile();

    std::string baseOutputFile;
    std::string currentOutputFile;
    std::ofstream out;
    size_t fileCounter = 0;
    std::function<void(std::ofstream &)> writeHeaderFunc;
    const std::streamsize maxFileSize = 799 * 1024 * 1024;
    size_t currentLineCount = 0;
    const size_t maxLineCount = 100000;
};


#endif //OUTPUTFILEMANAGER_H
