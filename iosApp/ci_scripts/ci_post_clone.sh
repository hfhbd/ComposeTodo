#!/bin/sh

brew install openjdk
/Users/local/Homebrew/opt/openjdk/bin/java --version

mkdir ~/.gradle
echo "systemProp.gpr.user=XcodeCloud
systemProp.gpr.key=${GHP_KEY}" >> ~/.gradle/gradle.properties
