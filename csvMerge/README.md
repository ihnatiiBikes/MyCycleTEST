# csvMerge

`csvMerge` is a C++ application designed to transform and merge product data from various vendor
CSV/text files (Trek, Brompton, Madison, and Cyclex) into a unified format suitable for ecommerce
platforms.

## Features

- **Multi-Vendor Support**: Custom mappers for Trek, Brompton, Madison, and Cyclex.
- **Stock Management**: Integrates stock availability data for Trek and Madison.
- **Automatic File Splitting**: Large output files are automatically split into smaller chunks
  (approx. 7MB each) for easier processing by destination systems.
- **Interactive Menu**: Easy-to-use command-line interface to select which brands to process.
- **Batch Processing**: Ability to process multiple brands in one go.

## Prerequisites

- **C++ Compiler**: Supports C++11 or later (e.g., GCC, Clang, MSVC).
- **CMake**: Version 3.31 or higher (as per `CMakeLists.txt`).

## Building the Project

1. **Clone the repository** (if applicable) or navigate to the project root.
2. **Create a build directory**:
   ```bash
   mkdir build
   cd build
   ```
3. **Configure with CMake**:
   ```bash
   cmake ..
   ```
4. **Build the project**:
   ```bash
   cmake --build .
   ```

The executable `csvMerge` will be created in your build directory.

## Usage

### 1. Prepare Input Files

Place your vendor files in the `input/` directory. The application expects specific filenames as
defined in `input/inputFileName.h`:

- **Trek**: `input/ENIE_Generic_Product.csv`
- **Trek Stock**: `input/IE_availability.csv`
- **Brompton**: `input/Ireland_2024_PriceList_1.csv`
- **Madison**: `input/MM7045.csv`
- **Madison Stock**: `input/stockdownload_m7045_20251018061922370.csv`
- **Cyclex**: `input/Cyclex_Ecommerce_File_13-08-25.csv`

*Note: You can modify these paths in `input/inputFileName.h` before building.*

### 2. Run the Application

Execute the compiled binary:

```bash
./csvMerge
```

### 3. Interactive Menu

Upon running, you will see a menu:

```
Choose the Brand to transform:
1. Trek
2. Brompton
3. Madison
4. Cyclex
0. Exit
Enter one or more options (space separated): 
```

- **Process a single brand**: Enter its number (e.g., `1`) and press Enter.
- **Process multiple brands**: Enter space-separated numbers (e.g., `1 3 4`) and press Enter.
- **Exit**: Enter `0` or an empty line.

### 4. Output

The processed data will be saved in the `output/` directory.

- Base output file: `output/Product.csv`
- If the output exceeds 7MB, additional files like `output/Product_1.csv`, `output/Product_2.csv`,
  etc., will be created.

## Project Structure

- `main.cpp`: Application entry point and menu logic.
- `csv_processor.h/cpp`: Core logic for reading input and coordinating transformations.
- `mappers/`: Contains specific logic for each vendor's data format.
- `stock_manager.h/cpp`: Handles loading and querying stock information.
- `output/outputFileManager.h/cpp`: Manages file writing and automatic splitting.
- `common.h/cpp`: Shared utility functions and constants.
- `input/inputFileName.h`: Configuration for input/output file paths.

## Configuration

You can customize the following in `common.h`:

- `BRANDS_TO_REMOVE`: A set of brand names to exclude during processing.

You can customize the following in `output/output_file_manager.h`:

- `maxFileSize`: The maximum size for each output file chunk (default is 7MB).
