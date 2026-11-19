//
// Created by ignatiys on 7/15/25.
//

#include "outputFileManager.h"
#include <iostream>
#include <stdexcept>
#include "../csv_processor.h"

OutputFileManager::OutputFileManager(
    const std::string &baseOutputFile,
    std::function<void(std::ofstream &)> writeHeaderFunc)
    : baseOutputFile(baseOutputFile), writeHeaderFunc(std::move(writeHeaderFunc)) {
    OpenNewFile();
}

void OutputFileManager::OpenNewFile() {
    if (out.is_open()) out.close();

    if (fileCounter == 0) {
        currentOutputFile = baseOutputFile;
    } else {
        size_t dotPos = baseOutputFile.find_last_of('.');
        if (dotPos != std::string::npos) {
            currentOutputFile = baseOutputFile.substr(0, dotPos) + "_" +
                                std::to_string(fileCounter) +
                                baseOutputFile.substr(dotPos);
        } else {
            currentOutputFile = baseOutputFile + "_" + std::to_string(fileCounter);
        }
    }

    out.open(currentOutputFile);
    if (!out.is_open())
        throw std::runtime_error("Cannot open output: " + currentOutputFile);

    // Reset line count for the new file
    currentLineCount = 0;

    writeHeaderFunc(out);
}

void OutputFileManager::WriteLine(const std::string &line) {
    // Trigger split if file size limit OR line count limit (5000) is exceeded
    if ((out.tellp() + static_cast<std::streamoff>(line.size()) >= maxFileSize) ||
        currentLineCount >= maxLineCount) {
        ++fileCounter;
        OpenNewFile();
        }

    out << line;
    ++currentLineCount;
}

void OutputFileManager::WriteLineDirect(const std::string &line) {
    out << line << '\n';
    ++currentLineCount;
}

void OutputFileManager::Close() {
    if (out.is_open()) out.close();
}

std::ofstream& OutputFileManager::Stream() {
    return out;
}