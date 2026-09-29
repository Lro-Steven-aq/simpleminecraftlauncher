#!/usr/bin/env sh

# error: i need to parse the json.
CLASS_PATH="/home/steven/桌面/.minecraft/versions/26.1.2-Fabric/26.1.2-Fabric.jar\
:/home/steven/桌面/.minecraft/libraries"

ASSETS_DIR="/home/steven/桌面/.minecraft/assets"
MINECRAFT_DIR="/home/steven/桌面/.minecraft/"
MAIN_CLASS="net.fabricmc.loader.impl.launch.knot.KnotClient"
#if command -v java > /dev/null 2>&1; then
if [ -n "$JAVA_HOME" ];then
    echo "found java"
    "$JAVA_HOME/bin/java" -Xmx4G -Xms4G \
        -XX:MaxGCPauseMillis=50 \
        -XX:+UseG1GC \
        -XX:+AlwaysPreTouch \
         -cp "$CLASS_PATH" \
         "$MAIN_CLASS" \
        --username steven \
        --uuid UUID \
        --accessToken ACCESS_TOKEN \
        --assetsDir "$ASSETS_DIR" --assetsIndex 26.1.2 \
        --gameDir "$MINECRAFT_DIR" --versionType release \
else
    echo "Can't find executable file: \"java\" in path or JAVA_HOME"
fi