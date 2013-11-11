#!/bin/sh

# Build maven
mvn package

mv target/oracle-*.jar /Users/botskonet/Minecraft/TestServer/plugins/oracle-nightly.jar

echo "BUILD COMPLETE"