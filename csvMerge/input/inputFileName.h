//
// Created by ignatiys on 7/15/25.
//

#ifndef INPUTFILENAME_H
#define INPUTFILENAME_H
const std::string inputTrekFile = "input/ENIE_Generic_Product.csv";
const std::string inputBromptonFile = "input/brompton-catalogue-A000110-2026-10-23.csv";
const std::string inputMadisonFile = "input/MM7045.csv";
const std::string inputCyclexFile = "input/Cyclex_Ecommerce_File_07-05-26.csv";
const std::string inputCubeFile = "input/Cube-epos.csv";
const std::string trekStockFile = "input/IE_availability.csv";
const std::string madisonStockFile = "input/stockdownload_m7045_20261022062254100.csv";
const std::string inputCodesFile = "input/checkCodes.csv";
inline void showMenu() {
    std::cout << "\nChoose the Brand to transform:\n"
            << "1. Trek\n"
            << "2. Brompton\n"
            << "3. Madison\n"
            << "4. Cyclex\n"
            << "5. Cube\n"
            << "99. Check Codes\n"
            << "0. Exit\n"
            << "Enter one or more options (space separated): ";
}

#endif //INPUTFILENAME_H
