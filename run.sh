#!/bin/bash

# export JAVA_HOME=/usr/lib/jvm/java-27-openjdk-amd64/
# 

javac RumbleTest.java
export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
java --enable-native-access=ALL-UNNAMED RumbleTest
