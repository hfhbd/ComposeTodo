#!/bin/sh
set -x

/usr/local/opt/openjdk/bin/java --version
export JAVA_HOME=/usr/local/opt/openjdk
java --version
cd ..
cd ..
./gradlew assembleXCFramework
