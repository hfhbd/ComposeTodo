#!/bin/sh

/Users/local/Homebrew/opt/openjdk/bin/java --version
export JAVA_HOME=/Users/local/Homebrew/opt/openjdk
java --version
cd ..
cd ..
./gradlew assembleXCFramework
