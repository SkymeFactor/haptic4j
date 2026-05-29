#!/bin/bash

rm -r generated/ classes/

g++ -std=c++23 \
   -fPIC \
   -I $JAVA_HOME/include \
   -I $JAVA_HOME/include/linux \
   -shared \
   -o libhaptic4j.so \
   Rumbler.cpp

jextract \
    --output generated \
    -t org.haptic4j \
    -l :./libhaptic4j.so \
    "Rumbler.h"
javac generated/org/**/*.java -d classes
javac -cp .:classes Rumbler.java UIController.java -d classes
