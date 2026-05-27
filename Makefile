.PHONY: all

all: rumble-test wrapper

rumble-test:
	g++ -std=c++23 rumble-test.cpp -o rumble-test

wrapper:
	g++ -std=c++23 -fPIC -I /usr/lib/jvm/java-21-openjdk-amd64/include -I /usr/lib/jvm/java-21-openjdk-amd64/include/linux  -shared -o libwrapper.so RumbleTest.cpp
