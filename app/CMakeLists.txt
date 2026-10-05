cmake_minimum_required(VERSION 3.22.1)
project(cervixontop_native)

add_library(native-lib SHARED native-lib.cpp)

add_library(libnode SHARED IMPORTED)
set_target_properties(libnode PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libnode/bin/${ANDROID_ABI}/libnode.so"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/libnode/include/node"
)

target_include_directories(native-lib PRIVATE
    "${CMAKE_SOURCE_DIR}/libnode/include/node"
)

target_link_libraries(native-lib libnode log android)
