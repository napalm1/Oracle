#!/bin/sh

# Build maven
mvn package

mv target/oracle-nightly.jar /Users/botskonet/Minecraft/TestServer/plugins/oracle-nightly.jar

echo "BUILD COMPLETE"