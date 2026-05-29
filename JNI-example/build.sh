#!/bin/bash

rm -r build classes

mkdir classes
javac -h . Rumbler.java -d classes

mkdir build
g++ -std=c++23 \
   -fPIC \
   -I $JAVA_HOME/include \
   -I $JAVA_HOME/include/linux \
   -shared \
   -o build/libhaptic4j.so \
   Rumbler.cpp
