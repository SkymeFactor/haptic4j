#!/bin/bash

export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
java -cp .:classes --enable-native-access=ALL-UNNAMED Rumbler
