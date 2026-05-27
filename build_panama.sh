#!/bin/bash

rm -r generated/ classes/

jextract \
    --output generated \
    -t org.haptic \
    -l :./libhaptic.so \
    "wrapper.h"
javac generated/org/**/*.java -d classes
javac -cp .:classes Rumbler.java -d classes


#javac -h . Rumbler.java
#g++ -std=c++23 \
#    -fPIC \
#    -I /usr/lib/jvm/java-21-openjdk-amd64/include \
#    -I /usr/lib/jvm/java-21-openjdk-amd64/include/linux \
#    -shared \
#    -o libhaptic.so \
#    Rumbler.cpp

