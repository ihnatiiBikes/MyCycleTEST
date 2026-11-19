cmake_minimum_required(VERSION 3.31.6)
project(csvMerge)

set(CMAKE_CXX_STANDARD 11)

add_executable(csvMerge
        main.cpp
        mappers/trek_mapper.cpp
        mappers/brompton_mapper.cpp
        mappers/madison_mapper.cpp
        csv_processor.cpp
        common.cpp
        common.h
        output/outputFileManager.cpp
        mappers/cyclex_mapper.cpp
        mappers/cyclex_mapper.h
        mappers/cube_mapper.h
        mappers/cube_mapper.cpp
)
