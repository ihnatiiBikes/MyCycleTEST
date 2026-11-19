#include <iostream>
#include <sstream>

#include "csv_processor.h"
#include "mappers/trek_mapper.h"
#include "input/inputFileName.h"
#include "mappers/cyclex_mapper.h"
#include "output/outputFileManager.h"


int main() {
    try {
        // Initialize components
        TrekMapper trekMapper;
        MadisonMapper madisonMapper;
        BromptonMapper bromptonMapper;
        CyclexMapper cyclexMapper;
        CubeMapper cubeMapper;
        CSVProcessor processor(trekMapper,
                               madisonMapper,
                               bromptonMapper,
                               cyclexMapper,
                               cubeMapper);
        OutputFileManager outputFileManager(
                outputFile,
                [&processor](std::ofstream &out) {
                    processor.WriteOutputHeader(out);
                });


        auto processChoices = [&](const std::vector<int> &choices) {
            for (int choice: choices) {
                switch (choice) {
                    case 1:
                        std::cout << "Processing Trek..." << std::endl;
                        processor.ProcessTrekFiles(inputTrekFile, outputFileManager);
                        break;
                    case 2:
                        std::cout << "Processing Brompton..." << std::endl;
                        processor.ProcessBromptonFiles(inputBromptonFile, outputFileManager);
                        break;
                    case 3:
                        std::cout << "Processing Madison..." << std::endl;
                        processor.ProcessMadisonFiles(inputMadisonFile, outputFileManager);
                        break;
                    case 4:
                        std::cout << "Processing Cyclex..." << std::endl;
                        processor.ProcessCyclexFiles(inputCyclexFile, outputFileManager);
                        break;
                    case 5:
                        std::cout << "Processing Cube..." << std::endl;
                        processor.ProcessCubeFiles(inputCubeFile, outputFileManager);
                        break;
                    case 99:
                        std::cout << "Checking codes..." << std::endl;
                        CSVProcessor::CheckCodes();
                        break;
                    case 0:
                        std::cout << "Exiting..." << std::endl;
                        exit(0);
                    default:
                        std::cout << "Ignoring invalid choice: " << choice << std::endl;
                }
            }
        };


        while (true) {
            showMenu();

            std::string input;
            std::getline(std::cin, input);

            if (input.empty() || input == "0") {
                std::cout << "Exiting..." << std::endl;
                break;
            }

            std::vector<int> choices;
            std::istringstream iss(input);
            int num;

            while (iss >> num) {
                choices.push_back(num);
            }

            // Clear any error state if non-integer was entered
            if (iss.fail() && !iss.eof()) {
                std::cout << "Invalid input. Please enter numbers only." << std::endl;
                continue;
            }

            processChoices(choices);
        }

        std::cout << "Processing completed successfully" << std::endl;
        std::cout << "Output file created: " << outputFile << std::endl;
    } catch (const std::exception &e) {
        std::cerr << "Error: " << e.what() << std::endl;
        return 1;
    }
    return 0;
}
